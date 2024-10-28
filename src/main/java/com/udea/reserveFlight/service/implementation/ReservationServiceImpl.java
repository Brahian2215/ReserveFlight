package com.udea.reserveFlight.service.implementation;

import com.udea.reserveFlight.persistence.entity.Flight;
import com.udea.reserveFlight.persistence.entity.Passenger;
import com.udea.reserveFlight.persistence.entity.Reservation;
import com.udea.reserveFlight.persistence.entity.ReservationPassenger;
import com.udea.reserveFlight.persistence.repository.IFlightRepository;
import com.udea.reserveFlight.persistence.repository.IPassengerRepository;
import com.udea.reserveFlight.persistence.repository.IReservationPassengerRepository;
import com.udea.reserveFlight.persistence.repository.IReservationRepository;
import com.udea.reserveFlight.presentation.dto.ReservationDTO;
import com.udea.reserveFlight.presentation.dto.ReservationSummaryDTO;
import com.udea.reserveFlight.service.exception.AppException;
import com.udea.reserveFlight.service.interfaces.IReservationService;
import com.udea.reserveFlight.util.ReservationCodeGenerator;
import com.udea.reserveFlight.util.ReservationConverter;
import com.udea.reserveFlight.util.ReservationPassengerConverter;
import com.udea.reserveFlight.util.SeatNumberGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository reservationRepository;
    private final IFlightRepository flightRepository;
    private final IPassengerRepository passengerRepository;
    private final IReservationPassengerRepository reservationPassengerRepository;
    private final ReservationConverter reservationConverter;
    private final ReservationPassengerConverter reservationPassengerConverter;
    private final SeatNumberGenerator seatNumberGenerator;
    private final ReservationCodeGenerator reservationCodeGenerator;

    public ReservationServiceImpl(IReservationRepository reservationRepository, IFlightRepository flightRepository, IPassengerRepository passengerRepository, IReservationPassengerRepository reservationPassengerRepository, ReservationConverter reservationConverter, ReservationPassengerConverter reservationPassengerConverter, SeatNumberGenerator seatNumberGenerator, ReservationCodeGenerator reservationCodeGenerator) {
        this.reservationRepository = reservationRepository;
        this.flightRepository = flightRepository;
        this.passengerRepository = passengerRepository;
        this.reservationPassengerRepository = reservationPassengerRepository;
        this.reservationConverter = reservationConverter;
        this.reservationPassengerConverter = reservationPassengerConverter;
        this.seatNumberGenerator = seatNumberGenerator;
        this.reservationCodeGenerator = reservationCodeGenerator;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationSummaryDTO> findAll() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(reservationConverter::reservationEntityToSummaryDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationDTO findById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new AppException("Reserva no encontrada con ID: " + id, "NOT_FOUND"));
        return reservationConverter.reservationToDTO(reservation);
    }

    @Override
    @Transactional
    public ReservationDTO save(ReservationDTO reservationDTO, List<Long> passengerIds) {
        if (passengerIds == null || passengerIds.isEmpty()) {
            throw new AppException("La lista de IDs de pasajeros no puede ser nula o vacía", "INVALID_ARGUMENT");
        }
        Flight flight = validateAndReduceSeats(reservationDTO.getFlight().getId(), passengerIds.size());

        String reservationCode = reservationCodeGenerator.generateReservationCode();
        reservationDTO.setReservationCode(reservationCode);

        Reservation reservation = new Reservation();
        reservation.setReservationCode(reservationCode);
        reservation.setFlight(flight);
        Reservation savedReservation = reservationRepository.save(reservation);

        associatePassengersWithReservation(savedReservation, passengerIds);
        return reservationConverter.reservationToDTO(savedReservation);
    }

    @Override
    @Transactional
    public ReservationDTO update(Long id, ReservationDTO reservationDTO, List<Long> passengerIds) {
        if (passengerIds == null || passengerIds.isEmpty()) {
            throw new AppException("La lista de pasajeros no puede ser nula o vacía", "INVALID_ARGUMENT");
        }

        // Buscar la reserva existente
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new AppException("Reserva no encontrada con ID: " + id, "NOT_FOUND"));

        // Validar y actualizar el vuelo si es diferente
        Flight currentFlight = reservation.getFlight();
        Flight newFlight = flightRepository.findById(reservationDTO.getFlight().getId())
                .orElseThrow(() -> new AppException("Vuelo no encontrado con ID: " + reservationDTO.getFlight().getId(), "NOT_FOUND"));

        if (!currentFlight.equals(newFlight)) {
            // Restaurar asientos del vuelo anterior
            restoreSeatsAvailability(currentFlight, reservation.getReservationPassenger().size());

            // Reducir asientos del nuevo vuelo
            validateAndReduceSeats(newFlight.getId(), passengerIds.size());
        } else {
            // Actualizar solo los asientos si el vuelo es el mismo
            int seatsDifference = passengerIds.size() - reservation.getReservationPassenger().size();
            if (seatsDifference > 0) {
                validateAndReduceSeats(currentFlight.getId(), seatsDifference);
            } else {
                restoreSeatsAvailability(currentFlight, -seatsDifference);
            }
        }

        // Actualizar el vuelo de la reserva
        reservation.setFlight(newFlight);

        // Eliminar pasajeros anteriores
        reservationPassengerRepository.deleteAll(reservation.getReservationPassenger());

        // Asociar nuevos pasajeros a la reserva
        associatePassengersWithReservation(reservation, passengerIds);

        // Guardar y devolver la reserva actualizada
        return reservationConverter.reservationToDTO(reservation);
    }

    @Override
    @Transactional
    public String delete(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new AppException("Reserva no encontrada con ID: " + id, "NOT_FOUND"));

        // Recuperar el vuelo asociado a la reserva
        Flight flight = reservation.getFlight();

        // Calcular el número de asientos a restaurar basándose en el número de pasajeros en la reserva
        int seatsToRestore = reservation.getReservationPassenger().size();

        // Restaurar la disponibilidad de asientos
        restoreSeatsAvailability(flight, seatsToRestore);

        reservationRepository.delete(reservation);
        return "Reserva eliminada con éxito";
    }

    private Flight validateAndReduceSeats(Long flightId, int seatsRequired) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new AppException("Vuelo no encontrado con ID: " + flightId, "NOT_FOUND"));

        if (flight.getSeatsAvailable() < seatsRequired) {
            throw new AppException("No hay suficientes asientos disponibles para los pasajeros", "SEATS_UNAVAILABLE");
        }
        // Reducir asientos disponibles
        flight.setSeatsAvailable(flight.getSeatsAvailable() - seatsRequired);
        flightRepository.save(flight);
        return flight;
    }

    @Override
    @Transactional
    public void restoreSeatsAvailability(Flight flight, int seatsToRestore) {
        flight.setSeatsAvailable(flight.getSeatsAvailable() + seatsToRestore);
        flightRepository.save(flight);
    }

    private void associatePassengersWithReservation(Reservation reservation, List<Long> passengerIds) {
        List<ReservationPassenger> reservationPassengers = passengerIds.stream()
                .distinct()
                .map(passengerId -> {
                    Passenger passenger = passengerRepository.findById(passengerId)
                            .orElseThrow(() -> new AppException("Pasajero no encontrado con ID: " + passengerId, "NOT_FOUND"));

                    return new ReservationPassenger(
                            null,
                            seatNumberGenerator.generateSeatNumber(),
                            LocalDateTime.now(),
                            reservation,
                            passenger
                    );
                })
                .collect(Collectors.toList());
        reservationPassengerRepository.saveAll(reservationPassengers);
        reservation.setReservationPassenger(reservationPassengers);
        reservationRepository.save(reservation);
    }

}

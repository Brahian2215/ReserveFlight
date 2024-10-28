package com.udea.reserveFlight.service.implementation;

import com.udea.reserveFlight.persistence.entity.Flight;
import com.udea.reserveFlight.persistence.entity.Passenger;
import com.udea.reserveFlight.persistence.entity.Reservation;
import com.udea.reserveFlight.persistence.entity.ReservationPassenger;
import com.udea.reserveFlight.persistence.repository.IFlightRepository;
import com.udea.reserveFlight.persistence.repository.IPassengerRepository;
import com.udea.reserveFlight.persistence.repository.IReservationPassengerRepository;
import com.udea.reserveFlight.persistence.repository.IReservationRepository;
import com.udea.reserveFlight.presentation.dto.ReservationPassengerDTO;
import com.udea.reserveFlight.presentation.dto.ReservationPassengerSummaryDTO;
import com.udea.reserveFlight.service.exception.AppException;
import com.udea.reserveFlight.service.interfaces.IReservationPassengerService;
import com.udea.reserveFlight.util.ReservationPassengerConverter;
import com.udea.reserveFlight.util.SeatNumberGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationPassengerServiceImpl implements IReservationPassengerService {

    private final IReservationPassengerRepository reservationPassengerRepository;
    private final IReservationRepository reservationRepository;
    private final IPassengerRepository passengerRepository;
    private final IFlightRepository flightRepository;
    private final ReservationPassengerConverter reservationPassengerConverter;
    private final SeatNumberGenerator seatNumberGenerator;

    public ReservationPassengerServiceImpl(IReservationPassengerRepository reservationPassengerRepository, IReservationRepository reservationRepository, IPassengerRepository passengerRepository, IFlightRepository flightRepository, ReservationPassengerConverter reservationPassengerConverter, SeatNumberGenerator seatNumberGenerator) {
        this.reservationPassengerRepository = reservationPassengerRepository;
        this.reservationRepository = reservationRepository;
        this.passengerRepository = passengerRepository;
        this.flightRepository = flightRepository;
        this.reservationPassengerConverter = reservationPassengerConverter;
        this.seatNumberGenerator = seatNumberGenerator;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationPassengerDTO> findAll() {
        List<ReservationPassenger> reservationPassengers = reservationPassengerRepository.findAll();
        return reservationPassengers.stream()
                .map(reservationPassengerConverter::reservationPassengerToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationPassengerDTO findById(Long id) {
        ReservationPassenger reservationPassenger = reservationPassengerRepository.findById(id)
                .orElseThrow(() -> new AppException("Reserva de pasajero no encontrada con ID: " + id, "NOT_FOUND"));
        return reservationPassengerConverter.reservationPassengerToDTO(reservationPassenger);
    }

    @Override
    @Transactional
    public ReservationPassengerDTO save(ReservationPassengerDTO reservationPassengerDTO) {
        ReservationPassenger reservationPassenger = buildReservationPassengerEntity(reservationPassengerDTO);
        ReservationPassenger savedReservationPassenger = reservationPassengerRepository.save(reservationPassenger);
        return reservationPassengerConverter.reservationPassengerToDTO(savedReservationPassenger);
    }

    @Override
    @Transactional
    public ReservationPassengerDTO update(Long id, ReservationPassengerDTO reservationPassengerDTO) {
        ReservationPassenger reservationPassenger = reservationPassengerRepository.findById(id)
                .orElseThrow(() -> new AppException("Reserva de pasajero no encontrada con ID: " + id, "NOT_FOUND"));

        reservationPassenger.setSeatNumber(reservationPassengerDTO.getSeatNumber());
        reservationPassenger.setReservationTime(reservationPassengerDTO.getReservationTime());

        // Asociar cambios en la reserva y pasajero si es necesario
        if (!reservationPassenger.getReservation().getId().equals(reservationPassengerDTO.getReservation().getId())) {
            Reservation newReservation = reservationRepository.findById(reservationPassengerDTO.getReservation().getId())
                    .orElseThrow(() -> new AppException("Nueva reserva no encontrada con ID: " + reservationPassengerDTO.getReservation().getId(), "NOT_FOUND"));
            reservationPassenger.setReservation(newReservation);
        }

        if (!reservationPassenger.getPassenger().getId().equals(reservationPassengerDTO.getPassenger().getId())) {
            Passenger newPassenger = passengerRepository.findById(reservationPassengerDTO.getPassenger().getId())
                    .orElseThrow(() -> new AppException("Nuevo pasajero no encontrado con ID: " + reservationPassengerDTO.getPassenger().getId(), "NOT_FOUND"));
            reservationPassenger.setPassenger(newPassenger);
        }

        ReservationPassenger updatedReservationPassenger = reservationPassengerRepository.save(reservationPassenger);
        return reservationPassengerConverter.reservationPassengerToDTO(updatedReservationPassenger);
    }

    @Override
    @Transactional
    public String delete(Long id) {
        // Find the ReservationPassenger
        ReservationPassenger reservationPassenger = reservationPassengerRepository.findById(id)
                .orElseThrow(() -> new AppException("Reserva de pasajero no encontrada con ID: " + id, "NOT_FOUND"));

        // Retrieve associated reservation and flight
        Reservation reservation = reservationPassenger.getReservation();
        Flight flight = reservation.getFlight();

        // Remove ReservationPassenger from Reservation
        reservation.getReservationPassenger().remove(reservationPassenger);

        // Restore seat availability in the flight
        flight.setSeatsAvailable(flight.getSeatsAvailable() + 1);
        flightRepository.save(flight);

        // Delete ReservationPassenger record
        reservationPassengerRepository.delete(reservationPassenger);

        // If no passengers left in reservation, delete reservation
        if (reservation.getReservationPassenger().isEmpty()) {
            reservationRepository.delete(reservation);
        } else {
            reservationRepository.save(reservation); // Update reservation in DB if not deleted
        }

        return "Reserva de pasajero eliminada";
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationPassengerDTO> findByPassengerId(Long passengerId) {
        // Obtener la lista de entidades de `ReservationPassenger` desde el repositorio
        List<ReservationPassenger> reservationPassengers = reservationPassengerRepository.findByPassengerId(passengerId);

        // Convertir la lista de entidades a una lista de DTOs
        return reservationPassengers.stream()
                .map(reservationPassengerConverter::reservationPassengerToDTO)
                .collect(Collectors.toList());
    }

    private ReservationPassenger buildReservationPassengerEntity(ReservationPassengerDTO reservationPassengerDTO) {
        // Buscar la reserva por su ID
        Reservation reservation = reservationRepository.findById(reservationPassengerDTO.getReservation().getId())
                .orElseThrow(() -> new AppException("Reserva no encontrada con la ID: " + reservationPassengerDTO.getReservation().getId(), "NOT_FOUND"));

        // Buscar el pasajero por su ID
        Passenger passenger = passengerRepository.findById(reservationPassengerDTO.getPassenger().getId())
                .orElseThrow(() -> new AppException("Pasajero no encontrado con la ID: " + reservationPassengerDTO.getPassenger().getId(), "NOT_FOUND"));

        // Generar el número de asiento automáticamente
        String seatNumber = seatNumberGenerator.generateSeatNumber();

        // Retornar la entidad creada con el número de asiento generado
        return new ReservationPassenger(
                reservationPassengerDTO.getId(),
                seatNumber,  // Asignar el número de asiento generado
                reservationPassengerDTO.getReservationTime(),
                reservation,
                passenger
        );
    }
}

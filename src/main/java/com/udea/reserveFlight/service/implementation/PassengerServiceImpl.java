package com.udea.reserveFlight.service.implementation;

import com.udea.reserveFlight.persistence.entity.Flight;
import com.udea.reserveFlight.persistence.entity.Passenger;
import com.udea.reserveFlight.persistence.entity.Reservation;
import com.udea.reserveFlight.persistence.entity.ReservationPassenger;
import com.udea.reserveFlight.persistence.repository.IPassengerRepository;
import com.udea.reserveFlight.persistence.repository.IReservationPassengerRepository;
import com.udea.reserveFlight.persistence.repository.IReservationRepository;
import com.udea.reserveFlight.presentation.dto.PassengerDTO;
import com.udea.reserveFlight.presentation.dto.PassengerSummaryDTO;
import com.udea.reserveFlight.service.exception.AppException;
import com.udea.reserveFlight.service.interfaces.IPassengerService;
import com.udea.reserveFlight.service.interfaces.IReservationService;
import com.udea.reserveFlight.util.PassengerConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PassengerServiceImpl implements IPassengerService {

    private final IPassengerRepository passengerRepository;
    private final IReservationRepository reservationRepository;
    private final IReservationPassengerRepository reservationPassengerRepository;
    private final IReservationService reservationService;
    private final PassengerConverter passengerConverter;

    public PassengerServiceImpl(IPassengerRepository passengerRepository, IReservationRepository reservationRepository, IReservationPassengerRepository reservationPassengerRepository, IReservationService reservationService, PassengerConverter passengerConverter) {
        this.passengerRepository = passengerRepository;
        this.reservationRepository = reservationRepository;
        this.reservationPassengerRepository = reservationPassengerRepository;
        this.reservationService = reservationService;
        this.passengerConverter = passengerConverter;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PassengerSummaryDTO> findAll() {
        List<Passenger> passengers = passengerRepository.findAll();
        return passengers.stream()
                .map(passengerConverter::passengerToSummaryDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PassengerSummaryDTO findById(Long id) {
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new AppException("Pasajero no encontrado con ID: " + id, "NOT_FOUND"));
        return passengerConverter.passengerToSummaryDTO(passenger);
    }

    @Override
    @Transactional
    public PassengerSummaryDTO save(PassengerSummaryDTO passengerSummaryDTO) {
        validateUniqueDniAndEmail(passengerSummaryDTO.getDni(), passengerSummaryDTO.getEmail());
        Passenger passenger = passengerConverter.passengerSummaryToEntity(passengerSummaryDTO);
        Passenger savedPassenger = passengerRepository.save(passenger);
        return passengerConverter.passengerToSummaryDTO(savedPassenger);
    }

    @Override
    @Transactional
    public PassengerSummaryDTO update(Long id, PassengerSummaryDTO passengerSummaryDTO) {
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new AppException("Pasajero no encontrado con ID: " + id, "NOT_FOUND"));

        if (!passenger.getDni().equals(passengerSummaryDTO.getDni()) || !passenger.getEmail().equals(passengerSummaryDTO.getEmail())) {
            validateUniqueDniAndEmail(passengerSummaryDTO.getDni(), passengerSummaryDTO.getEmail());
        }

        passenger.setName(passengerSummaryDTO.getName());
        passenger.setLastName(passengerSummaryDTO.getLastName());
        passenger.setNationality(passengerSummaryDTO.getNationality());
        passenger.setTypeDni(passengerSummaryDTO.getTypeDni());
        passenger.setDni(passengerSummaryDTO.getDni());
        passenger.setAge(passengerSummaryDTO.getAge());
        passenger.setEmail(passengerSummaryDTO.getEmail());
        passenger.setPhone(passengerSummaryDTO.getPhone());

        Passenger updatedPassenger = passengerRepository.save(passenger);
        return passengerConverter.passengerToSummaryDTO(updatedPassenger);
    }

    @Override
    @Transactional
    public String delete(Long id) {
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new AppException("Pasajero no encontrado con ID: " + id, "NOT_FOUND"));

        // Cargar todas las relaciones ReservationPassenger asociadas a este pasajero
        List<ReservationPassenger> reservationPassengers = reservationPassengerRepository.findByPassengerId(id);

        for (ReservationPassenger reservationPassenger : reservationPassengers) {
            Reservation reservation = reservationPassenger.getReservation();
            Flight flight = reservation.getFlight();

            // Restaurar 1 asiento en el vuelo asociado
            reservationService.restoreSeatsAvailability(flight, 1);

            // Eliminar la relación ReservationPassenger
            reservationPassengerRepository.delete(reservationPassenger);

            // Refrescar la lista de pasajeros asociados a la reserva
            reservation.setReservationPassenger(
                    reservationPassengerRepository.findByReservationId(reservation.getId())
            );

            // Verificar si la reserva ya no tiene pasajeros asociados
            if (reservation.getReservationPassenger().isEmpty()) {
                // Si es el último pasajero en la reserva, eliminar la reserva completa
                reservationRepository.delete(reservation);
            }
        }

        // Finalmente, eliminar el pasajero
        passengerRepository.delete(passenger);
        return "Pasajero eliminado";
    }



    private void validateUniqueDniAndEmail(String dni, String email) {
        if (passengerRepository.existsByDni(dni)) {
            throw new AppException("Ya existe un pasajero con ese DNI", "DUPLICATE_DNI");
        }
        if (passengerRepository.existsByEmail(email)) {
            throw new AppException("Ya existe un pasajero con ese email", "DUPLICATE_EMAIL");
        }
    }

}

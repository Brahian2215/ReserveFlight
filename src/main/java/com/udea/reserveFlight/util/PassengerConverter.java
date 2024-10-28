package com.udea.reserveFlight.util;

import com.udea.reserveFlight.graphql.PassengerInput;
import com.udea.reserveFlight.persistence.entity.Passenger;
import com.udea.reserveFlight.presentation.dto.PassengerDTO;
import com.udea.reserveFlight.presentation.dto.PassengerSummaryDTO;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class PassengerConverter {

    private final ReservationPassengerConverter reservationPassengerConverter;

    public PassengerConverter(ReservationPassengerConverter reservationPassengerConverter) {
        this.reservationPassengerConverter = reservationPassengerConverter;
    }

    // Convertir Passenger a DTO
    public PassengerDTO passengerToDto(Passenger passenger) {
        return new PassengerDTO(
                passenger.getId(),
                passenger.getName(),
                passenger.getLastName(),
                passenger.getNationality(),
                passenger.getTypeDni(),
                passenger.getDni(),
                passenger.getAge(),
                passenger.getEmail(),
                passenger.getPhone(),
                passenger.getReservationPassenger() != null ?
                        passenger.getReservationPassenger().stream()
                                .map(reservationPassengerConverter::reservationPassengerToSummaryDTO)
                                .collect(Collectors.toList()) : Collections.emptyList()
        );
    }

    public PassengerDTO passengerInputToDTO(PassengerInput passengerInput) {
        return new PassengerDTO(
                null,
                passengerInput.getName(),
                passengerInput.getLastName(),
                passengerInput.getNationality(),
                passengerInput.getTypeDni(),
                passengerInput.getDni(),
                passengerInput.getAge(),
                passengerInput.getEmail(),
                passengerInput.getPhone(),
                null
        );
    }

    public PassengerSummaryDTO passengerInputToSummaryDTO(PassengerInput passengerInput) {
        return new PassengerSummaryDTO(
                null,
                passengerInput.getName(),
                passengerInput.getLastName(),
                passengerInput.getNationality(),
                passengerInput.getTypeDni(),
                passengerInput.getDni(),
                passengerInput.getAge(),
                passengerInput.getEmail(),
                passengerInput.getPhone()
        );
    }

    // Convertir PassengerDTO a PassengerSummaryDTO
    public PassengerSummaryDTO passengerDtoToSummaryDTO(PassengerDTO passengerDTO) {
        return new PassengerSummaryDTO(
                passengerDTO.getId(),
                passengerDTO.getName(),
                passengerDTO.getLastName(),
                passengerDTO.getNationality(),
                passengerDTO.getTypeDni(),
                passengerDTO.getDni(),
                passengerDTO.getAge(),
                passengerDTO.getEmail(),
                passengerDTO.getPhone()
        );
    }

    // Convertir Passenger a PassengerSummaryDTO
    public PassengerSummaryDTO passengerToSummaryDTO(Passenger passenger) {
        return new PassengerSummaryDTO(
                passenger.getId(),
                passenger.getName(),
                passenger.getLastName(),
                passenger.getNationality(),
                passenger.getTypeDni(),
                passenger.getDni(),
                passenger.getAge(),
                passenger.getEmail(),
                passenger.getPhone()
        );
    }

    public Passenger passengerSummaryToEntity(PassengerSummaryDTO passengerSummaryDTO) {
        return new Passenger(
                passengerSummaryDTO.getId(),
                passengerSummaryDTO.getName(),
                passengerSummaryDTO.getLastName(),
                passengerSummaryDTO.getNationality(),
                passengerSummaryDTO.getTypeDni(),
                passengerSummaryDTO.getDni(),
                passengerSummaryDTO.getAge(),
                passengerSummaryDTO.getEmail(),
                passengerSummaryDTO.getPhone(),
                null  // Lista de ReservationPassenger asignada después
        );
    }

    // Convertir de DTO a Entidad
    public Passenger passengerToEntity(PassengerDTO passengerDTO) {

        return new Passenger(
                passengerDTO.getId(),
                passengerDTO.getName(),
                passengerDTO.getLastName(),
                passengerDTO.getNationality(),
                passengerDTO.getTypeDni(),
                passengerDTO.getDni(),
                passengerDTO.getAge(),
                passengerDTO.getEmail(),
                passengerDTO.getPhone(),
                null
        );
    }

    // Convertir ID de pasajero a entidad
    public Passenger convertIdToEntity(Long passengerId) {
        Passenger passenger = new Passenger();
        passenger.setId(passengerId);
        return passenger;
    }

}

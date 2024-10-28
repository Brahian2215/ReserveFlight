package com.udea.reserveFlight.util;

import com.udea.reserveFlight.graphql.ReservationPassengerInput;
import com.udea.reserveFlight.persistence.entity.ReservationPassenger;
import com.udea.reserveFlight.presentation.dto.ReservationPassengerDTO;
import com.udea.reserveFlight.presentation.dto.ReservationPassengerSummaryDTO;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class ReservationPassengerConverter {

    private final PassengerConverter passengerConverter;
    private final ReservationConverter reservationConverter;

    public ReservationPassengerConverter(@Lazy PassengerConverter passengerConverter, @Lazy ReservationConverter reservationConverter) {
        this.passengerConverter = passengerConverter;
        this.reservationConverter = reservationConverter;
    }

    // Convertir ReservationPassengerInput a ReservationPassengerDTO
    public ReservationPassengerDTO reservationPassengerInputToDTO(ReservationPassengerInput reservationPassengerInput) {
        return new ReservationPassengerDTO(
                null,  // ID es nulo al crear
                null,  // Asignar asiento en el servicio
                reservationPassengerInput.getReservationTime(),
                null,  // Reserva se manejará en la capa de servicio
                null  // No se incluye el pasajero completo aquí, se manejará en la capa de servicio por ID
        );
    }

    // Convertir ReservationPassenger a ReservationPassengerDTO
    public ReservationPassengerDTO reservationPassengerToDTO(ReservationPassenger reservationPassenger) {
        return new ReservationPassengerDTO(
                reservationPassenger.getId(),
                reservationPassenger.getSeatNumber(),
                reservationPassenger.getReservationTime(),
                reservationConverter.reservationToDTO(reservationPassenger.getReservation()),
                passengerConverter.passengerToDto(reservationPassenger.getPassenger())
        );
    }

    // Convertir ReservationPassengerDTO a ReservationPassengerSummaryDTO
    public ReservationPassengerSummaryDTO reservationPassengerDtoToSummaryDTO(ReservationPassengerDTO reservationPassengerDTO) {
        return new ReservationPassengerSummaryDTO(
                reservationPassengerDTO.getId(),
                reservationPassengerDTO.getSeatNumber(),
                reservationPassengerDTO.getReservationTime(),
                passengerConverter.passengerDtoToSummaryDTO(reservationPassengerDTO.getPassenger())
        );
    }

    public ReservationPassenger reservationPassengerSummaryToEntity(ReservationPassengerSummaryDTO passengerSummaryDTO) {
        return new ReservationPassenger(
                null,  // El ID se generará automáticamente
                passengerSummaryDTO.getSeatNumber(),
                passengerSummaryDTO.getReservationTime(),
                null,  // La reserva será asignada después
                passengerConverter.passengerSummaryToEntity(passengerSummaryDTO.getPassenger())  // Convertir el pasajero
        );
    }

    // Convertir ReservationPassenger a ReservationPassengerSummaryDTO (simplificado)
    public ReservationPassengerSummaryDTO reservationPassengerToSummaryDTO(ReservationPassenger reservationPassenger) {
        return new ReservationPassengerSummaryDTO(
                reservationPassenger.getId(),
                reservationPassenger.getSeatNumber(),
                reservationPassenger.getReservationTime(),
                passengerConverter.passengerToSummaryDTO(reservationPassenger.getPassenger())
        );
    }

    // Convertir ReservationPassengerDTO a ReservationPassenger (entidad)
    public ReservationPassenger reservationPassengerToEntity(ReservationPassengerDTO reservationPassengerDTO) {
        return new ReservationPassenger(
                reservationPassengerDTO.getId(),
                null, // El seatNumber se asignará en el servicio
                reservationPassengerDTO.getReservationTime(),
                reservationConverter.reservationToEntity(reservationPassengerDTO.getReservation()),
                passengerConverter.passengerToEntity(reservationPassengerDTO.getPassenger())
        );
    }
}

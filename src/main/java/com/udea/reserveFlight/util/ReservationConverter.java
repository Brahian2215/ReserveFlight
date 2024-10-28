package com.udea.reserveFlight.util;

import com.udea.reserveFlight.graphql.ReservationInput;
import com.udea.reserveFlight.persistence.entity.Reservation;
import com.udea.reserveFlight.presentation.dto.FlightSummaryDTO;
import com.udea.reserveFlight.presentation.dto.ReservationDTO;
import com.udea.reserveFlight.presentation.dto.ReservationSummaryDTO;
import com.udea.reserveFlight.service.interfaces.IFlightService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class ReservationConverter {

    private final IFlightService flightService;
    private final FlightConverter flightConverter;
    private final ReservationPassengerConverter reservationPassengerConverter;

    public ReservationConverter(IFlightService flightService, @Lazy FlightConverter flightConverter, @Lazy ReservationPassengerConverter reservationPassengerConverter) {
        this.flightService = flightService;
        this.flightConverter = flightConverter;
        this.reservationPassengerConverter = reservationPassengerConverter;
    }

    // Conversión de ReservationInput a ReservationDTO (para usar en el controlador y servicio)
    public ReservationDTO reservationInputToDTO(ReservationInput reservationInput) {
        FlightSummaryDTO flightSummaryDTO = flightService.getFlightSummaryById(reservationInput.getFlightId());

        return new ReservationDTO(
                null,
                null, // Código de reserva se generará en el servicio
                flightSummaryDTO, // Obtener DTO del vuelo
                Collections.emptyList() // Inicializa con lista vacía para evitar null
        );
    }

    // Conversión de Reservation a ReservationDTO
    public ReservationDTO reservationToDTO(Reservation reservation) {
        return new ReservationDTO(
                reservation.getId(),
                reservation.getReservationCode(),
                flightConverter.flightToSummaryDTO(reservation.getFlight()),  // Convierte el vuelo a DTO
                reservation.getReservationPassenger() != null ?  // Manejar si es null
                        reservation.getReservationPassenger().stream()
                                .map(this.reservationPassengerConverter::reservationPassengerToSummaryDTO)
                                .collect(Collectors.toList()) :
                        Collections.emptyList()
        );// Devuelve lista vacía si no hay pasajeros
    }

    // Convertir Reservation a ReservationSummaryDTO (simplificado)
    public ReservationSummaryDTO reservationToSummaryDTO(ReservationDTO reservationDTO) {
        return new ReservationSummaryDTO(
                reservationDTO.getId(),
                reservationDTO.getReservationCode(),
                reservationDTO.getFlight()
        );
    }

    public ReservationSummaryDTO reservationEntityToSummaryDTO(Reservation reservation) {
        return new ReservationSummaryDTO(
                reservation.getId(),
                reservation.getReservationCode(),
                flightConverter.flightToSummaryDTO(reservation.getFlight())
        );
    }

    // Conversión de ReservationDTO a Entidad
    public Reservation reservationToEntity(ReservationDTO reservationDTO) {
        return new Reservation(
                reservationDTO.getId(),
                reservationDTO.getReservationCode(),
                flightConverter.flightSummaryToEntity(reservationDTO.getFlight()),  // Convierte el vuelo a entidad
                reservationDTO.getReservationPassenger() != null ?  // Manejar si es null
                        reservationDTO.getReservationPassenger().stream()
                                .map(reservationPassengerConverter::reservationPassengerSummaryToEntity)
                                .collect(Collectors.toList()) :
                        Collections.emptyList()
        );
    }

}

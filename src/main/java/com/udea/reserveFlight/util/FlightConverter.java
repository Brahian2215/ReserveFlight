package com.udea.reserveFlight.util;

import com.udea.reserveFlight.graphql.FlightInput;
import com.udea.reserveFlight.persistence.entity.Flight;
import com.udea.reserveFlight.presentation.dto.FlightDTO;
import com.udea.reserveFlight.presentation.dto.FlightSummaryDTO;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class FlightConverter {

    private final ReservationConverter reservationConverter;

    public FlightConverter(@Lazy ReservationConverter reservationConverter) {
        this.reservationConverter = reservationConverter;
    }

    /**
     * Convierte un objeto Flight en un DTO completo FlightDTO, incluyendo sus reservas.
     */
    public FlightDTO flightToDTO(Flight flight) {
        return new FlightDTO(
                flight.getId(),
                flight.getFlightNumber(),
                flight.getOrigin(),
                flight.getDestination(),
                flight.getSeatsAvailable(),
                flight.getDepartureTime(),
                flight.getArrivalTime(),
                flight.getReservations() != null
                        ? flight.getReservations().stream()
                        .map(this.reservationConverter::reservationEntityToSummaryDTO)
                        .collect(Collectors.toList()) : Collections.emptyList()
        );
    }

    /**
     * Convierte un FlightSummaryDTO en una entidad Flight, sin las reservas.
     */
    public Flight flightSummaryToEntity(FlightSummaryDTO flightSummaryDTO) {
        return new Flight(
                flightSummaryDTO.getId(),
                flightSummaryDTO.getFlightNumber(),
                flightSummaryDTO.getOrigin(),
                flightSummaryDTO.getDestination(),
                flightSummaryDTO.getSeatsAvailable(),
                flightSummaryDTO.getDepartureTime(),
                flightSummaryDTO.getArrivalTime(),
                null
        );
    }

    /**
     * Convierte un objeto Flight en un FlightSummaryDTO, una versión simplificada del DTO.
     */
    public FlightSummaryDTO flightToSummaryDTO(Flight flight) {
        return new FlightSummaryDTO(
                flight.getId(),
                flight.getFlightNumber(),
                flight.getOrigin(),
                flight.getDestination(),
                flight.getSeatsAvailable(),
                flight.getDepartureTime(),
                flight.getArrivalTime()
        );
    }

    /**
     * Convierte un FlightInput en un FlightDTO, utilizado al crear un nuevo vuelo.
     */
    public FlightDTO flightInputToDTO(FlightInput flightInput) {
        return new FlightDTO(
                null,  // El id es nulo al crear un nuevo vuelo
                flightInput.getFlightNumber(),
                flightInput.getOrigin(),
                flightInput.getDestination(),
                flightInput.getSeatsAvailable(),
                flightInput.getDepartureTime(),
                flightInput.getArrivalTime(),
                null // Reservas no son parte de la entrada
        );
    }

    public FlightSummaryDTO flightInputToSummaryDTO (FlightInput flightInput){
        return new FlightSummaryDTO(
                null,
                flightInput.getFlightNumber(),
                flightInput.getOrigin(),
                flightInput.getDestination(),
                flightInput.getSeatsAvailable(),
                flightInput.getDepartureTime(),
                flightInput.getArrivalTime()
        );
    }

    /**
     * Convierte un FlightDTO en una entidad Flight, sin las reservas.
     */
    public Flight flightToEntity(FlightDTO flightDTO) {
        return new Flight(
                flightDTO.getId(),
                flightDTO.getFlightNumber(),
                flightDTO.getOrigin(),
                flightDTO.getDestination(),
                flightDTO.getSeatsAvailable(),
                flightDTO.getDepartureTime(),
                flightDTO.getArrivalTime(),
                null
        );
    }

}

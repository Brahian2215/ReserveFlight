package com.udea.reserveFlight.presentation.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {

    private Long id;

    @Column(unique = true)
    private String reservationCode;

    private FlightSummaryDTO flight;
    private List<ReservationPassengerSummaryDTO> reservationPassenger;
}

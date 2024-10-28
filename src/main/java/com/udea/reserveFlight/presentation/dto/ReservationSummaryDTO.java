package com.udea.reserveFlight.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationSummaryDTO {
    private Long id;
    private String reservationCode;
    private FlightSummaryDTO flight; // Relación simplificada para evitar ciclos
}

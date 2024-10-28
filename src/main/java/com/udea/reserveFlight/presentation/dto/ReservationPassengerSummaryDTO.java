package com.udea.reserveFlight.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationPassengerSummaryDTO {
    private Long id;
    private String seatNumber;
    private LocalDateTime reservationTime;
    private PassengerSummaryDTO passenger; // Relación simplificada para evitar ciclos
}

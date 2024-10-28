package com.udea.reserveFlight.graphql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationPassengerInput {
    private LocalDateTime reservationTime;
    private Long reservationId;
    private Long passengerId;
}

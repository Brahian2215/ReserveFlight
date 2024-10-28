package com.udea.reserveFlight.graphql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightInput {
    private String flightNumber;
    private String origin;
    private String destination;
    private int seatsAvailable;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
}

package com.udea.reserveFlight.graphql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationInput {
    private Long flightId;
    private List<Long> passengerIds;
}

package com.udea.reserveFlight.presentation.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationPassengerDTO {

    private Long id;

    @NotNull(message = "Ingrese un número de silla válido")
    @Pattern(regexp = "^[A-Z0-9]{2,5}$", message = "El número de silla solo puede contener letras mayúsculas y números")
    private String seatNumber;

    @NotNull(message = "Reserva adecuadamente")
    private LocalDateTime reservationTime;

    private ReservationDTO reservation;
    private PassengerDTO passenger;

}

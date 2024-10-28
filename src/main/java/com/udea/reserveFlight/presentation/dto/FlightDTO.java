package com.udea.reserveFlight.presentation.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightDTO {

    private Long id;

    @NotNull(message = "Ingrese un número de vuelo válido")
    @Pattern(regexp = "^[A-Z0-9]{5,15}$", message = "El número de vuelo solo puede contener letras mayúsculas y números")
    private String flightNumber;

    @NotNull(message = "Ingrese un origen de vuelo válido")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñÜü'\\-\\s]{3,20}$", message = "El origen solo puede contener letras y espacios")
    private String origin;

    @NotNull(message = "Ingrese un destino de vuelo válido")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñÜü'\\-\\s]{3,20}$", message = "El destino solo puede contener letras y espacios")
    private String destination;

    @NotNull(message = "Ingrese un número de sillas válido")
    @Min(value = 0, message = "El número de asientos disponibles no puede ser negativo")
    private int seatsAvailable;

    @NotNull(message = "Ingrese una hora de salida válida")
    private LocalDateTime departureTime;

    @NotNull(message = "Ingrese una hora de llegada válida")
    private LocalDateTime arrivalTime;

    private List<ReservationSummaryDTO> reservations;
}

package com.udea.reserveFlight.presentation.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerDTO {

    private Long id;

    @NotNull(message = "Ingresa un nombre válido")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñÜü'\\-\\s]{3,20}$", message = "Ingresa un nombre válido")
    private String name;

    @NotNull(message = "Ingresa un apellido válido")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñÜü'\\-\\s]{3,20}$", message = "Ingresa un apellido válido")
    private String lastName;

    @NotNull(message = "Ingresa una nacionalidad válida")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñÜü'\\-\\s]{3,20}$", message = "Ingresa una nacionalidad válida")
    private String nationality;

    @NotNull(message = "Ingresa un tipo de DNI válido")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñÜü]{3,20}$", message = "Ingresa un tipo de DNI válido")
    private String typeDni;

    @NotNull(message = "Ingresa un DNI válido")
    @Pattern(regexp = "^[0-9]{5,10}$", message = "Ingresa un DNI válido")
    private String dni;

    @NotNull(message = "Ingresa una edad válida")
    @Min(value = 10, message = "La edad debe ser mayor o igual a 10")
    @Max(value = 100, message = "La edad debe ser menor o igual a 100")
    private int age;

    @NotNull(message = "Ingresa un email válido")
    @Email(message = "Email no válido")
    private String email;

    @NotNull(message = "Ingresa un telefono válido")
    @Pattern(regexp = "^[0-9]{5,10}$", message = "Ingresa un telefono válido")
    private String phone;

    private List<ReservationPassengerSummaryDTO> reservationPassenger;

}

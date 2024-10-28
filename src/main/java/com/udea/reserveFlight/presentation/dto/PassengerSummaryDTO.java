package com.udea.reserveFlight.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerSummaryDTO {
    private Long id;
    private String name;
    private String lastName;
    private String nationality;
    private String typeDni;
    private String dni;
    private int age;
    private String email;
    private String phone;
}

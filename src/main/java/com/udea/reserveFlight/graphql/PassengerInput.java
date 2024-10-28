package com.udea.reserveFlight.graphql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerInput {

    private String name;
    private String lastName;
    private String nationality;
    private String typeDni;
    private String dni;
    private int age;
    private String email;
    private String phone;
}

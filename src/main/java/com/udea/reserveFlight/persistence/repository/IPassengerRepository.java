package com.udea.reserveFlight.persistence.repository;

import com.udea.reserveFlight.persistence.entity.Passenger;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPassengerRepository extends JpaRepository<Passenger, Long> {
    boolean existsByDni(String dni);
    boolean existsByEmail(String email);
}

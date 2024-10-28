package com.udea.reserveFlight.persistence.repository;

import com.udea.reserveFlight.persistence.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IFlightRepository extends JpaRepository<Flight, Long> {
    boolean existsByFlightNumber(String flightNumber);
}

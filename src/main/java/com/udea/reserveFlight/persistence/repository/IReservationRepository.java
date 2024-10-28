package com.udea.reserveFlight.persistence.repository;

import com.udea.reserveFlight.persistence.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IReservationRepository extends JpaRepository<Reservation, Long> {
}

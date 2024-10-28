package com.udea.reserveFlight.persistence.repository;

import com.udea.reserveFlight.persistence.entity.ReservationPassenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IReservationPassengerRepository extends JpaRepository<ReservationPassenger, Long> {
    List<ReservationPassenger> findByPassengerId(Long passengerId);
    List<ReservationPassenger> findByReservationId(Long reservationId);
}

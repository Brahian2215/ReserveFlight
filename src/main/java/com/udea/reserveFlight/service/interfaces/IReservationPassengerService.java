package com.udea.reserveFlight.service.interfaces;

import com.udea.reserveFlight.persistence.entity.ReservationPassenger;
import com.udea.reserveFlight.presentation.dto.ReservationPassengerDTO;

import java.util.List;

public interface IReservationPassengerService {

    List<ReservationPassengerDTO> findAll();
    ReservationPassengerDTO findById(Long id);
    ReservationPassengerDTO save(ReservationPassengerDTO reservationPassengerDTO);
    ReservationPassengerDTO update(Long id, ReservationPassengerDTO reservationPassengerDTO);
    String delete(Long id);
    List<ReservationPassengerDTO> findByPassengerId(Long passengerId);
}

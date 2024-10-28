package com.udea.reserveFlight.service.interfaces;

import com.udea.reserveFlight.persistence.entity.Flight;
import com.udea.reserveFlight.presentation.dto.ReservationDTO;
import com.udea.reserveFlight.presentation.dto.ReservationSummaryDTO;

import java.util.List;

public interface IReservationService {
    List<ReservationSummaryDTO> findAll();
    ReservationDTO findById(Long id);
    ReservationDTO save(ReservationDTO reservationDTO, List<Long> passengerIds);
    ReservationDTO update(Long id, ReservationDTO reservationDTO, List<Long> passengerIds);
    String delete(Long id);
    void restoreSeatsAvailability(Flight flight, int seatsToRestore);
}

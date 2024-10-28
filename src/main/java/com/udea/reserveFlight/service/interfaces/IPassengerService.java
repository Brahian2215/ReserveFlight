package com.udea.reserveFlight.service.interfaces;

import com.udea.reserveFlight.presentation.dto.PassengerDTO;
import com.udea.reserveFlight.presentation.dto.PassengerSummaryDTO;

import java.util.List;

public interface IPassengerService {

    List<PassengerSummaryDTO> findAll();
    PassengerSummaryDTO findById(Long id);
    PassengerSummaryDTO save(PassengerSummaryDTO passengerSummaryDTO);
    PassengerSummaryDTO update(Long id, PassengerSummaryDTO passengerSummaryDTO);
    String delete(Long id);
}

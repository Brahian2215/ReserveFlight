package com.udea.reserveFlight.service.interfaces;

import com.udea.reserveFlight.presentation.dto.FlightDTO;
import com.udea.reserveFlight.presentation.dto.FlightSummaryDTO;

import java.util.List;

public interface IFlightService {
    List<FlightSummaryDTO> findAll();
    FlightSummaryDTO findById(Long id);
    FlightSummaryDTO save(FlightSummaryDTO flightSummaryDTO);
    FlightSummaryDTO update(Long id, FlightSummaryDTO flightSummaryDTO);
    String delete(Long id);
    FlightSummaryDTO getFlightSummaryById(Long flightId);
}

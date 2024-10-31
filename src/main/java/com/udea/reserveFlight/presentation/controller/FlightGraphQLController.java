package com.udea.reserveFlight.presentation.controller;

import com.udea.reserveFlight.graphql.FlightInput;
import com.udea.reserveFlight.presentation.dto.FlightDTO;
import com.udea.reserveFlight.presentation.dto.FlightSummaryDTO;
import com.udea.reserveFlight.service.exception.AppException;
import com.udea.reserveFlight.service.interfaces.IFlightService;
import com.udea.reserveFlight.util.FlightConverter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@Validated
public class FlightGraphQLController {

    @Autowired
    private IFlightService flightService;

    @Autowired
    private FlightConverter flightConverter;

    // Find All
    @QueryMapping(name = "allFlights")
    public List<FlightSummaryDTO> findAll(){
        return flightService.findAll();
    }

    // Find By ID
    @QueryMapping(name = "flightById")
    public FlightSummaryDTO findById(@Argument Long id){
        if (id == null) throw new AppException("El ID no puede ser nulo", "INVALID_ARGUMENT");
        return flightService.findById(id);
    }

    // Save
    @MutationMapping(name = "saveFlight")
    public FlightSummaryDTO save(@Argument @Valid FlightInput flightInput) {
        if (flightInput == null) {
            throw new AppException("No se ha recibido información para guardar el vuelo", "BAD_REQUEST");
        }
        validateFlightTimes(flightInput.getDepartureTime(), flightInput.getArrivalTime());
        FlightSummaryDTO flightSummaryDTO = flightConverter.flightInputToSummaryDTO(flightInput);
        return flightService.save(flightSummaryDTO);
    }

    // Update
    @MutationMapping(name = "updateFlight")
    public FlightSummaryDTO update( @Argument Long id, @Argument @Valid FlightInput flightInput){
        if (id == null) {
            throw new AppException("El ID del vuelo no puede ser nulo", "INVALID_ARGUMENT");
        }
        validateFlightTimes(flightInput.getDepartureTime(), flightInput.getArrivalTime());
        FlightSummaryDTO flightSummaryDTO = flightConverter.flightInputToSummaryDTO(flightInput);
        return flightService.update(id, flightSummaryDTO);
    }

    // Delete
    @MutationMapping(name = "deleteFlight")
    public String delete(@Argument Long id){
        if (id == null) {
            throw new AppException("El ID del vuelo no puede ser nulo", "INVALID_ARGUMENT");
        }
        flightService.delete(id);
        return "Vuelo eliminado con éxito";
    }

    public void validateFlightTimes(LocalDateTime departure, LocalDateTime arrival) {
        if (departure == null || arrival == null) {
            throw new AppException("Las horas de salida y llegada no pueden ser nulas", "NULL_TIME");
        }
        if (departure.isAfter(arrival)) {
            throw new AppException("La hora de salida debe ser antes que la hora de llegada", "INVALID_TIME_RANGE");
        }
    }

}

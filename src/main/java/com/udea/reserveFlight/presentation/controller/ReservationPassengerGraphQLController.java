package com.udea.reserveFlight.presentation.controller;

import com.udea.reserveFlight.graphql.ReservationPassengerInput;
import com.udea.reserveFlight.presentation.dto.ReservationPassengerDTO;
import com.udea.reserveFlight.presentation.dto.ReservationPassengerSummaryDTO;
import com.udea.reserveFlight.service.interfaces.IReservationPassengerService;
import com.udea.reserveFlight.util.ReservationPassengerConverter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@Validated
public class ReservationPassengerGraphQLController {

    private final IReservationPassengerService reservationPassengerService;
    private final ReservationPassengerConverter reservationPassengerConverter;

    public ReservationPassengerGraphQLController(IReservationPassengerService reservationPassengerService, ReservationPassengerConverter reservationPassengerConverter) {
        this.reservationPassengerService = reservationPassengerService;
        this.reservationPassengerConverter = reservationPassengerConverter;
    }

    // findAll
    @QueryMapping(name = "allReservationPassengers")
    public List<ReservationPassengerDTO> findAll(){
        return reservationPassengerService.findAll();

    }

    // findById
    @QueryMapping(name = "reservationPassengerById")
    public ReservationPassengerDTO findById(@Argument Long id) {
        return reservationPassengerService.findById(id);
    }

    // save
    @MutationMapping(name = "saveReservationPassenger")
    public ReservationPassengerDTO save(@Argument @Valid ReservationPassengerInput reservationPassengerInput) {
        ReservationPassengerDTO reservationPassengerDTO = reservationPassengerConverter.reservationPassengerInputToDTO(reservationPassengerInput);
        return reservationPassengerService.save(reservationPassengerDTO);
    }

    // update
    @MutationMapping(name = "updateReservationPassenger")
    public ReservationPassengerDTO update(@Argument Long id, @Argument @Valid ReservationPassengerInput reservationPassengerInput) {
        ReservationPassengerDTO reservationPassengerDTO = reservationPassengerConverter.reservationPassengerInputToDTO(reservationPassengerInput);
        return reservationPassengerService.update(id, reservationPassengerDTO);
    }

    // delete
    @MutationMapping(name = "deleteReservationPassenger")
    public String delete(@Argument Long id) {
        return reservationPassengerService.delete(id);
    }


}

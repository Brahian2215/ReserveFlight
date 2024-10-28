package com.udea.reserveFlight.presentation.controller;

import com.udea.reserveFlight.graphql.ReservationInput;
import com.udea.reserveFlight.presentation.dto.ReservationDTO;
import com.udea.reserveFlight.presentation.dto.ReservationSummaryDTO;
import com.udea.reserveFlight.service.exception.AppException;
import com.udea.reserveFlight.service.interfaces.IReservationService;
import com.udea.reserveFlight.util.ReservationConverter;
import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Controller
@Validated
public class ReservationGraphQLController {

    private final IReservationService reservationService;
    private final ReservationConverter reservationConverter;

    public ReservationGraphQLController(IReservationService reservationService, ReservationConverter reservationConverter) {
        this.reservationService = reservationService;
        this.reservationConverter = reservationConverter;
    }

    // findAll
    @QueryMapping(name = "allReservations")
    public List<ReservationSummaryDTO> findAll(){
        return reservationService.findAll();
    }

    // findById
    @QueryMapping(name = "reservationById")
    public ReservationDTO findById(@Argument Long id){
        return reservationService.findById(id);
    }

    // save
    @MutationMapping(name = "saveReservation")
    public ReservationDTO save(@Argument @Valid ReservationInput reservationInput) {
        return reservationService.save(reservationConverter.reservationInputToDTO(reservationInput), reservationInput.getPassengerIds());
    }

    // update
    @MutationMapping(name = "updateReservation")
    public ReservationDTO update(@Argument Long id, @Argument @Valid ReservationInput reservationInput){
        return reservationService.update(id, reservationConverter.reservationInputToDTO(reservationInput), reservationInput.getPassengerIds());
    }

    // delete
    @MutationMapping(name = "deleteReservation")
    public String delete(@Argument Long id){
        if (id == null) {
            throw new AppException("El ID de la reserva no puede ser nulo", "INVALID_ARGUMENT");
        }
        return reservationService.delete(id);
    }
}

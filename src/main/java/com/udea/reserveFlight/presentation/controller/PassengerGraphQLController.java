package com.udea.reserveFlight.presentation.controller;

import com.udea.reserveFlight.graphql.PassengerInput;
import com.udea.reserveFlight.presentation.dto.PassengerDTO;
import com.udea.reserveFlight.presentation.dto.PassengerSummaryDTO;
import com.udea.reserveFlight.service.interfaces.IPassengerService;
import com.udea.reserveFlight.util.PassengerConverter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Controller
@Validated
public class PassengerGraphQLController {

    @Autowired
    private IPassengerService passengerService;

    @Autowired
    private PassengerConverter passengerConverter;

    // findAll
    @QueryMapping(name = "allPassengers")
    public List<PassengerSummaryDTO> findAll(){
        return passengerService.findAll();
    }

    // findById
    @QueryMapping(name = "passengerById")
    public PassengerSummaryDTO findById(@Argument Long id){
        return passengerService.findById(id);
    }

    // save
    @MutationMapping(name = "savePassenger")
    public PassengerSummaryDTO save(@Argument @Valid PassengerInput passengerInput) {
        PassengerSummaryDTO passengerSummaryDTO = passengerConverter.passengerInputToSummaryDTO(passengerInput);
        return passengerService.save(passengerSummaryDTO);
    }

    // update
    @MutationMapping(name = "updatePassenger")
    public PassengerSummaryDTO update(@Argument Long id, @Argument @Valid PassengerInput passengerInput){
        PassengerSummaryDTO passengerSummaryDTO = passengerConverter.passengerInputToSummaryDTO(passengerInput);
        return passengerService.update(id, passengerSummaryDTO);
    }

    // delete
    @MutationMapping(name = "deletePassenger")
    public String delete(@Argument Long id){
        return passengerService.delete(id);
    }

}

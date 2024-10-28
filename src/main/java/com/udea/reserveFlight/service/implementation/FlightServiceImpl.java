package com.udea.reserveFlight.service.implementation;

import com.udea.reserveFlight.persistence.entity.Flight;
import com.udea.reserveFlight.persistence.repository.IFlightRepository;
import com.udea.reserveFlight.presentation.dto.FlightDTO;
import com.udea.reserveFlight.presentation.dto.FlightSummaryDTO;
import com.udea.reserveFlight.service.exception.AppException;
import com.udea.reserveFlight.service.interfaces.IFlightService;
import com.udea.reserveFlight.util.FlightConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightServiceImpl implements IFlightService {

    private final IFlightRepository flightRepository;
    private final FlightConverter flightConverter;

    public FlightServiceImpl(IFlightRepository flightRepository, FlightConverter flightConverter) {
        this.flightRepository = flightRepository;
        this.flightConverter = flightConverter;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlightSummaryDTO> findAll() {
        return flightRepository.findAll().stream()
                .map(flightConverter::flightToSummaryDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public FlightSummaryDTO findById(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new AppException("Vuelo no encontrado con la ID: " + id, "NOT_FOUND"));
        return flightConverter.flightToSummaryDTO(flight);
    }

    /**
     * Método para obtener un FlightSummaryDTO basado en el ID del vuelo.
     * @param flightId ID del vuelo a buscar.
     * @return FlightSummaryDTO con los datos resumidos del vuelo.
     */
    @Transactional(readOnly = true)
    public FlightSummaryDTO getFlightSummaryById(Long flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new AppException("Vuelo no encontrado con ID: " + flightId, "NOT_FOUND"));
        return flightConverter.flightToSummaryDTO(flight);
    }

    @Override
    @Transactional
    public FlightSummaryDTO save(FlightSummaryDTO flightSummaryDTO) {
        // Verificar duplicidad de número de vuelo
        if (flightRepository.existsByFlightNumber(flightSummaryDTO.getFlightNumber())) {
            throw new AppException("Ya existe un vuelo con ese número", "DUPLICATE_FLIGHT_NUMBER");
        }
        Flight flight = flightConverter.flightSummaryToEntity(flightSummaryDTO);
        Flight savedFlight = flightRepository.save(flight);
        return flightConverter.flightToSummaryDTO(savedFlight);
    }

    @Override
    @Transactional
    public FlightSummaryDTO update(Long id, FlightSummaryDTO flightSummaryDTO) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new AppException("Vuelo no encontrado con la ID: " + id, "NOT_FOUND"));

        // Verificar si el vuelo tiene reservas asociadas
        boolean hasReservations = flight.getReservations() != null && !flight.getReservations().isEmpty();

        if (hasReservations) {
            // Si el vuelo tiene reservas, solo permitimos modificar el número de vuelo
            if (!flight.getFlightNumber().equals(flightSummaryDTO.getFlightNumber())) {
                flight.setFlightNumber(flightSummaryDTO.getFlightNumber());
            }
            // Si se intenta modificar otros campos, lanzamos una excepción
            if (!flight.getOrigin().equals(flightSummaryDTO.getOrigin()) ||
                    !flight.getDestination().equals(flightSummaryDTO.getDestination()) ||
                    flight.getSeatsAvailable() != flightSummaryDTO.getSeatsAvailable() ||
                    !flight.getDepartureTime().equals(flightSummaryDTO.getDepartureTime()) ||
                    !flight.getArrivalTime().equals(flightSummaryDTO.getArrivalTime())) {
                throw new AppException("No se puede modificar otros datos del vuelo si tiene reservas asociadas", "INVALID_OPERATION");
            }
        } else {
            // Si no tiene reservas, permitimos modificar todos los campos excepto el número de vuelo
            if (!flight.getFlightNumber().equals(flightSummaryDTO.getFlightNumber())) {
                throw new AppException("No se puede modificar el número de vuelo porque no tiene reservas asociadas", "INVALID_OPERATION");
            }

            // Actualizamos los demás campos
            flight.setOrigin(flightSummaryDTO.getOrigin());
            flight.setDestination(flightSummaryDTO.getDestination());
            flight.setSeatsAvailable(flightSummaryDTO.getSeatsAvailable());
            flight.setDepartureTime(flightSummaryDTO.getDepartureTime());
            flight.setArrivalTime(flightSummaryDTO.getArrivalTime());
        }

        // Guardamos los cambios en la base de datos
        Flight updatedFlight = flightRepository.save(flight);
        return flightConverter.flightToSummaryDTO(updatedFlight);
    }

    @Override
    @Transactional
    public String delete(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new AppException("Vuelo no encontrado con la ID: " + id, "NOT_FOUND"));

        // Verificar si el vuelo tiene reservas asociadas
        if (flight.getReservations() != null && !flight.getReservations().isEmpty()) {
            throw new AppException("No se puede eliminar el vuelo porque tiene reservas asociadas", "RESERVATIONS_ASSOCIATED");
            // Alternativamente, pudieramos eliminar en cascada:
            // flight.getReservations().clear();  // Eliminar reservas manualmente si se quiere en cascada
        }

        flightRepository.delete(flight);
        return "Vuelo eliminado con éxito";
    }


    private void validateSeatsAvailability(Flight flight, int newSeatsAvailable) {
        int reservedSeats = flight.getReservations() != null ? flight.getReservations().size() : 0;
        if (newSeatsAvailable < reservedSeats) {
            throw new AppException("La cantidad de asientos no puede ser menor que las reservas actuales", "INVALID_SEAT_COUNT");
        }
    }

}

package com.udea.reserveFlight.service.implementation;

import com.udea.reserveFlight.persistence.entity.Flight;
import com.udea.reserveFlight.persistence.entity.Reservation;
import com.udea.reserveFlight.persistence.repository.IFlightRepository;
import com.udea.reserveFlight.presentation.dto.FlightSummaryDTO;
import com.udea.reserveFlight.service.exception.AppException;
import com.udea.reserveFlight.util.FlightConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceImplTest {

    @InjectMocks
    private FlightServiceImpl flightService;

    @Mock
    private IFlightRepository flightRepository;

    @Mock
    private FlightConverter flightConverter;

    private Flight flight;
    private FlightSummaryDTO flightSummaryDTO;
    private Flight newFlight;
    private FlightSummaryDTO updatedFlight;

    @BeforeEach
    void setUp() {
        // Configuración de datos de prueba
        flight = new Flight();
        flight.setFlightNumber("ATN240");
        flight.setOrigin("Bucaramanga");
        flight.setDestination("Arauca");
        flight.setSeatsAvailable(150);
        flight.setDepartureTime(LocalDateTime.parse("2023-10-27T07:00:00"));
        flight.setArrivalTime(LocalDateTime.parse("2023-10-27T07:30:00"));
        flight.setReservations(List.of(new Reservation())); // Mocked reservation

        flightSummaryDTO = new FlightSummaryDTO();
        flightSummaryDTO.setId(1L);
        flightSummaryDTO.setFlightNumber("ATN240");
        flightSummaryDTO.setOrigin("Bucaramanga");
        flightSummaryDTO.setDestination("Arauca");
        flightSummaryDTO.setSeatsAvailable(150);
        flightSummaryDTO.setDepartureTime(flight.getDepartureTime());
        flightSummaryDTO.setArrivalTime(flight.getArrivalTime());

        newFlight = new Flight();
        newFlight.setFlightNumber("LAT001");
        newFlight.setOrigin("Bogotá");
        newFlight.setDestination("Medellín");
        newFlight.setSeatsAvailable(150);
        newFlight.setDepartureTime(LocalDateTime.parse("2023-10-28T09:00:00"));
        newFlight.setArrivalTime(LocalDateTime.parse("2023-10-28T10:00:00"));

        updatedFlight = new FlightSummaryDTO();
        updatedFlight.setId(1L);
        updatedFlight.setFlightNumber("LAT001");
        updatedFlight.setOrigin("Bogotá");
        updatedFlight.setDestination("Medellín");
        updatedFlight.setSeatsAvailable(150);
        updatedFlight.setDepartureTime(newFlight.getDepartureTime());
        updatedFlight.setArrivalTime(newFlight.getArrivalTime());
    }

    // Save
    @Test
    void testSaveFlight() {
        when(flightConverter.flightSummaryToEntity(flightSummaryDTO)).thenReturn(flight);
        when(flightRepository.existsByFlightNumber("ATN240")).thenReturn(false);
        when(flightRepository.save(flight)).thenReturn(flight);
        when(flightConverter.flightToSummaryDTO(flight)).thenReturn(flightSummaryDTO);

        FlightSummaryDTO savedFlight = flightService.save(flightSummaryDTO);

        assertNotNull(savedFlight);
        assertEquals("ATN240", savedFlight.getFlightNumber());
        assertEquals("Bucaramanga", savedFlight.getOrigin());
        assertEquals("Arauca", savedFlight.getDestination());
        assertEquals(150, savedFlight.getSeatsAvailable());
        assertEquals(flightSummaryDTO.getDepartureTime(), savedFlight.getDepartureTime());
        assertEquals(flightSummaryDTO.getArrivalTime(), savedFlight.getArrivalTime());

        verify(flightRepository, times(1)).save(flight);
        verify(flightRepository, times(1)).existsByFlightNumber("ATN240");
        verify(flightConverter, times(1)).flightSummaryToEntity(flightSummaryDTO);
        verify(flightConverter, times(1)).flightToSummaryDTO(flight);
    }

    @Test
    void testSaveFlightDuplicateNumber() {
        when(flightRepository.existsByFlightNumber("ATN240")).thenReturn(true);
        AppException exception = assertThrows(AppException.class, () -> flightService.save(flightSummaryDTO));
        assertEquals("Ya existe un vuelo con ese número", exception.getMessage());
    }

    // Update
    @Test
    void testUpdateFlightWithReservations() {
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        flightSummaryDTO.setOrigin("NewOrigin");

        // Verificar que la actualización lanza una excepción al intentar cambiar campos distintos al número de vuelo
        AppException exception = assertThrows(AppException.class, () -> flightService.update(1L, flightSummaryDTO));
        assertEquals("No se puede modificar otros datos del vuelo si tiene reservas asociadas", exception.getMessage());
    }

    @Test
    void testUpdateFlightNotFound() {
        FlightSummaryDTO flightSummaryDTO = new FlightSummaryDTO();
        when(flightRepository.findById(1L)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> flightService.update(1L, flightSummaryDTO));
        assertEquals("Vuelo no encontrado con la ID: 1", exception.getMessage());
    }

    @Test
    void testUpdateFlight() {
        // Configurar vuelo sin reservas
        flight.setReservations(List.of());
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

        flightSummaryDTO.setFlightNumber("NEW123");  // Intento de cambiar el número de vuelo
        AppException exception = assertThrows(AppException.class, () -> flightService.update(1L, flightSummaryDTO));
        assertEquals("No se puede modificar el número de vuelo porque no tiene reservas asociadas", exception.getMessage());

        // Cambiar otros datos y verificar éxito
        flightSummaryDTO.setFlightNumber("ATN240");
        flightSummaryDTO.setOrigin("Cartagena");
        flightSummaryDTO.setDestination("Santa Marta");
        flightSummaryDTO.setSeatsAvailable(180);

        when(flightRepository.save(flight)).thenReturn(flight);
        when(flightConverter.flightToSummaryDTO(flight)).thenReturn(flightSummaryDTO);

        FlightSummaryDTO updatedFlight = flightService.update(1L, flightSummaryDTO);

        assertNotNull(updatedFlight);
        assertEquals("ATN240", updatedFlight.getFlightNumber());
        assertEquals("Cartagena", updatedFlight.getOrigin());
        assertEquals("Santa Marta", updatedFlight.getDestination());
        assertEquals(180, updatedFlight.getSeatsAvailable());
        assertEquals(flightSummaryDTO.getDepartureTime(), updatedFlight.getDepartureTime());
        assertEquals(flightSummaryDTO.getArrivalTime(), updatedFlight.getArrivalTime());

        verify(flightRepository, times(1)).save(flight);
        verify(flightConverter, times(1)).flightToSummaryDTO(flight);
        verify(flightRepository, atLeastOnce()).findById(1L);
    }

    @Test
    void testDeleteFlight() {
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        flight.setReservations(List.of());  // Sin reservas

        String result = flightService.delete(1L);
        assertEquals("Vuelo eliminado con éxito", result);
        verify(flightRepository, times(1)).delete(flight);
    }

    @Test
    void testDeleteFlightWithReservations() {
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        AppException exception = assertThrows(AppException.class, () -> flightService.delete(1L));
        assertEquals("No se puede eliminar el vuelo porque tiene reservas asociadas", exception.getMessage());
    }
}
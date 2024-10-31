package com.udea.reserveFlight.presentation.controller;

import com.udea.reserveFlight.graphql.FlightInput;
import com.udea.reserveFlight.presentation.dto.FlightSummaryDTO;
import com.udea.reserveFlight.service.exception.AppException;
import com.udea.reserveFlight.service.interfaces.IFlightService;
import com.udea.reserveFlight.util.FlightConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FlightGraphQLControllerTest {

    @Mock
    private IFlightService flightService;

    @Mock
    private FlightConverter flightConverter;

    @InjectMocks
    private FlightGraphQLController flightController;

    private FlightSummaryDTO flightSummaryDTO;
    private FlightInput flightInput;
    private FlightInput newFlightInput;
    private FlightSummaryDTO updatedFlightSummaryDTO;

    @BeforeEach
    void setUp() {
        // Configuración de datos de prueba

        flightInput = new FlightInput();
        flightInput.setFlightNumber("ATN240");
        flightInput.setOrigin("Bucaramanga");
        flightInput.setDestination("Arauca");
        flightInput.setSeatsAvailable(150);
        flightInput.setDepartureTime(LocalDateTime.parse("2023-10-27T07:00:00"));
        flightInput.setArrivalTime(LocalDateTime.parse("2023-10-27T07:30:00"));

        flightSummaryDTO = new FlightSummaryDTO();
        flightSummaryDTO.setId(1L);
        flightSummaryDTO.setFlightNumber("ATN240");
        flightSummaryDTO.setOrigin("Bucaramanga");
        flightSummaryDTO.setDestination("Arauca");
        flightSummaryDTO.setSeatsAvailable(150);
        flightSummaryDTO.setDepartureTime(flightInput.getDepartureTime());
        flightSummaryDTO.setArrivalTime(flightInput.getArrivalTime());

        newFlightInput = new FlightInput();
        newFlightInput.setFlightNumber("LAT001");
        newFlightInput.setOrigin("Bogotá");
        newFlightInput.setDestination("Medellín");
        newFlightInput.setSeatsAvailable(150);
        newFlightInput.setDepartureTime(LocalDateTime.parse("2023-10-28T09:00:00"));
        newFlightInput.setArrivalTime(LocalDateTime.parse("2023-10-28T10:00:00"));

        updatedFlightSummaryDTO = new FlightSummaryDTO();
        updatedFlightSummaryDTO.setId(1L);
        updatedFlightSummaryDTO.setFlightNumber("LAT001");
        updatedFlightSummaryDTO.setOrigin("Bogotá");
        updatedFlightSummaryDTO.setDestination("Medellín");
        updatedFlightSummaryDTO.setSeatsAvailable(150);
        updatedFlightSummaryDTO.setDepartureTime(newFlightInput.getDepartureTime());
        updatedFlightSummaryDTO.setArrivalTime(newFlightInput.getArrivalTime());
    }

    // Test para findAll()
    @Test
    void testFindAll_ShouldListFlightSummaryDTO() {

        // Configura el mock para retornar una lista con flightSummaryDTO cuando se llama a findAll()
        when(flightService.findAll()).thenReturn(Collections.singletonList(flightSummaryDTO));

        // Ejecuta el metodo y verifica el resultado
        List<FlightSummaryDTO> result = flightController.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ATN240", result.get(0).getFlightNumber());
        assertEquals("Bucaramanga", result.get(0).getOrigin());
        assertEquals("Arauca", result.get(0).getDestination());
        assertEquals(150, result.get(0).getSeatsAvailable());
        assertEquals(flightSummaryDTO.getDepartureTime(), result.get(0).getDepartureTime());
        assertEquals(flightSummaryDTO.getArrivalTime(), result.get(0).getArrivalTime());

        verify(flightService, times(1)).findAll();
    }

    @Test
    void testValidateFlightTimes_ShouldValidTimes() {
        LocalDateTime validDeparture = LocalDateTime.parse("2023-10-27T07:00:00");
        LocalDateTime validArrival = LocalDateTime.parse("2023-10-27T07:30:00");

        assertDoesNotThrow(() -> flightController.validateFlightTimes(validDeparture, validArrival));
    }

    @Test
    void testValidateFlightInvalidTimes_ShouldThrowException() {
        LocalDateTime invalidDeparture = LocalDateTime.parse("2023-10-27T08:00:00");
        LocalDateTime invalidArrival = LocalDateTime.parse("2023-10-27T07:30:00");

        AppException exception = assertThrows(AppException.class, () ->
                flightController.validateFlightTimes(invalidDeparture, invalidArrival));
        assertEquals("La hora de salida debe ser antes que la hora de llegada", exception.getMessage());
    }

    @Test
    void testValidateFlightTimes_NullDepartureOrArrival_ShouldThrowException() {
        LocalDateTime validDeparture = LocalDateTime.parse("2023-10-27T07:00:00");
        LocalDateTime validArrival = LocalDateTime.parse("2023-10-27T08:00:00");

        AppException exception1 = assertThrows(AppException.class, () ->
                flightController.validateFlightTimes(null, validArrival));
        assertEquals("Las horas de salida y llegada no pueden ser nulas", exception1.getMessage());

        AppException exception2 = assertThrows(AppException.class, () ->
                flightController.validateFlightTimes(validDeparture, null));
        assertEquals("Las horas de salida y llegada no pueden ser nulas", exception2.getMessage());
    }

    // Test para findById()
    @Test
    void testFindById_ShouldValidId() {
        when(flightService.findById(1L)).thenReturn(flightSummaryDTO);

        FlightSummaryDTO result = flightController.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("ATN240", result.getFlightNumber());
        assertEquals("Bucaramanga", result.getOrigin());
        assertEquals("Arauca", result.getDestination());
        assertEquals(150, result.getSeatsAvailable());
        assertEquals(flightSummaryDTO.getDepartureTime(), result.getDepartureTime());
        assertEquals(flightSummaryDTO.getArrivalTime(), result.getArrivalTime());

        verify(flightService, times(1)).findById(1L);
    }

    @Test
    void testFindById_NullId_ShouldThrowException() {
        AppException exception = assertThrows(AppException.class, () -> flightController.findById(null));
        assertEquals("El ID no puede ser nulo", exception.getMessage());
    }

    // Test para save()
    @Test
    void testSaveValidInput_ShouldSaveFlightSummaryDTO() {
        when(flightConverter.flightInputToSummaryDTO(flightInput)).thenReturn(flightSummaryDTO);
        when(flightService.save(flightSummaryDTO)).thenReturn(flightSummaryDTO);

        FlightSummaryDTO result = flightController.save(flightInput);

        assertNotNull(result);
        assertEquals("ATN240", result.getFlightNumber());
        assertEquals("Bucaramanga", result.getOrigin());
        assertEquals("Arauca", result.getDestination());
        assertEquals(150, result.getSeatsAvailable());
        assertEquals(flightSummaryDTO.getDepartureTime(), result.getDepartureTime());
        assertEquals(flightSummaryDTO.getArrivalTime(), result.getArrivalTime());

        verify(flightConverter, times(1)).flightInputToSummaryDTO(flightInput);
        verify(flightService, times(1)).save(flightSummaryDTO);
    }

    @Test
    void testSaveNullInput_ShouldThrowException() {
        AppException exception = assertThrows(AppException.class, () -> flightController.save(null));
        assertEquals("No se ha recibido información para guardar el vuelo", exception.getMessage());
    }

    @Test
    void testUpdateValidInput_ShouldUpdateFlightSummaryDTO() {

        when(flightConverter.flightInputToSummaryDTO(newFlightInput)).thenReturn(updatedFlightSummaryDTO);
        when(flightService.update(1L, updatedFlightSummaryDTO)).thenReturn(updatedFlightSummaryDTO);

        // Llamar al método update con el nuevo FlightInput
        FlightSummaryDTO result = flightController.update(1L, newFlightInput);

        // Verificar que el resultado tenga los valores actualizados
        assertNotNull(result);
        assertEquals("LAT001", result.getFlightNumber());
        assertEquals("Bogotá", result.getOrigin());
        assertEquals("Medellín", result.getDestination());
        assertEquals(150, result.getSeatsAvailable());
        assertEquals(newFlightInput.getDepartureTime(), result.getDepartureTime());
        assertEquals(newFlightInput.getArrivalTime(), result.getArrivalTime());

        // Verificar interacciones con los mocks
        verify(flightConverter, times(1)).flightInputToSummaryDTO(newFlightInput);
        verify(flightService, times(1)).update(1L, updatedFlightSummaryDTO);
    }

    @Test
    void testUpdate_ShouldThrowExceptionWhenIdIsNull() {
        AppException exception = assertThrows(AppException.class, () -> {
            flightController.update(null, flightInput);
        });
        assertEquals("El ID del vuelo no puede ser nulo", exception.getMessage());
    }

    // Test para el metodo delete()
    @Test
    void testDelete_ShouldDeleteFlight() {
        Long flightId = 1L;
        when(flightService.delete(flightId)).thenReturn("Vuelo eliminado con éxito");

        String result = flightController.delete(flightId);

        assertEquals("Vuelo eliminado con éxito", result);
        verify(flightService, times(1)).delete(flightId);
    }

    @Test
    void testDelete_ShouldThrowExceptionWhenIdIsNull() {
        AppException exception = assertThrows(AppException.class, () -> {
            flightController.delete(null);
        });
        assertEquals("El ID del vuelo no puede ser nulo", exception.getMessage());
    }
}
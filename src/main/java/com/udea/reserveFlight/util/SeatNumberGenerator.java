package com.udea.reserveFlight.util;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SeatNumberGenerator {
    private static final int MAX_ROWS = 30; // Número máximo de filas en el avión
    private static final char[] COLUMNS = {'A', 'B', 'C', 'D', 'E', 'F'}; // Columnas disponibles en cada fila
    private int currentRow = 1; // Fila inicial
    private int currentColumnIndex = 0; // Índice de la columna inicial

    /**
     * Genera un número de asiento en formato "1A", "1B", ... hasta el máximo especificado.
     * Avanza de manera secuencial entre filas y columnas.
     *
     * @return El próximo número de asiento en el formato deseado.
     */

    public String generateSeatNumber() {
        if (currentRow > MAX_ROWS) {
            throw new IllegalStateException("No hay más asientos disponibles.");
        }

        // Formato del asiento, ej: "12A"
        String seatNumber = currentRow + "" + COLUMNS[currentColumnIndex];

        // Actualizar columna y, si es necesario, incrementar fila
        currentColumnIndex++;
        if (currentColumnIndex >= COLUMNS.length) {
            currentColumnIndex = 0;
            currentRow++;
        }

        return seatNumber;
    }

    /**
     * Genera una lista de números de asiento para múltiples pasajeros en una reserva.
     *
     * @param numSeats Número de asientos a generar
     * @return Lista de números de asiento en el formato deseado.
     */

    public List<String> generateSeatNumbers(int numSeats) {
        List<String> seatNumbers = new ArrayList<>();
        for (int i = 0; i < numSeats; i++) {
            seatNumbers.add(generateSeatNumber());
        }
        return seatNumbers;
    }
}

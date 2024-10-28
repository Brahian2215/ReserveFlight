package com.udea.reserveFlight.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Component
public class ReservationCodeGenerator {
    private static final String PREFIX = "RSV";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final Random RANDOM = new Random();

    /**
     * Genera un código de reserva único en formato "RSV-AAA999-YYYYMMDD".
     * @return el código de reserva en el formato deseado.
     */
    public String generateReservationCode() {
        String randomLetters = generateRandomLetters(3);
        String randomNumbers = String.format("%03d", RANDOM.nextInt(1000));
        String datePart = LocalDate.now().format(DATE_FORMAT);

        return String.format("%s-%s%s-%s", PREFIX, randomLetters, randomNumbers, datePart);
    }

    /**
     * Genera letras aleatorias.
     * @param length longitud de la cadena de letras a generar.
     * @return una cadena de letras aleatorias.
     */
    private String generateRandomLetters(int length) {
        StringBuilder letters = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            letters.append((char) ('A' + RANDOM.nextInt(26)));
        }
        return letters.toString();
    }
}

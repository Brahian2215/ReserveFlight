package com.udea.reserveFlight.service.exception;

public class AppException extends RuntimeException{

    private final String errorCode;

    public AppException(String message) {
        super(message);
        this.errorCode = "UNKNOWN_ERROR"; // Código de error por defecto
    }

    public AppException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

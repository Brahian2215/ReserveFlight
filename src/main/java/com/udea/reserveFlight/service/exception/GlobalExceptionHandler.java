package com.udea.reserveFlight.service.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Maneja excepciones generales de la aplicación
    @ExceptionHandler(AppException.class)
    public GraphQLError handleAppException(AppException ex) {
        return GraphqlErrorBuilder.newError()
                .message(ex.getMessage())
                .errorType(getErrorType(ex.getErrorCode()))
                .build();
    }

    // Maneja excepciones de validación
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public GraphQLError handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder("Errores de validación:");
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errorMessage.append(" [").append(error.getField()).append("] ").append(error.getDefaultMessage())
        );
        return GraphqlErrorBuilder.newError()
                .message(errorMessage.toString())
                .errorType(ErrorType.BAD_REQUEST)
                .build();
    }

    private ErrorType getErrorType(String errorCode) {
        switch (errorCode) {
            case "NOT_FOUND":
                return ErrorType.NOT_FOUND;
            case "BAD_REQUEST":
                return ErrorType.BAD_REQUEST;
            default:
                return ErrorType.INTERNAL_ERROR;
        }
    }

}

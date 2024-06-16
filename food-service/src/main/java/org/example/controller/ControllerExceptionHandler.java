package org.example.controller;

import org.example.entity.dto.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(value = ResponseStatusException.class)
    public ResponseEntity<ErrorMessage> handleResponseStatusException(ResponseStatusException exception) {
        return ResponseEntity
                .status(exception.getStatus())
                .body(ErrorMessage.builder()
                        .errorMessage(exception.getReason())
                        .build()
                );
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorMessage.builder()
                        .errorMessage(exception.getMessage())
                        .build()
                );
    }
}

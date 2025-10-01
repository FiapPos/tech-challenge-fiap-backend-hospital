package com.fiap.techchallenge.appointment_service.web;

import com.fiap.techchallenge.appointment_service.core.exception.InvalidCredentialsException;
import com.fiap.techchallenge.appointment_service.core.exception.OrchestratorUnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentials(InvalidCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(OrchestratorUnavailableException.class)
    public ResponseEntity<String> handleOrchestratorUnavailable(OrchestratorUnavailableException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ex.getMessage());
    }
}

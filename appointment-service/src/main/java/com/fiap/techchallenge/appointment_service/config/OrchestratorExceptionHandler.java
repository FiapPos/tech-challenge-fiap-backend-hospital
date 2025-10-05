package com.fiap.techchallenge.appointment_service.config;

import com.fiap.techchallenge.appointment_service.infra.client.OrchestratorException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class OrchestratorExceptionHandler {

    @ExceptionHandler(OrchestratorException.class)
    public ResponseEntity<Object> handleOrchestratorException(OrchestratorException oe) {
        HttpStatus httpStatus = HttpStatus.resolve(oe.getStatus().value());
        if (httpStatus == null) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        String body = oe.getMessage();
        if (body != null && !body.isEmpty()) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(body, headers, httpStatus);
        }

        return ResponseEntity.status(httpStatus).build();
    }
}

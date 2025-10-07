package com.fiap.techchallenge.appointment_service.core.exception;

public class OrchestratorUnavailableException extends RuntimeException {
    public OrchestratorUnavailableException(String message) {
        super(message);
    }

    public OrchestratorUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}

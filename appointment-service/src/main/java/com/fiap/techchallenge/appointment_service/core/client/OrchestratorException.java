package com.fiap.techchallenge.appointment_service.core.client;

import org.springframework.http.HttpStatusCode;

public class OrchestratorException extends RuntimeException {

    private final HttpStatusCode status;

    public OrchestratorException(HttpStatusCode status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatusCode getStatus() {
        return status;
    }
}

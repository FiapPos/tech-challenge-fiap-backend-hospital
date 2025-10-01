package com.fiap.techchallenge.appointment_service.core.document;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthEventTest {

    @Test
    void fromString_null_retornaOrquestrador() {
        AuthEvent.AuthEventSource src = AuthEvent.AuthEventSource.fromString(null);
        assertEquals(AuthEvent.AuthEventSource.ORCHESTRATOR_SERVICE, src);
    }

    @Test
    void fromString_desconhecido_retornaOrquestrador() {
        AuthEvent.AuthEventSource src = AuthEvent.AuthEventSource.fromString("SOMETHING_ELSE");
        assertEquals(AuthEvent.AuthEventSource.ORCHESTRATOR_SERVICE, src);
    }

    @Test
    void fromString_valido_retornaMesmo() {
        AuthEvent.AuthEventSource src = AuthEvent.AuthEventSource.fromString("API_GATEWAY");
        assertEquals(AuthEvent.AuthEventSource.API_GATEWAY, src);
    }
}

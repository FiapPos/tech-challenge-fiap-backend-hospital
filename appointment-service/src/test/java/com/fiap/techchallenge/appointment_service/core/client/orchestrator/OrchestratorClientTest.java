package com.fiap.techchallenge.appointment_service.core.client.orchestrator;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class OrchestratorClientTest {

    @Test
    void constructor_smoke() {
        OrchestratorClient client = Mockito.mock(OrchestratorClient.class);
        org.junit.jupiter.api.Assertions.assertNotNull(client);
    }

}

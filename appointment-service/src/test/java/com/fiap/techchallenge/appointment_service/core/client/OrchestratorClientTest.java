package com.fiap.techchallenge.appointment_service.core.client;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.web.client.RestTemplate;

public class OrchestratorClientTest {

    @Test
    void constructor_smoke() {
        RestTemplate rt = Mockito.mock(RestTemplate.class);
        CircuitBreakerFactory<?, ?> cbf = Mockito.mock(CircuitBreakerFactory.class);
        OrchestratorClient client = new OrchestratorClient(rt, cbf, null);
        org.junit.jupiter.api.Assertions.assertNotNull(client);
    }

}

package com.fiap.techchallenge.appointment_service.core.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.fiap.techchallenge.appointment_service.core.dto.UsuarioResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import java.util.function.Supplier;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.retry.RetryConfig;
import java.time.Duration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class OrchestratorClientTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private CircuitBreakerFactory<?, ?> cbFactory;

    @Mock
    private CircuitBreaker circuitBreaker;

    private OrchestratorClient client;
    private RetryRegistry retryRegistry;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(cbFactory.create(anyString())).thenReturn(circuitBreaker);
        // by default have circuit breaker just run the supplier
        when(circuitBreaker.run(any(), any())).thenAnswer(invocation -> {
            Supplier<?> supplier = invocation.getArgument(0);
            return supplier.get();
        });

        // create a simple retry registry with small config for tests
        RetryConfig config = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofMillis(10))
                .build();
        this.retryRegistry = RetryRegistry.of(config);

        client = new OrchestratorClient(restTemplate, cbFactory, retryRegistry);
    }

    @Test
    void findUsuarioByLogin_happyPath_returnsUsuario() {
        UsuarioResponse expected = new UsuarioResponse();
        expected.setId(123L);
        expected.setLogin("joao");

        ResponseEntity<UsuarioResponse> resp = new ResponseEntity<>(expected, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(UsuarioResponse.class), anyString()))
                .thenReturn(resp);

        UsuarioResponse actual = client.findUsuarioByLogin("joao");
        assertNotNull(actual);
        assertEquals(123L, actual.getId());
    }

    @Test
    void findUsuarioByLogin_errorFromOrchestrator_throwsOrchestratorException() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(UsuarioResponse.class), anyString()))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad request"));

        OrchestratorException ex = assertThrows(OrchestratorException.class, () -> client.findUsuarioByLogin("joao"));
        assertNotNull(ex.getStatus());
    }

}

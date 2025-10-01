package com.fiap.techchallenge.appointment_service.core.service;

import com.fiap.techchallenge.appointment_service.core.client.OrchestratorClient;
import com.fiap.techchallenge.appointment_service.core.dto.UsuarioResponse;
import com.fiap.techchallenge.appointment_service.core.config.ResilienceProvider;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioService {

    private final OrchestratorClient orchestratorClient;
    private final ResilienceProvider resilienceProvider;

    public UsuarioResponse findUsuarioByLogin(String login) {
        CircuitBreakerConfig cbConfigFind = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .slidingWindowSize(10)
                .waitDurationInOpenState(Duration.ofSeconds(5))
                .build();
        CircuitBreaker cbFind = resilienceProvider.getOrCreateCircuitBreaker("orchestrator-find", cbConfigFind);

        RetryConfig configFind = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofMillis(200))
                .build();
        Retry retry = resilienceProvider.getOrCreateRetry("orchestrator-find", configFind);

        Supplier<UsuarioResponse> supplier = Retry.decorateSupplier(retry,
                CircuitBreaker.decorateSupplier(cbFind,
                        () -> orchestratorClient.findUsuarioByLogin(login)));

        // Let any exception propagate to the caller (Auth service / controller) where
        // it will be
        // translated to a proper HTTP status by the global handlers.
        return supplier.get();
    }
}

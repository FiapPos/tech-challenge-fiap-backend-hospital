package com.fiap.techchallenge.appointment_service.core.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class ResilienceProvider {

    private final RetryRegistry retryRegistry;
    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final ConcurrentMap<String, Retry> retries = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, CircuitBreaker> breakers = new ConcurrentHashMap<>();

    public ResilienceProvider() {
        this.retryRegistry = RetryRegistry.ofDefaults();
        this.circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults();
    }

    public ResilienceProvider(RetryRegistry retryRegistry, CircuitBreakerRegistry circuitBreakerRegistry) {
        this.retryRegistry = retryRegistry;
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    public Retry getOrCreateRetry(String name, RetryConfig config) {
        return retries.computeIfAbsent(name, key -> retryRegistry.retry(key, config));
    }

    public CircuitBreaker getOrCreateCircuitBreaker(String name, CircuitBreakerConfig config) {
        return breakers.computeIfAbsent(name, key -> circuitBreakerRegistry.circuitBreaker(key, config));
    }
}

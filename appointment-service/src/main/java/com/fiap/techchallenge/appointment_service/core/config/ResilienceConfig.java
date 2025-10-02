package com.fiap.techchallenge.appointment_service.core.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("coreResilienceConfig")
public class ResilienceConfig {

    @Bean("coreRetryRegistry")
    public RetryRegistry coreRetryRegistry() {
        return RetryRegistry.ofDefaults();
    }

    @Bean("coreCircuitBreakerRegistry")
    public CircuitBreakerRegistry coreCircuitBreakerRegistry() {
        return CircuitBreakerRegistry.ofDefaults();
    }
}

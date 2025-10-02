package com.fiap.techchallenge.appointment_service.config;

import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.retry.RetryConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;

@Configuration
public class ResilienceConfig {

    @Bean
    public RetryRegistry retryRegistry(
            @Value("${resilience4j.retry.instances.orchestrator-find.max-attempts:3}") int maxAttempts,
            @Value("${resilience4j.retry.instances.orchestrator-find.wait-duration:200ms}") String waitDurationStr) {
        Duration waitDuration = Duration.ofMillis(200);
        try {
            if (waitDurationStr.endsWith("ms")) {
                waitDuration = Duration.ofMillis(Long.parseLong(waitDurationStr.replace("ms", "")));
            } else if (waitDurationStr.endsWith("s")) {
                waitDuration = Duration.ofSeconds(Long.parseLong(waitDurationStr.replace("s", "")));
            }
        } catch (NumberFormatException nfe) {
        }

        RetryConfig config = RetryConfig.custom()
                .maxAttempts(maxAttempts)
                .waitDuration(waitDuration)
                .build();

        return RetryRegistry.of(config);
    }
}

package com.fiap.techchallenge.appointment_service.core.consumer;

// AuthEventConsumer disabled: no external auth consumption. Kept as placeholder.
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthEventConsumer {

    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}", topics = "${spring.kafka.topic.user-authenticated}")
    public void consumeUserAuthenticatedEvent(String payload) {
        // Auth events are handled locally by this microservice. External auth event consumption
        // has been disabled to avoid delegating authentication/authorization to the orchestrator.
        log.debug("Auth event consumer is disabled — ignoring payload from user-authenticated");
    }

    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}", topics = "${spring.kafka.topic.user-authorization}")
    public void consumeUserAuthorizationEvent(String payload) {
        // Disabled: authorization is evaluated locally; ignoring external authorization events.
        log.debug("Auth event consumer is disabled — ignoring payload from user-authorization");
    }

    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}", topics = "${spring.kafka.topic.auth-events}")
    public void consumeAuthEvent(String payload) {
        // Disabled generic auth events consumer. All auth responsibilities are local to this service.
        log.debug("Auth event consumer is disabled — ignoring payload from auth-events");
    }
}
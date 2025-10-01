package com.fiap.techchallenge.appointment_service.core.producer;

import com.fiap.techchallenge.appointment_service.core.document.AuthEvent;
// AuthEventProducer disabled: publishing auth events from this service is turned off.
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthEventProducer {
    // No-op producer: fields and configuration intentionally removed while publishing is disabled.

    public void sendAuthenticationEvent(AuthEvent authEvent) {
        // Publishing auth events disabled: authentication/authorization is handled locally.
        log.debug("AuthEventProducer.sendAuthenticationEvent called but publishing is disabled");
    }

    public void sendAuthorizationEvent(AuthEvent authEvent) {
        // Publishing auth events disabled: authorization decisions are local.
        log.debug("AuthEventProducer.sendAuthorizationEvent called but publishing is disabled");
    }

    public void sendGeneralAuthEvent(AuthEvent authEvent) {
        // Publishing general auth events disabled.
        log.debug("AuthEventProducer.sendGeneralAuthEvent called but publishing is disabled");
    }
}
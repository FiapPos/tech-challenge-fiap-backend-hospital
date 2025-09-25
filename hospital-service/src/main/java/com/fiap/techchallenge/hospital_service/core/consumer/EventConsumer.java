package com.fiap.techchallenge.hospital_service.core.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EventConsumer {

    private final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    public EventConsumer() {
        logger.info("EventConsumer inicializado - funcionalidade de agendamento removida");
    }

    // Consumer de eventos removido - agora focamos apenas no CRUD de hospitais
}

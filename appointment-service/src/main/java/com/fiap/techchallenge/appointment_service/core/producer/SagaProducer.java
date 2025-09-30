package com.fiap.techchallenge.appointment_service.core.producer;

import com.fiap.techchallenge.appointment_service.core.dto.Evento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class SagaProducer {

    private final Logger logger = LoggerFactory.getLogger(SagaProducer.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topic.comeca-saga}")
    private String comecaTopicoSaga;

    public SagaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(Evento evento) {
        try {
            logger.info("Enviando evento para o tópico {} com os dados {}", comecaTopicoSaga, evento);
            kafkaTemplate.send(comecaTopicoSaga, evento);
        } catch (Exception ex) {
            logger.info("Erro ao tentar enviar evento para o tópico {} com os dados {}", comecaTopicoSaga, evento, ex);
        }
    }
}
package com.fiap.techchallenge.historico_service.core.producer;

import com.fiap.techchallenge.historico_service.core.dto.Evento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    private final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topic.orquestrador}")
    private String topicoOrquestrador;

    public KafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(Evento evento) {
        try {
            logger.info("Enviando evento para o tópico {} com os dados {}", topicoOrquestrador, evento.toString());
            kafkaTemplate.send(topicoOrquestrador, evento);
        } catch (Exception ex) {
            logger.info("Erro ao tentar enviar evento para o tópico {} com os dados {}", topicoOrquestrador, evento, ex);
        }
    }
}

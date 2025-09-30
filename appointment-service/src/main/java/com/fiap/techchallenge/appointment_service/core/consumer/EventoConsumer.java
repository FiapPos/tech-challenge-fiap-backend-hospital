package com.fiap.techchallenge.appointment_service.core.consumer;

import com.fiap.techchallenge.appointment_service.core.dto.Evento;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EventoConsumer {

    private final Logger logger = LoggerFactory.getLogger(EventoConsumer.class);

    @KafkaListener(topics = "${spring.kafka.topic.notifica-fim}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "appointmentKafkaListenerContainerFactory")
    public void notificaFimSaga(Evento evento, Acknowledgment acknowledgement) {
        try {
            logger.info("Recebendo notificação de fim de evento {} do topico notifica-fim", evento);

            acknowledgement.acknowledge();
        } catch (Exception e) {
            logger.error("Erro ao processar notificação de fim: {}", e.getMessage(), e);
        }
    }

}

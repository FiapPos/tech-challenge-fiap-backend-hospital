package com.fiap.techchallenge.usuario_service.core.consumer;

import com.fiap.techchallenge.usuario_service.core.dtos.evento.Evento;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UsuarioConsumer {

    private final Logger logger = LoggerFactory.getLogger(UsuarioConsumer.class);

    @KafkaListener(topics = "${spring.kafka.topic.usuario-sucesso}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "appointmentKafkaListenerContainerFactory")
    public void consumirEventoSucesso(Evento dtoAgendamento, Acknowledgment acknowledgement) {
        try {
            logger.info("Processando evento de sucesso de agendamento: {}", dtoAgendamento);

            acknowledgement.acknowledge();
        } catch (Exception e) {
            logger.error("Erro ao processar sucesso de agendamento: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "${spring.kafka.topic.usuario-falha}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "appointmentKafkaListenerContainerFactory")
    public void consumirEventoFalha(Evento dtoAgendamento, Acknowledgment acknowledgement) {
        try {
            logger.info("Processando evento de falha de agendamento: {}", dtoAgendamento);

            acknowledgement.acknowledge();
        } catch (Exception e) {
            logger.error("Erro ao processar falha de agendamento: {}", e.getMessage(), e);
        }
    }


}

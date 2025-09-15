package com.fiap.techchallenge.notificacao_service.core.consumer;

import com.fiap.techchallenge.notificacao_service.core.dto.NotificacaoAgendamento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class EventConsumer {

    private final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    @KafkaListener(topics = "${spring.kafka.topic.notificacao-success}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumirNotificacao(NotificacaoAgendamento notificacaoAgendamento, Acknowledgment acknowledgement) {
        try {
            logger.info(notificacaoAgendamento.getTemplateDeMensagem());
            acknowledgement.acknowledge();
        } catch (Exception e) {
            logger.error("Erro ao processar notificacao: {}", e.getMessage());
        }
    }
}

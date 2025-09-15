package com.fiap.techchallenge.notificacao_service.core.producer;

import com.fiap.techchallenge.notificacao_service.core.dto.NotificacaoAgendamento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    private final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    private final KafkaTemplate<String, NotificacaoAgendamento> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, NotificacaoAgendamento> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(NotificacaoAgendamento notificacaoAgendamento) {
        try {
            logger.info("Enviando a notificação: {}", notificacaoAgendamento);
            kafkaTemplate.send("notificacao", notificacaoAgendamento);
            logger.info("Notificação enviada com sucesso: {}", notificacaoAgendamento);
        } catch (Exception ex) {
            logger.info("Não foi possível enviar a notificação de agendamento: {}", notificacaoAgendamento);
        }
    }
}

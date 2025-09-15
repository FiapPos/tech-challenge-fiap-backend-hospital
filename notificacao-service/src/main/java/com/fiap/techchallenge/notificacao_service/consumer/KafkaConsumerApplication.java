package com.fiap.techchallenge.notificacao_service.consumer;

import com.fiap.techchallenge.notificacao_service.dto.NotificacaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumerApplication {

    private final Logger logger = LoggerFactory.getLogger(KafkaConsumerApplication.class);

    @KafkaListener(topics = "notificacao", groupId = "notificacao-group")
    public void consumirNotificacao(NotificacaoService notificacaoService, Acknowledgment acknowledgement) {
        try {
            logger.info("Notificacao recebida: {}", notificacaoService);

            acknowledgement.acknowledge();
        } catch (Exception e) {
            logger.error("Erro ao processar notificacao: {}", e.getMessage());
        }
    }
}

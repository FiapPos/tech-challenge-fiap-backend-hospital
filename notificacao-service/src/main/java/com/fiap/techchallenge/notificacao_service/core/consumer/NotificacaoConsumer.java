package com.fiap.techchallenge.notificacao_service.core.consumer;

import com.fiap.techchallenge.notificacao_service.core.dto.NotificacaoParaAgendamento;
import com.fiap.techchallenge.notificacao_service.core.service.CriaNotificacaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class NotificacaoConsumer {

    private final Logger logger = LoggerFactory.getLogger(NotificacaoConsumer.class);

    private final CriaNotificacaoService criaNotificacaoService;

    public NotificacaoConsumer(CriaNotificacaoService criaNotificacaoService) {
        this.criaNotificacaoService = criaNotificacaoService;
    }

    @KafkaListener(topics = "${spring.kafka.topic.notificacao-sucesso}",
                   groupId = "${spring.kafka.consumer.group-id}",
                   containerFactory = "notificacaoKafkaListenerContainerFactory")
    public void consumirEventoSucesso(NotificacaoParaAgendamento notificacaoParaAgendamento, Acknowledgment acknowledgement) {
        try {
            logger.info("Processando evento de agendamento: {}", notificacaoParaAgendamento);
 
            criaNotificacaoService.processarNotificacao(notificacaoParaAgendamento);
            acknowledgement.acknowledge();
        } catch (Exception e) {
            logger.error("Erro ao processar agendamento: {}", e.getMessage(), e);
        }
    }

}

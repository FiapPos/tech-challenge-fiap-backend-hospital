package com.fiap.techchallenge.historico_service.core.consumer;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class HistoricoConsumer {

    private final Logger logger = LoggerFactory.getLogger(HistoricoConsumer.class);

    //TODO implementar o DTOParaReceberDadosHistorico do jeito que for mais adequado, com os dados e nomes necessários para processar o agendamento
/*
    @KafkaListener(topics = "${spring.kafka.topic.historico-successo}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "historicoListenerContainerFactory")
    public void consumirEventoSucesso(DTOParaReceberDadosHistorico dtoAgendamento, Acknowledgment acknowledgement) {
        try {
            logger.info("Processando evento de sucesso de historico: {}", dtoAgendamento);

            acknowledgement.acknowledge();
        } catch (Exception e) {
            logger.error("Erro ao processar sucesso de agendamento: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "${spring.kafka.topic.historico-falha}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "historicoListenerContainerFactory")
    public void consumirEventoFalha(DTOParaReceberDadosHistorico dtoAgendamento, Acknowledgment acknowledgement) {
        try {
            logger.info("Processando evento de falha de historico: {}", dtoAgendamento);

            acknowledgement.acknowledge();
        } catch (Exception e) {
            logger.error("Erro ao processar falha de agendamento: {}", e.getMessage(), e);
        }
    }*/

}

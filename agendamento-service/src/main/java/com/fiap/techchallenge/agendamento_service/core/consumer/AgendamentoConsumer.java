package com.fiap.techchallenge.agendamento_service.core.consumer;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AgendamentoConsumer {

    private final Logger logger = LoggerFactory.getLogger(AgendamentoConsumer.class);


//TODO implementar o DTOParaReceberDadosAgendamento do jeito que for mais adequado, com os dados e nomes necessários para processar o agendamento
/*
    @KafkaListener(topics = "${spring.kafka.topic.agendamento-sucesso}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "agendamentoKafkaListenerContainerFactory")
    public void consumirEventoSucesso(DTOParaReceberDadosAgendamento dtoAgendamento, Acknowledgment acknowledgement) {
        try {
            logger.info("Processando evento de sucesso de agendamento: {}", dtoAgendamento);

            acknowledgement.acknowledge();
        } catch (Exception e) {
            logger.error("Erro ao processar sucesso de agendamento: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "${spring.kafka.topic.agendamento-falha}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "agendamentoKafkaListenerContainerFactory")
    public void consumirEventoFalha(DTOParaReceberDadosAgendamento dtoAgendamento, Acknowledgment acknowledgement) {
        try {
            logger.info("Processando evento de falha de agendamento: {}", dtoAgendamento);

            acknowledgement.acknowledge();
        } catch (Exception e) {
            logger.error("Erro ao processar falha de agendamento: {}", e.getMessage(), e);
        }
    }*/

}

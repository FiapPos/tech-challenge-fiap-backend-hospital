package com.fiap.techchallenge.historico_service.core.consumer;

import com.fiap.techchallenge.historico_service.core.dto.DadosAgendamento;
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

    @KafkaListener(topics = "${spring.kafka.topic.historico-sucesso}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "appointmentKafkaListenerContainerFactory")
    public void consumirEventoSucesso(DadosAgendamento evento, Acknowledgment acknowledgement) {
        try {
            logger.info("Processando evento de sucesso de historico: {}", evento);

            acknowledgement.acknowledge();
        } catch (Exception e) {
            logger.error("Erro ao processar sucesso de historico: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "${spring.kafka.topic.historico-falha}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "appointmentKafkaListenerContainerFactory")
    public void consumirEventoFalha(DadosAgendamento evento, Acknowledgment acknowledgement) {
        try {
            logger.info("Processando evento de falha de historico: {}", evento);

            acknowledgement.acknowledge();
        } catch (Exception e) {
            logger.error("Erro ao processar falha de historico: {}", e.getMessage(), e);
        }
    }

}

package com.fiap.techchallenge.historico_service.core.consumer;

import com.fiap.techchallenge.historico_service.core.dto.DadosAgendamento;
import com.fiap.techchallenge.historico_service.core.service.HistoricoMedicoService;
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
    private final HistoricoMedicoService historicoMedicoService;

    @KafkaListener(topics = "${spring.kafka.topic.historico-sucesso}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "appointmentKafkaListenerContainerFactory")
    public void consumirEventoSucesso(DadosAgendamento evento, Acknowledgment acknowledgement) {
        try {
            logger.info("Processando evento de sucesso de historico: {}", evento);

            historicoMedicoService.salvarHistorico(evento);

            logger.info("Histórico salvo com sucesso para agendamento ID: {}", evento.getAgendamentoId());
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
            historicoMedicoService.atualizarHistorico(evento)
                .orElseGet(() -> historicoMedicoService.salvarHistorico(evento));

            logger.info("Histórico de falha processado para agendamento ID: {}", evento.getAgendamentoId());
            acknowledgement.acknowledge();
        } catch (Exception e) {
            logger.error("Erro ao processar falha de historico: {}", e.getMessage(), e);
        }
    }
}

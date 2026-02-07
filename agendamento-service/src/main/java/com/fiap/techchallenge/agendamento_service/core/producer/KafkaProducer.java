package com.fiap.techchallenge.agendamento_service.core.producer;

import com.fiap.techchallenge.agendamento_service.core.dto.DadosAgendamento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    @Value("${spring.kafka.topic.consultas}")
    private String topicoConsultas;

    @Value("${spring.kafka.topic.notificacao-sucesso}")
    private String topicoNotificacoes;

    @Value("${spring.kafka.topic.historico-sucesso}")
    private String topicoHistorico;

    @Value("${spring.kafka.topic.proposta-vaga:proposta-vaga}")
    private String topicoPropostaVaga;

    private final KafkaTemplate<String, DadosAgendamento> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, DadosAgendamento> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void enviarEventosParaNotificacao(DadosAgendamento dadosAgendamento) {
        enviarEventoNotificacoes(dadosAgendamento);
        enviarEventoConsulta(dadosAgendamento);
        enviarEventoHistorico(dadosAgendamento);
    }

    public void enviarEventosParaHistorico(DadosAgendamento dadosAgendamento) {
        enviarEventoConsulta(dadosAgendamento);
        enviarEventoHistorico(dadosAgendamento);
    }

    private void enviarEventoConsulta(DadosAgendamento evento) {
        try {
            logger.info("Enviando evento de consulta para o tópico Kafka '{}': {}", topicoConsultas, evento);
            kafkaTemplate.send(topicoConsultas, String.valueOf(evento.getAgendamentoId()), evento);
        } catch (Exception e) {
            logger.error("Erro ao enviar evento de consulta para o Kafka", e);
        }
    }

    private void enviarEventoNotificacoes(DadosAgendamento evento) {
        try {
            logger.info("Enviando evento de notificacao para o tópico Kafka '{}': {}", topicoNotificacoes, evento);
            kafkaTemplate.send(topicoNotificacoes, String.valueOf(evento.getAgendamentoId()), evento);
        } catch (Exception e) {
            logger.error("Erro ao enviar evento de notificacao para o Kafka", e);
        }
    }

    private void enviarEventoHistorico(DadosAgendamento evento) {
        try {
            logger.info("Enviando evento de historico para o tópico Kafka '{}': {}", topicoHistorico, evento);
            kafkaTemplate.send(topicoHistorico, String.valueOf(evento.getAgendamentoId()), evento);
        } catch (Exception e) {
            logger.error("Erro ao enviar evento de historico para o Kafka", e);
        }
    }

    /**
     * Envia proposta de vaga disponível para pacientes prioritários na fila de espera.
     * Esta mensagem é processada pelo serviço de notificação para enviar email/telegram.
     */
    public void enviarPropostaVagaDisponivel(DadosAgendamento evento) {
        try {
            logger.info("Enviando proposta de vaga disponível para o tópico Kafka '{}': paciente {}",
                    topicoPropostaVaga, evento.getPacienteId());
            kafkaTemplate.send(topicoPropostaVaga, String.valueOf(evento.getPacienteId()), evento);
            // Também envia para o tópico de notificações para processamento
            enviarEventoNotificacoes(evento);
        } catch (Exception e) {
            logger.error("Erro ao enviar proposta de vaga disponível para o Kafka", e);
        }
    }
}

package com.fiap.techchallenge.notificacao_service.core.service;

import com.fiap.techchallenge.notificacao_service.core.dto.*;
import com.fiap.techchallenge.notificacao_service.core.enums.ESagaStatus;
import com.fiap.techchallenge.notificacao_service.core.producer.KafkaProducer;
import com.fiap.techchallenge.notificacao_service.core.strategy.NotificacaoStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

import static com.fiap.techchallenge.notificacao_service.core.enums.ESagaStatus.*;

@Component
public class CriaNotificacaoService {

    private static final Logger logger = LoggerFactory.getLogger(CriaNotificacaoService.class);
    private final KafkaProducer kafkaProducer;
    private final List<NotificacaoStrategy> estrategias;

    public CriaNotificacaoService(KafkaProducer kafkaProducer, List<NotificacaoStrategy> estrategias) {
        this.kafkaProducer = Objects.requireNonNull(kafkaProducer, "KafkaProducer não pode ser nulo");
        this.estrategias = Objects.requireNonNull(estrategias, "Lista de estratégias não pode ser nula");
    }

    public void processarNotificacao(NotificacaoParaAgendamento agendamento) {

        try {
            NotificacaoStrategy estrategia = selecionarEstrategia(agendamento);
            String mensagem = estrategia.processar(agendamento);
            
            logger.info("Processando notificação para agendamento ID: {}", agendamento.getId());
            logger.info("Nova mensagem recebida: {}", mensagem);
            
            enviarEventoOrquestrador(SUCCESS);
            logger.info("Notificação processada com sucesso para agendamento ID: {}", agendamento.getId());
            
        } catch (Exception e) {
            logger.error("Erro ao processar notificação para agendamento ID: {}", agendamento.getId(), e);
            enviarEventoOrquestrador(FAIL);
        }
    }

    private NotificacaoStrategy selecionarEstrategia(NotificacaoParaAgendamento agendamento) {
        return estrategias.stream()
                .filter(estrategia -> estrategia.isAplicavel(agendamento))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                    "Nenhuma estratégia encontrada para processar o agendamento ID: " + agendamento.getId()));
    }

    private void enviarEventoOrquestrador(ESagaStatus status) {
        try {
            EventoOrquestrador evento = new EventoOrquestrador(status);
            kafkaProducer.sendEvent(evento);
            logger.debug("Evento orquestrador enviado com status: {}", status);
        } catch (Exception e) {
            logger.error("Erro ao enviar evento orquestrador com status: {}", status, e);
            throw e;
        }
    }
}

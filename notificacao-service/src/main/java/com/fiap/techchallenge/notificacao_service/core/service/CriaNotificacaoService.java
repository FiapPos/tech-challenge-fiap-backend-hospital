package com.fiap.techchallenge.notificacao_service.core.service;

import com.fiap.techchallenge.notificacao_service.core.dto.*;
import com.fiap.techchallenge.notificacao_service.core.strategy.NotificacaoStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class CriaNotificacaoService {

    private static final Logger logger = LoggerFactory.getLogger(CriaNotificacaoService.class);
    private final List<NotificacaoStrategy> estrategias;

    public CriaNotificacaoService(List<NotificacaoStrategy> estrategias) {
        this.estrategias = Objects.requireNonNull(estrategias, "Lista de estratégias não pode ser nula");
    }

    public void processarNotificacao(DadosAgendamento dados) {

        try {
            NotificacaoStrategy estrategia = selecionarEstrategia(dados);
            String mensagem = estrategia.processar(dados);
            
            logger.info("Processando notificação para agendamento ID: {}", dados.getAgendamentoId());
            logger.info("Nova mensagem recebida: {}", mensagem);
            logger.info("Notificação processada com sucesso para agendamento ID: {}", dados.getAgendamentoId());
        } catch (Exception e) {
            logger.error("Erro ao processar notificação para agendamento ID: {}", dados.getAgendamentoId(), e);
        }
    }

    private NotificacaoStrategy selecionarEstrategia(DadosAgendamento dados) {
        return estrategias.stream()
                .filter(estrategia -> estrategia.isAplicavel(dados))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                    "Nenhuma estratégia encontrada para processar o agendamento ID: " + dados.getAgendamentoId()));
    }

}

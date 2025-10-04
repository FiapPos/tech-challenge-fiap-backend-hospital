package com.fiap.techchallenge.notificacao_service.core.strategy;

import com.fiap.techchallenge.notificacao_service.core.dto.DadosAgendamento;
import com.fiap.techchallenge.notificacao_service.core.dto.NotificacaoAgendamentoEdicao;
import org.springframework.stereotype.Component;

import static com.fiap.techchallenge.notificacao_service.core.enums.EStatusAgendamento.CANCELADA;

@Component
public class NotificacaoEdicaoStrategy implements NotificacaoStrategy {

    @Override
    public String processar(DadosAgendamento agendamento) {
        NotificacaoAgendamentoEdicao notificacao = new NotificacaoAgendamentoEdicao(agendamento);
        return notificacao.getTemplateDeMensagem();
    }

    @Override
    public boolean isAplicavel(DadosAgendamento agendamento) {
        return agendamento.getAtualizadoEm() != null && !CANCELADA.equals(agendamento.getStatusAgendamento());
    }
}

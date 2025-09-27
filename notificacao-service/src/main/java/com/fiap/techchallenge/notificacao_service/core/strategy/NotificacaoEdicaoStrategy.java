package com.fiap.techchallenge.notificacao_service.core.strategy;

import com.fiap.techchallenge.notificacao_service.core.dto.NotificacaoAgendamentoEdicao;
import com.fiap.techchallenge.notificacao_service.core.dto.NotificacaoParaAgendamento;
import org.springframework.stereotype.Component;

@Component
public class NotificacaoEdicaoStrategy implements NotificacaoStrategy {

    @Override
    public String processar(NotificacaoParaAgendamento agendamento) {
        NotificacaoAgendamentoEdicao notificacao = new NotificacaoAgendamentoEdicao(agendamento);
        return notificacao.getTemplateDeMensagem();
    }

    @Override
    public boolean isAplicavel(NotificacaoParaAgendamento agendamento) {
        return agendamento.getAtualizadoEm() != null;
    }
}

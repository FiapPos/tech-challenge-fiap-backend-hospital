package com.fiap.techchallenge.notificacao_service.core.strategy;

import com.fiap.techchallenge.notificacao_service.core.dto.NotificacaoAgendamentoCriacao;
import com.fiap.techchallenge.notificacao_service.core.dto.NotificacaoParaAgendamento;
import org.springframework.stereotype.Component;

@Component
public class NotificacaoCriacaoStrategy implements NotificacaoStrategy {

    @Override
    public String processar(NotificacaoParaAgendamento agendamento) {
        NotificacaoAgendamentoCriacao notificacao = new NotificacaoAgendamentoCriacao(agendamento);
        return notificacao.getTemplateDeMensagem();
    }

    @Override
    public boolean isAplicavel(NotificacaoParaAgendamento agendamento) {
        return agendamento.getAtualizadoEm() == null;
    }
}

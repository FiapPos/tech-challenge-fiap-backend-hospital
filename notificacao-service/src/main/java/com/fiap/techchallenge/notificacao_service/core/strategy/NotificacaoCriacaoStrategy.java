package com.fiap.techchallenge.notificacao_service.core.strategy;

import com.fiap.techchallenge.notificacao_service.core.dto.DadosAgendamento;
import com.fiap.techchallenge.notificacao_service.core.dto.NotificacaoAgendamentoCriacao;
import org.springframework.stereotype.Component;

@Component
public class NotificacaoCriacaoStrategy implements NotificacaoStrategy {

    @Override
    public String processar(DadosAgendamento agendamento) {
        NotificacaoAgendamentoCriacao notificacao = new NotificacaoAgendamentoCriacao(agendamento);
        return notificacao.getTemplateDeMensagem();
    }

    @Override
    public boolean isAplicavel(DadosAgendamento agendamento) {
        return agendamento.getAtualizadoEm() == null;
    }
}

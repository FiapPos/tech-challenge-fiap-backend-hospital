package com.fiap.techchallenge.notificacao_service.core.strategy;

import com.fiap.techchallenge.notificacao_service.core.dto.DadosAgendamento;
import com.fiap.techchallenge.notificacao_service.core.dto.NotificacaoAgendamentoCancelamento;
import com.fiap.techchallenge.notificacao_service.core.dto.NotificacaoAgendamentoEdicao;
import org.springframework.stereotype.Component;

import static com.fiap.techchallenge.notificacao_service.core.enums.EStatusAgendamento.CANCELADA;

@Component
public class NotificacaoCancelamentoStrategy implements NotificacaoStrategy {

    @Override
    public String processar(DadosAgendamento agendamento) {
        NotificacaoAgendamentoCancelamento notificacao = new NotificacaoAgendamentoCancelamento(agendamento);
        return notificacao.getTemplateDeMensagem();
    }

    @Override
    public boolean isAplicavel(DadosAgendamento agendamento) {
        return CANCELADA.equals(agendamento.getStatusAgendamento());
    }
}

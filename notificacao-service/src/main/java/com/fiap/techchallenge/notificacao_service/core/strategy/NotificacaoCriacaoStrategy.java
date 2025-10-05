package com.fiap.techchallenge.notificacao_service.core.strategy;

import com.fiap.techchallenge.notificacao_service.core.dto.DadosAgendamento;
import com.fiap.techchallenge.notificacao_service.core.dto.NotificacaoAgendamentoCriacao;
import org.springframework.stereotype.Component;

import static com.fiap.techchallenge.notificacao_service.core.enums.EStatusAgendamento.CRIADA;

@Component
public class NotificacaoCriacaoStrategy implements NotificacaoStrategy {

    @Override
    public String processar(DadosAgendamento agendamento) {
        NotificacaoAgendamentoCriacao notificacao = new NotificacaoAgendamentoCriacao(agendamento);
        return notificacao.getTemplateDeMensagem();
    }

    @Override
    public boolean isAplicavel(DadosAgendamento agendamento) {
        return CRIADA.equals(agendamento.getStatusAgendamento());
    }
}

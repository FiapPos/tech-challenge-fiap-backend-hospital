package com.fiap.techchallenge.notificacao_service.core.dto;

public class NotificacaoAgendamentoCriacao extends NotificacaoAgendamento {

    public NotificacaoAgendamentoCriacao(NotificacaoParaAgendamento agendamento) {
        super(agendamento);
    }

    @Override
    public String getTemplateDeMensagem() {
        return String.format(
            "Olá, %s! Seu agendamento foi confirmado para %s na especialidade %s. Valor: R$ %.2f",
            getNome(),
            getDataHoraFormatada(),
            getEspecializacao(),
            getValor()
        );
    }
}

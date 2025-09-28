package com.fiap.techchallenge.notificacao_service.core.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class NotificacaoAgendamentoEdicao extends NotificacaoAgendamento {

    public NotificacaoAgendamentoEdicao(NotificacaoParaAgendamento agendamento) {
        super(agendamento);
    }

    @Override
    public String getTemplateDeMensagem() {
        return String.format(
            "Olá, %s! Seu agendamento foi atualizado para %s na especialidade %s. Valor: R$ %.2f",
            getNome(),
            getDataHoraFormatada(),
            getEspecializacao(),
            getValor()
        );
    }
}

package com.fiap.techchallenge.notificacao_service.core.dto;

public class NotificacaoAgendamentoCriacao extends NotificacaoAgendamento {

    public NotificacaoAgendamentoCriacao(DadosAgendamento agendamento) {
        super(agendamento);
    }

    @Override
    public String getTemplateDeMensagem() {
        return String.format(
            "Olá, %s! Seu agendamento foi confirmado! Seguem as informações: Consulta com %s na especialidade %s em %s, no endereço %s em %s.",
                getNomePaciente(),
                getNomeMedico(),
                getEspecializacao(),
                getNomeHospital(),
                getEnderecoHospital(),
                getDataHoraFormatada()
        );
    }
}

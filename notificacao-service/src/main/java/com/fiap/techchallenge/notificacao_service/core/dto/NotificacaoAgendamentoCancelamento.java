package com.fiap.techchallenge.notificacao_service.core.dto;

public class NotificacaoAgendamentoCancelamento extends NotificacaoAgendamento {

    public NotificacaoAgendamentoCancelamento(DadosAgendamento agendamento) {
        super(agendamento);
    }

    @Override
    public String getTemplateDeMensagem() {
        return String.format(
            "Olá, %s. Infelizmente, o seguinte agendamento foi cancelado. Seguem as informações: Consulta com %s na especialidade %s em %s, no endereço %s em %s.",
                getNomePaciente(),
                getNomeMedico(),
                getEspecializacao(),
                getNomeHospital(),
                getEnderecoHospital(),
                getDataHoraFormatada()
        );
    }
}

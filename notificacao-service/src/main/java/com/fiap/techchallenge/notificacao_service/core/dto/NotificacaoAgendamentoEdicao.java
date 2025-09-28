package com.fiap.techchallenge.notificacao_service.core.dto;

public class NotificacaoAgendamentoEdicao extends NotificacaoAgendamento {

    public NotificacaoAgendamentoEdicao(DadosAgendamento agendamento) {
        super(agendamento);
    }

    @Override
    public String getTemplateDeMensagem() {
        return String.format(
                "Olá, %s. Seu agendamento foi atualizado! Seguem as novas informações: Consulta com %s na especialidade %s em %s, no endereço %s em %s.",
            getNomePaciente(),
            getNomeMedico(),
            getEspecializacao(),
            getNomeHospital(),
            getEnderecoHospital(),
            getDataHoraFormatada()
        );
    }
}

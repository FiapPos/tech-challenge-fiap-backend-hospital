package com.fiap.techchallenge.notificacao_service.core.service;

import com.fiap.techchallenge.notificacao_service.core.dto.AgendamentoCriadoEvento;
import com.fiap.techchallenge.notificacao_service.core.dto.AgendamentoEditadoEvento;
import com.fiap.techchallenge.notificacao_service.core.dto.NotificacaoAgendamentoCriacao;
import com.fiap.techchallenge.notificacao_service.core.dto.NotificacaoAgendamentoEdicao;
import org.springframework.stereotype.Component;

@Component
public class CriaNotificacaoService {

    public NotificacaoAgendamentoCriacao execute(AgendamentoCriadoEvento agendamentoCriado) {
        return new NotificacaoAgendamentoCriacao(
                agendamentoCriado.getNomePaciente(),
                agendamentoCriado.getDataHora(),
                agendamentoCriado.getEspecializacao(),
                agendamentoCriado.getValor()
        );

    }

    public NotificacaoAgendamentoEdicao execute(AgendamentoEditadoEvento agendamentoEditado) {
        return new NotificacaoAgendamentoEdicao(
                agendamentoEditado.getNomePaciente(),
                agendamentoEditado.getDataHora(),
                agendamentoEditado.getEspecializacao(),
                agendamentoEditado.getValor()
        );
    }
}

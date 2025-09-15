package com.fiap.techchallenge.notificacao_service.core.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotificacaoAgendamentoEdicao extends NotificacaoAgendamento {

    @JsonProperty("atualizadoEm")
    private LocalDateTime atualizadoEm;

    @JsonCreator
    public NotificacaoAgendamentoEdicao(
            @JsonProperty("nome") String nome,
            @JsonProperty("dataHora") LocalDateTime dataHora,
            @JsonProperty("especializacao") String especializacao,
            @JsonProperty("valor") BigDecimal valor) {
        super(nome, dataHora, especializacao, valor);
        this.atualizadoEm = LocalDateTime.now();
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    @Override
    public String getTemplateDeMensagem() {
        return """
                Nova notificação: Um agendamento foi editado com sucesso!
                
                Nome da pessoa paciente: %s
                Especialização: %s
                Data e hora: %s
                Valor: %s
                Atualizada em: %s
                """.formatted(
                getNome(),
                getEspecializacao(),
                formate(getDataHora()),
                getValor(),
                formate(getAtualizadoEm())
        );
    }

    private String formate(LocalDateTime data) {
        return data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}

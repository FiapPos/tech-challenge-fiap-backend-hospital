package com.fiap.techchallenge.notificacao_service.core.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotificacaoAgendamentoCriacao extends NotificacaoAgendamento {

    @JsonCreator
    public NotificacaoAgendamentoCriacao(
            @JsonProperty("nome") String nome,
            @JsonProperty("dataHora") LocalDateTime dataHora,
            @JsonProperty("especializacao") String especializacao,
            @JsonProperty("valor") BigDecimal valor) {
        super(nome, dataHora, especializacao, valor);
    }

    @Override
    public String getTemplateDeMensagem() {
        return """
                Nova notificação: Um novo agendamento foi criado com sucesso!
                
                Nome da pessoa paciente: %s
                Especialização: %s
                Data e hora: %s
                Valor: %s
                """.formatted(
                        getNome(),
                        getEspecializacao(),
                        getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                        getValor()
        );
    }
}

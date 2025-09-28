package com.fiap.techchallenge.agendamento_service.core.dto;

import com.fiap.techchallenge.agendamento_service.core.enums.ESagaStatus;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Evento implements Serializable {

    private String fonte;
    private ESagaStatus status;
    private DadosAgendamento dados;

    public Evento() {
    }

    public Evento(DadosAgendamento dadosAgendamento) {
        this.dados = dadosAgendamento;
    }

    @JsonCreator
    public Evento(@JsonProperty("fonte") String fonte,
                  @JsonProperty("status") ESagaStatus status,
                  @JsonProperty("dados") DadosAgendamento dados) {
        this.fonte = fonte;
        this.status = status;
        this.dados = dados;
    }

    @Override
    public String toString() {
        return "Evento{" +
                "fonte='" + fonte + '\'' +
                ", status=" + status +
                ", dados=" + (dados != null ? "DadosAgendamento{agendamentoId=" + dados.getAgendamentoId() + "}" : "null") +
                '}';
    }

    public LocalDateTime getAtualizadoEm() {
        return this.dados.getAtualizadoEm();
    }
}

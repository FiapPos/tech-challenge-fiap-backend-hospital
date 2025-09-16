package com.fiap.techchallenge.agendamento_service.core.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AgendamentoCriado implements Serializable {
    private final String id;
    private final String nomePaciente;
    private final LocalDateTime dataHora;
    private final String especializacao;
    private final BigDecimal valor;
    private final LocalDateTime criadoEm;

    public AgendamentoCriado(String id, String nomePaciente, LocalDateTime dataHora, String especializacao, BigDecimal valor) {
        this.id = id;
        this.nomePaciente = nomePaciente;
        this.dataHora = dataHora;
        this.especializacao = especializacao;
        this.valor = valor;
        this.criadoEm = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public String getNomePaciente() {
        return nomePaciente;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String getEspecializacao() {
        return especializacao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
}

package com.fiap.techchallenge.notificacao_service.core.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class NotificacaoAgendamento implements Serializable {
    private final String nome;
    private final LocalDateTime dataHora;
    private final String especializacao;
    private final BigDecimal valor;
    private LocalDateTime criadoEm;

    public NotificacaoAgendamento(String nome, LocalDateTime dataHora, String especializacao, BigDecimal valor) {
        this.nome = nome;
        this.dataHora = dataHora;
        this.especializacao = especializacao;
        this.valor = valor;
        this.criadoEm = LocalDateTime.now();
    }

    public String getNome() {
        return nome;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String getDataHoraFormatada() {
        return getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
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

    public abstract String getTemplateDeMensagem();
}

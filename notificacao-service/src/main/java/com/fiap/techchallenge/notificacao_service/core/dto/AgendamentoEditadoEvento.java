package com.fiap.techchallenge.notificacao_service.core.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
public class AgendamentoEditadoEvento implements Serializable {
    @NotNull
    private String id;

    @NotBlank
    private String nomePaciente;

    @FutureOrPresent
    private LocalDateTime dataHora;

    @NotBlank
    private String especializacao;

    @NotNull
    private BigDecimal valor;

    @NotNull
    private LocalDateTime criadoEm;

    @NotNull
    private LocalDateTime editadoEm;

    // Construtor padrão para deserialização
    public AgendamentoEditadoEvento() {}

    public AgendamentoEditadoEvento(@NotNull String id,
                                    String nomePaciente,
                                    LocalDateTime dataHora,
                                    String especializacao,
                                    @NotNull BigDecimal valor,
                                    @NotNull LocalDateTime criadoEm,
                                    @NotNull LocalDateTime editadoEm) {
        this.id = id;
        this.nomePaciente = nomePaciente;
        this.dataHora = dataHora;
        this.especializacao = especializacao;
        this.valor = valor;
        this.criadoEm = criadoEm;
        this.editadoEm = editadoEm;
    }

    public @NotNull String getId() {
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

    public @NotNull BigDecimal getValor() {
        return valor;
    }

    public @NotNull LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public @NotNull LocalDateTime getEditadoEm() {
        return editadoEm;
    }

    @Override
    public String toString() {
        return "AgendamentoEditadoEvento{" +
                "id='" + id + '\'' +
                ", nomePaciente='" + nomePaciente + '\'' +
                ", dataHora=" + dataHora +
                ", especializacao='" + especializacao + '\'' +
                ", valor=" + valor +
                ", editadoEm=" + editadoEm +
                '}';
    }
}

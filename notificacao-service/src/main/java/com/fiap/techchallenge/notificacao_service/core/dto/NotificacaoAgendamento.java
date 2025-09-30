package com.fiap.techchallenge.notificacao_service.core.dto;

import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Setter
public abstract class NotificacaoAgendamento implements Serializable {
    private String nomePaciente;
    private String nomeMedico;
    private String nomeHospital;
    private String enderecoHospital;
    private String especializacao;
    private LocalDateTime dataHoraAgendamento;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public NotificacaoAgendamento(DadosAgendamento agendamento) {
        this.nomePaciente = agendamento.getNomePaciente();
        this.nomeMedico = agendamento.getNomeMedico();
        this.nomeHospital = agendamento.getNomeHospital();
        this.enderecoHospital = agendamento.getEnderecoHospital();
        this.especializacao = agendamento.getEspecializacao();
        this.dataHoraAgendamento = agendamento.getDataHoraAgendamento();
        this.criadoEm = agendamento.getCriadoEm();
        this.atualizadoEm = agendamento.getAtualizadoEm();
    }

    public String getDataHoraFormatada() {
        return getDataHoraAgendamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public abstract String getTemplateDeMensagem();

    public String getNomePaciente() {
        return nomePaciente;
    }

    public String getNomeMedico() {
        return nomeMedico;
    }

    public String getNomeHospital() {
        return nomeHospital;
    }

    public String getEnderecoHospital() {
        return enderecoHospital;
    }

    public String getEspecializacao() {
        return especializacao;
    }

    public LocalDateTime getDataHoraAgendamento() {
        return dataHoraAgendamento;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }
}

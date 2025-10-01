package com.fiap.techchallenge.agendamento_service.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fiap.techchallenge.agendamento_service.core.entity.Consulta;
import com.fiap.techchallenge.agendamento_service.core.enums.EStatusAgendamento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DadosAgendamento implements Serializable {

    private long agendamentoId;
    private long pacienteId;
    private long hospitalId;
    private long medicoId;

    private String nomePaciente;
    private String nomeMedico;
    private String nomeHospital;
    private String enderecoHospital;
    private String especializacao;
    private EStatusAgendamento statusAgendamento;
    private LocalDateTime dataHoraAgendamento;
    private LocalDateTime criadoEm = LocalDateTime.now();
    private LocalDateTime atualizadoEm;

    public DadosAgendamento(Consulta consulta) {
        pacienteId = consulta.getPacienteId();
        medicoId = consulta.getMedicoId();
        agendamentoId = consulta.getId();
        dataHoraAgendamento = consulta.getDataHora();
        statusAgendamento = consulta.getStatus();
    }

    public long getAgendamentoId() {
        return agendamentoId;
    }

    public long getPacienteId() {
        return pacienteId;
    }

    public long getHospitalId() {
        return hospitalId;
    }

    public long getMedicoId() {
        return medicoId;
    }

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

    public EStatusAgendamento getStatusAgendamento() {
        return statusAgendamento;
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

    public void setAgendamentoId(long agendamentoId) {
        this.agendamentoId = agendamentoId;
    }

    public void setPacienteId(long pacienteId) {
        this.pacienteId = pacienteId;
    }

    public void setHospitalId(long hospitalId) {
        this.hospitalId = hospitalId;
    }

    public void setMedicoId(long medicoId) {
        this.medicoId = medicoId;
    }

    public void setNomePaciente(String nomePaciente) {
        this.nomePaciente = nomePaciente;
    }

    public void setNomeMedico(String nomeMedico) {
        this.nomeMedico = nomeMedico;
    }

    public void setNomeHospital(String nomeHospital) {
        this.nomeHospital = nomeHospital;
    }

    public void setEnderecoHospital(String enderecoHospital) {
        this.enderecoHospital = enderecoHospital;
    }

    public void setEspecializacao(String especializacao) {
        this.especializacao = especializacao;
    }

    public void setStatusAgendamento(EStatusAgendamento statusAgendamento) {
        this.statusAgendamento = statusAgendamento;
    }

    public void setDataHoraAgendamento(LocalDateTime dataHoraAgendamento) {
        this.dataHoraAgendamento = dataHoraAgendamento;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

    public String getDataHoraFormatada() {
        return getDataHoraAgendamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}

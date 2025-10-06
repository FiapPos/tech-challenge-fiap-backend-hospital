package com.fiap.techchallenge.orchestrator_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fiap.techchallenge.orchestrator_service.enums.EStatusAgendamento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DadosAgendamento implements Serializable {

    private long agendamentoId;
    private long pacienteId;
    private long hospitalId;
    private long especialidadeId;
    private long medicoId;

    private String nomePaciente;
    private String nomeMedico;
    private String nomeHospital;
    private String enderecoHospital;
    private String especializacao;

    private String sagaId;
    private EStatusAgendamento statusAgendamento;

    private LocalDateTime dataHoraAgendamento;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
    private String observacoes;

    public void atualiza(EspecialidadeDTO especialidade,
                         UsuarioDto medico,
                         HospitalDTO hospital,
                         LocalDateTime dataHora,
                         String observacoes,
                         UsuarioDto paciente) {

        atualiza(especialidade, medico, hospital, paciente);
        this.dataHoraAgendamento = dataHora;
        if (observacoes != null) this.observacoes = observacoes;
    }

    public void atualiza(EspecialidadeDTO especialidade, UsuarioDto medico, HospitalDTO hospital, UsuarioDto paciente) {
        this.especialidadeId = especialidade.getId();
        this.medicoId = medico.getId();
        this.hospitalId = hospital.getId();
        this.pacienteId = paciente.getId();
        this.nomeHospital = hospital.getNome();
        this.nomeMedico = medico.getNome();
        this.nomePaciente = paciente.getNome();
        this.enderecoHospital = hospital.getEndereco();
        this.especializacao = especialidade.getNome();
    }

    public long getAgendamentoId() {
        return agendamentoId;
    }

    public void setAgendamentoId(long agendamentoId) {
        this.agendamentoId = agendamentoId;
    }

    public long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(long pacienteId) {
        this.pacienteId = pacienteId;
    }

    public long getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(long hospitalId) {
        this.hospitalId = hospitalId;
    }

    public long getEspecialidadeId() {
        return especialidadeId;
    }

    public void setEspecialidadeId(long especialidadeId) {
        this.especialidadeId = especialidadeId;
    }

    public long getMedicoId() {
        return medicoId;
    }

    public void setMedicoId(long medicoId) {
        this.medicoId = medicoId;
    }

    public String getNomePaciente() {
        return nomePaciente;
    }

    public void setNomePaciente(String nomePaciente) {
        this.nomePaciente = nomePaciente;
    }

    public String getNomeMedico() {
        return nomeMedico;
    }

    public void setNomeMedico(String nomeMedico) {
        this.nomeMedico = nomeMedico;
    }

    public String getNomeHospital() {
        return nomeHospital;
    }

    public void setNomeHospital(String nomeHospital) {
        this.nomeHospital = nomeHospital;
    }

    public String getEnderecoHospital() {
        return enderecoHospital;
    }

    public void setEnderecoHospital(String enderecoHospital) {
        this.enderecoHospital = enderecoHospital;
    }

    public String getEspecializacao() {
        return especializacao;
    }

    public void setEspecializacao(String especializacao) {
        this.especializacao = especializacao;
    }

    public String getSagaId() {
        return sagaId;
    }

    public void setSagaId(String sagaId) {
        this.sagaId = sagaId;
    }

    public EStatusAgendamento getStatusAgendamento() {
        return statusAgendamento;
    }

    public void setStatusAgendamento(EStatusAgendamento statusAgendamento) {
        this.statusAgendamento = statusAgendamento;
    }

    public LocalDateTime getDataHoraAgendamento() {
        return dataHoraAgendamento;
    }

    public void setDataHoraAgendamento(LocalDateTime dataHoraAgendamento) {
        this.dataHoraAgendamento = dataHoraAgendamento;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}

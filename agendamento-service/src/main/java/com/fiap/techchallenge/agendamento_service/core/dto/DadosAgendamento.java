package com.fiap.techchallenge.agendamento_service.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    public DadosAgendamento(Consulta consulta) {
        this.agendamentoId = consulta.getId();
        this.pacienteId = consulta.getPacienteId();
        this.hospitalId = consulta.getHospitalId();
        this.especialidadeId = consulta.getEspecialidadeId();
        this.medicoId = consulta.getMedicoId();
        this.nomePaciente = consulta.getNomePaciente();
        this.nomeMedico = consulta.getNomeMedico();
        this.nomeHospital = consulta.getNomeHospital();
        this.enderecoHospital = consulta.getEnderecoHospital();
        this.especializacao = consulta.getEspecializacao();
        this.observacoes = consulta.getObservacoes();
        this.criadoEm = consulta.getCriadoEm();
        this.atualizadoEm = consulta.getAtualizadoEm();
        this.dataHoraAgendamento = consulta.getDataHora();
        this.statusAgendamento = consulta.getStatus();
    }

}
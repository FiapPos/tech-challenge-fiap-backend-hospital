package com.fiap.techchallenge.orchestrator_service.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fiap.techchallenge.orchestrator_service.enums.EStatusAgendamento;
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

    private String servicoId;
    private String sagaId;
    private EStatusAgendamento statusAgendamento;

    private LocalDateTime dataHoraAgendamento;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
    private String observacoes;

    public DadosAgendamento(long pacienteId,
                            long hospitalId,
                            long medicoId,
                            long agendamentoId,
                            String nomePaciente,
                            String nomeMedico,
                            String nomeHospital,
                            String enderecoHospital,
                            String especializacao,
                            LocalDateTime dataHoraAgendamento,
                            LocalDateTime criadoEm) {
        this.pacienteId = pacienteId;
        this.hospitalId = hospitalId;
        this.medicoId = medicoId;
        this.agendamentoId = agendamentoId;
        this.nomePaciente = nomePaciente;
        this.nomeMedico = nomeMedico;
        this.nomeHospital = nomeHospital;
        this.enderecoHospital = enderecoHospital;
        this.especializacao = especializacao;
        this.dataHoraAgendamento = dataHoraAgendamento;
        this.criadoEm = criadoEm;
    }

    public DadosAgendamento(DadosAgendamento outro) {
        this.agendamentoId = outro.agendamentoId;
        this.pacienteId = outro.pacienteId;
        this.hospitalId = outro.hospitalId;
        this.medicoId = outro.medicoId;
        this.nomePaciente = outro.nomePaciente;
        this.nomeMedico = outro.nomeMedico;
        this.nomeHospital = outro.nomeHospital;
        this.enderecoHospital = outro.enderecoHospital;
        this.especializacao = outro.especializacao;
        this.servicoId = outro.servicoId;
        this.sagaId = outro.sagaId;
        this.statusAgendamento = outro.statusAgendamento;
        this.dataHoraAgendamento = outro.dataHoraAgendamento;
        this.criadoEm = outro.criadoEm;
        this.atualizadoEm = outro.atualizadoEm;
        this.observacoes = outro.observacoes;
    }

    public String getDataHoraFormatada() {
        return getDataHoraAgendamento() != null
                ? getDataHoraAgendamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                : null;
    }
}

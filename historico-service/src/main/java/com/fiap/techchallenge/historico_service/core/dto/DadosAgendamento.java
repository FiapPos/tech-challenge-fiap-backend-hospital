package com.fiap.techchallenge.historico_service.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    private long usuarioId;
    private long hospitalId;
    private long medicoId;
    private long agendamentoId;

    private String nomePaciente;
    private String nomeMedico;
    private String nomeHospital;
    private String enderecoHospital;
    private String especializacao;
    private LocalDateTime dataHoraAgendamento;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public DadosAgendamento(long usuarioId,
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
        this.usuarioId = usuarioId;
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

    public String getDataHoraFormatada() {
        return getDataHoraAgendamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}

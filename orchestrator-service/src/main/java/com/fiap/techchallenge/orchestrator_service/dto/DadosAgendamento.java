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


    public String getDataHoraFormatada() {
        return getDataHoraAgendamento() != null
                ? getDataHoraAgendamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                : null;
    }


    public void atualiza(EspecialidadeDTO especialidade,
                         UsuarioDTO medico, HospitalDTO hospital,
                         LocalDateTime dataHora,
                         String observacoes,
                         UsuarioDTO paciente) {
        this.especialidadeId = especialidade.getId();
        this.medicoId = medico.getId();
        this.hospitalId = hospital.getId();
        this.pacienteId = paciente.getId();
        this.nomeHospital = hospital.getNome();
        this.nomeMedico = medico.getNome();
        this.nomePaciente = paciente.getNome();
        this.enderecoHospital = hospital.getEndereco();
        this.especializacao = especialidade.getNome();
        this.dataHoraAgendamento = dataHora;
        if (observacoes != null) this.observacoes = observacoes;
    }
}

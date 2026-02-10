package com.fiap.techchallenge.agendamento_service.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fiap.techchallenge.agendamento_service.core.dto.DadosAgendamento;
import com.fiap.techchallenge.agendamento_service.core.enums.EStatusAgendamento;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.fiap.techchallenge.agendamento_service.core.enums.EStatusAgendamento.CANCELADA;
import static com.fiap.techchallenge.agendamento_service.core.enums.EStatusAgendamento.CRIADA;

@Data
@NoArgsConstructor
@Entity
@Table(name = "consultas")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long pacienteId;

    @Column(nullable = false)
    private Long medicoId;

    @Column(nullable = false)
    private Long especialidadeId;

    @Column(nullable = false)
    private Long hospitalId;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EStatusAgendamento status;

    private String nomePaciente;
    private String nomeMedico;
    private String nomeHospital;
    private String enderecoHospital;
    private String especializacao;
    private String observacoes;

    @Column(nullable = false)
    private LocalDateTime criadoEm;

    private LocalDateTime atualizadoEm;

    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        atualizadoEm = LocalDateTime.now();
    }

    public void atualiza(DadosAgendamento dto) {
        this.medicoId = dto.getMedicoId();
        this.especialidadeId = dto.getEspecialidadeId() ;
        this.hospitalId = dto.getHospitalId();
        this.dataHora = dto.getDataHoraAgendamento() != null ? dto.getDataHoraAgendamento() : this.dataHora;
        this.nomePaciente = dto.getNomePaciente() != null ? dto.getNomePaciente() : this.nomePaciente;
        this.nomeMedico = dto.getNomeMedico() != null ? dto.getNomeMedico() : this.nomeMedico;
        this.nomeHospital = dto.getNomeHospital() != null ? dto.getNomeHospital() : this.nomeHospital;
        this.enderecoHospital = dto.getEnderecoHospital() != null ? dto.getEnderecoHospital() : this.enderecoHospital;
        this.especializacao = dto.getEspecializacao() != null ? dto.getEspecializacao() : this.especializacao;
        this.observacoes = dto.getObservacoes() != null ? dto.getObservacoes() : this.observacoes;
        this.status = EStatusAgendamento.ATUALIZADA;
    }

    @JsonIgnore
    public String getTemplateDeMensagem() {
        return String.format(
                "• Consulta com %s na especialidade %s em %s, no endereço %s em %s",
                getNomeMedico(),
                getEspecializacao(),
                getNomeHospital(),
                getEnderecoHospital(),
                getDataHoraFormatada()
        );
    }

    public String getDataHoraFormatada() {
        return getDataHora() != null
                ? getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                : null;
    }

    public boolean isCriada() {
        return CRIADA.equals(getStatus());
    }

    public boolean isCancelada() {
        return CANCELADA.equals(getStatus());
    }
}
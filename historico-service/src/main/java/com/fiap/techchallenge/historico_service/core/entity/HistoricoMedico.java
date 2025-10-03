package com.fiap.techchallenge.historico_service.core.entity;

import com.fiap.techchallenge.historico_service.core.enums.EStatusAgendamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "historico_medico")
public class HistoricoMedico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long agendamentoId;

    @Column(nullable = false)
    private Long pacienteId;

    @Column(nullable = false)
    private Long hospitalId;

    @Column(nullable = false)
    private Long medicoId;

    @Column(nullable = false)
    private String nomePaciente;

    @Column(nullable = false)
    private String nomeMedico;

    @Column(nullable = false)
    private String nomeHospital;

    private String enderecoHospital;

    private String especializacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EStatusAgendamento statusAgendamento;

    @Column(nullable = false)
    private LocalDateTime dataHoraAgendamento;

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
}

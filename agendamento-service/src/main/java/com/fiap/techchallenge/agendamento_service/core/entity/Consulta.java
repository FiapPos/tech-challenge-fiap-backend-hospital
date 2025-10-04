package com.fiap.techchallenge.agendamento_service.core.entity;
import com.fiap.techchallenge.agendamento_service.core.dto.DadosAgendamento;
import com.fiap.techchallenge.agendamento_service.core.enums.EStatusAgendamento;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    @Column(nullable = false)
    private EStatusAgendamento status;

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
        this.especialidadeId = dto.getEspecialidadeId();
        this.hospitalId = dto.getHospitalId();
        this.dataHora = LocalDateTime.now();
        this.status = EStatusAgendamento.ATUALIZADA;
        this.atualizadoEm = LocalDateTime.now();
    }
}
package com.fiap.techchallenge.agendamento_service.core.entity;

import com.fiap.techchallenge.agendamento_service.core.enums.EStatusFilaEspera;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "fila_espera")
public class FilaEspera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long pacienteId;

    @Column(nullable = false)
    private Long especialidadeId;

    @Column(nullable = false)
    private Long hospitalId;

    @Column
    private Long medicoId;

    @Column(nullable = false)
    private LocalDateTime dataHoraDesejadaInicio;

    @Column(nullable = false)
    private LocalDateTime dataHoraDesejadaFim;

    @Column(nullable = false)
    private Boolean idoso = false;

    @Column(nullable = false)
    private Boolean gestante = false;

    @Column(nullable = false)
    private Boolean pcd = false;

    @Column(nullable = false)
    private Integer pesoPrioridade = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EStatusFilaEspera status;

    @Column(nullable = false)
    private LocalDateTime criadoEm;

    private LocalDateTime atualizadoEm;

    private LocalDateTime notificadoEm;

    @Column
    private String nomePaciente;

    @Column
    private String emailPaciente;

    @Column
    private String telegramId;

    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDateTime.now();
        calcularPesoPrioridade();
        if (status == null) {
            status = EStatusFilaEspera.AGUARDANDO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        atualizadoEm = LocalDateTime.now();
    }

    public void calcularPesoPrioridade() {
        int peso = 0;
        if (Boolean.TRUE.equals(pcd)) peso += 3;
        if (Boolean.TRUE.equals(idoso)) peso += 2;
        if (Boolean.TRUE.equals(gestante)) peso += 1;
        this.pesoPrioridade = peso;
    }

    public boolean isPrioritario() {
        return Boolean.TRUE.equals(idoso) || Boolean.TRUE.equals(gestante) || Boolean.TRUE.equals(pcd);
    }
}

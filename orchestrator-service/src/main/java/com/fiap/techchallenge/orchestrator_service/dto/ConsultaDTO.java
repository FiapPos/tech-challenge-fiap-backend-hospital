package com.fiap.techchallenge.orchestrator_service.dto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaDTO {

    private Long id; // Será nulo ao criar, e preenchido na resposta
    private Long pacienteId;
    private Long medicoId;
    private LocalDateTime dataHora;
    private String status;

    public ConsultaDTO(Long pacienteId, Long medicoId, LocalDateTime dataHora) {
        this.pacienteId = pacienteId;
        this.medicoId = medicoId;
        this.dataHora = dataHora;
        this.status = "PENDENTE"; // Status inicial padrão
    }
}
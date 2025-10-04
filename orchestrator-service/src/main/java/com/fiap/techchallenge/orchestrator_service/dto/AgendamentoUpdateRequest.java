package com.fiap.techchallenge.orchestrator_service.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoUpdateRequest {
    private Long pacienteId;
    private Long hospitalId;
    private Long medicoId;
    private Long especialidadeId;
    private LocalDateTime dataHora;
    private String observacoes;
}

package com.fiap.techchallenge.orchestrator_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgendamentoResponse {
    private String appointmentId;
    private String patientId;
    private String dataHora;
    private String servicoId;
    private String sagaId;
    private String status;
    private String atualizadoEm;
}

package com.fiap.techchallenge.orchestrator_service.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class SagaEventoDto {
    private Long id;
    private String sagaId;
    private String eventType;
    private String payload;
    private String timestamp;
}

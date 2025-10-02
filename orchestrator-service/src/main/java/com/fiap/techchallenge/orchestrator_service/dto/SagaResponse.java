package com.fiap.techchallenge.orchestrator_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SagaResponse {

    private boolean sucesso;
    private String mensagem;
    private Long consultaId;
}
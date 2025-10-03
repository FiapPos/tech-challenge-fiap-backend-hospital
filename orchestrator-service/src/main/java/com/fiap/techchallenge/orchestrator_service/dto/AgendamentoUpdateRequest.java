package com.fiap.techchallenge.orchestrator_service.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoUpdateRequest {
    private String dataHora;
    private String servicoId;
    private String observacoes;
}

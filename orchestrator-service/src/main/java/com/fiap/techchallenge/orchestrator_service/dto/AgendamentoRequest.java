package com.fiap.techchallenge.orchestrator_service.dto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoRequest {

    private Long pacienteId;
    private Long medicoId;
    private LocalDateTime dataHora;

}
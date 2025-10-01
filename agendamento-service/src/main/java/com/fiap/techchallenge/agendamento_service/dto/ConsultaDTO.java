package com.fiap.techchallenge.agendamento_service.dto;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ConsultaDTO {
    private Long id;
    private Long pacienteId;
    private Long medicoId;
    private LocalDateTime dataHora;
    private String status;
}
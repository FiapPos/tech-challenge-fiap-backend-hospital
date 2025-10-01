package com.fiap.techchallenge.agendamento_service.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoConsultaDTO {
    private Long consultaId;
    private Long pacienteId;
    private LocalDateTime dataHora;
    private String tipoEvento; // "CRIADA", "ATUALIZADA", "CANCELADA"
}

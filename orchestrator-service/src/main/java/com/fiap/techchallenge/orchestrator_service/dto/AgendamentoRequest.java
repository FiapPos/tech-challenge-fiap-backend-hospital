package com.fiap.techchallenge.orchestrator_service.dto;
<<<<<<< HEAD

=======
>>>>>>> origin/main
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
<<<<<<< HEAD
=======
    private Long hospitalId;
    private Long especialidadeId;
>>>>>>> origin/main
    private LocalDateTime dataHora;

}
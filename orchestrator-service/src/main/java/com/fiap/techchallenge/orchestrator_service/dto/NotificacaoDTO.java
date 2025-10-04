package com.fiap.techchallenge.orchestrator_service.dto;
<<<<<<< HEAD

=======
>>>>>>> origin/main
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class NotificacaoDTO {

    private Long destinatarioId; // ID do paciente para quem a notificação será enviada
    private String mensagem;
}
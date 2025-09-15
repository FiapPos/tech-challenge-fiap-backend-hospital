package com.fiap.techchallenge.notificacao_service.core.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record NotificacaoAgendamento(
        String nome,
        LocalDateTime dataHora,
        String especializacao,
        BigDecimal valor
) {
}

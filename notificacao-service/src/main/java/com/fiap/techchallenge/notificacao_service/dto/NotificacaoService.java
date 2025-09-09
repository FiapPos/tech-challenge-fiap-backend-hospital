package com.fiap.techchallenge.notificacao_service.dto;

public record NotificacaoService(
        Long id,
        String produto,
        int quantidade,
        Double price
) {
}

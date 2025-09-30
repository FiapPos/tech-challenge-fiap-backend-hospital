package com.fiap.techchallenge.usuario_service.core.exceptions;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final String mensagem;

    public ErrorResponse(String mensagem) {
        this.mensagem = mensagem;
    }
}
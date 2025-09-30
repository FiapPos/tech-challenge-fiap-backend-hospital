package com.fiap.techchallenge.usuario_service.core.exceptions;

public class UsuarioBusinessException extends RuntimeException {

    public UsuarioBusinessException(String message) {
        super(message);
    }

    public UsuarioBusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}

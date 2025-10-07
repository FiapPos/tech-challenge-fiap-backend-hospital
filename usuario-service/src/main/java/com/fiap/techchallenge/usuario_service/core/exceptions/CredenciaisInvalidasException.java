package com.fiap.techchallenge.usuario_service.core.exceptions;

public class CredenciaisInvalidasException extends RuntimeException {
    public CredenciaisInvalidasException() {
        super("Usuário, senha ou tipo inválidos.");
    }
}
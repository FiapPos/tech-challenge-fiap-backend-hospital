package com.fiap.techchallenge.usuario_service.core.exceptions;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
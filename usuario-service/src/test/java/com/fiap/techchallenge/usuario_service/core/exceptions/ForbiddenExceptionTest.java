package com.fiap.techchallenge.usuario_service.core.exceptions;

import org.junit.jupiter.api.Test;
import com.fiap.techchallenge.usuario_service.core.exceptions.ForbiddenException;

import static org.junit.jupiter.api.Assertions.*;

class ForbiddenExceptionTest {
    @Test
    void deveRetornarMensagemCorreta() {
        String mensagem = "acesso negado";
        ForbiddenException exception = new ForbiddenException(mensagem);
        assertEquals(mensagem, exception.getMessage());
    }
}
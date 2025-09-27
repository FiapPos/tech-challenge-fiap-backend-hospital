package br.com.fiap.techchallenge.usuario_service.core.exceptions;

import org.junit.jupiter.api.Test;
import br.com.fiap.techchallenge.core.exceptions.BadRequestException;

import static org.junit.jupiter.api.Assertions.*;

class BadRequestExceptionTest {
    @Test
    void deveRetornarMensagemCorreta() {
        String mensagem = "mensagem de erro";
        BadRequestException exception = new BadRequestException(mensagem);
        assertEquals(mensagem, exception.getMessage());
    }
}
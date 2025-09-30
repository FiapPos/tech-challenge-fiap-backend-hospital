package br.com.fiap.techchallenge.usuario_service.core.exceptions;

import org.junit.jupiter.api.Test;
import br.com.fiap.techchallenge.core.exceptions.ErrorResponse;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {
    @Test
    void deveRetornarMensagemCorreta() {
        String mensagem = "erro genérico";
        ErrorResponse errorResponse = new ErrorResponse(mensagem);
        assertEquals(mensagem, errorResponse.getMensagem());
    }
}
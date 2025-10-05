package com.fiap.techchallenge.appointment_service.core.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Fabrica os bodies JSON de erro para respostas de autenticação/authorization.
 * Isola a construção do mapa para facilitar reutilização e testes.
 */
@Component
public class GeraCorpoErroAutenticacao {

    public Map<String, Object> construirNaoAutorizado(HttpServletRequest request,
            AuthenticationException authException) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", authException.getMessage());
        body.put("path", request.getServletPath());
        return body;
    }
}

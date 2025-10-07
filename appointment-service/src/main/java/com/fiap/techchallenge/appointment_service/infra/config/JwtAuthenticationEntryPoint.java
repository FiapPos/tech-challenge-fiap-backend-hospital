package com.fiap.techchallenge.appointment_service.infra.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final com.fiap.techchallenge.appointment_service.core.utils.GeraCorpoErroAutenticacao bodyFactory;
    private final ObjectMapper objectMapper;

    public JwtAuthenticationEntryPoint(
            com.fiap.techchallenge.appointment_service.core.utils.GeraCorpoErroAutenticacao bodyFactory,
            ObjectMapper objectMapper) {
        this.bodyFactory = bodyFactory;
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        log.error("Erro de autenticação não autorizada: {}", authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final Map<String, Object> body = bodyFactory.construirNaoAutorizado(request, authException);

        objectMapper.writeValue(response.getOutputStream(), body);
    }
}

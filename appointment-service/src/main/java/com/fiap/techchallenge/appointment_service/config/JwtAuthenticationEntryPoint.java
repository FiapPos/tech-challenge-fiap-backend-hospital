package com.fiap.techchallenge.appointment_service.config;

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

    private final com.fiap.techchallenge.appointment_service.core.util.FabricaCorpoErroAutenticacao bodyFactory;

    public JwtAuthenticationEntryPoint(com.fiap.techchallenge.appointment_service.core.util.FabricaCorpoErroAutenticacao bodyFactory) {
        this.bodyFactory = bodyFactory;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        log.error("Erro de autenticação não autorizada: {}", authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    final Map<String, Object> body = bodyFactory.construirNaoAutorizado(request, authException);

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}
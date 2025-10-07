package com.fiap.techchallenge.appointment_service.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge.appointment_service.core.client.usuario.UsuarioClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioService {

        private final UsuarioClient usuarioClient;

        @SuppressWarnings({ "unchecked" })
        public java.util.Map<String, Object> loginCredentials(java.util.Map<String, Object> credentials) {
                ResponseEntity<Object> resp = usuarioClient.loginOnUsuarioService(credentials);
                if (resp == null || resp.getStatusCode().isError() || resp.getBody() == null) {
                        throw new RuntimeException("Failed to login on usuario-service");
                }

                Object body = resp.getBody();
                if (body instanceof java.util.Map) {
                        return (java.util.Map<String, Object>) body;
                }

                ObjectMapper mapper = new ObjectMapper();
                return mapper.convertValue(body, java.util.Map.class);
        }
}

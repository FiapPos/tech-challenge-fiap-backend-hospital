package com.fiap.techchallenge.appointment_service.core.controller;

import com.fiap.techchallenge.appointment_service.core.client.OrchestratorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;

import com.fiap.techchallenge.appointment_service.core.client.OrchestratorClient;
import com.fiap.techchallenge.appointment_service.core.dto.CriarUsuarioComandoDto;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Slf4j
public class UsuarioController {

    private final OrchestratorClient orchestratorClient;

    @PostMapping
    public ResponseEntity<Object> criarUsuario(HttpServletRequest request,
            @Valid @RequestBody CriarUsuarioComandoDto payload) {
        log.info("Encaminhando criação de usuário para o orchestrator-service");
        HttpHeaders headers = buildForwardHeaders(request);
        ResponseEntity<Object> response = orchestratorClient.createUsuarioWithHeaders(payload, headers);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    private HttpHeaders buildForwardHeaders(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        Object profileAttr = request.getAttribute("X-User-Profile");
        if (profileAttr != null) {
            headers.add("X-User-Profile", profileAttr.toString());
        }
        Object usernameAttr = request.getAttribute("X-Username");
        if (usernameAttr != null) {
            headers.add("X-Username", usernameAttr.toString());
        }
        String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (auth != null) {
            headers.add(HttpHeaders.AUTHORIZATION, auth);
        }
        return headers;
    }
}

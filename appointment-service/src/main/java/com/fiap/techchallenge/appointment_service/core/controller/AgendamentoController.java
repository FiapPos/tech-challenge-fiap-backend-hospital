package com.fiap.techchallenge.appointment_service.core.controller;

import com.fiap.techchallenge.appointment_service.core.client.OrchestratorClient;
import com.fiap.techchallenge.appointment_service.core.client.OrchestratorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/agendamento")
@RequiredArgsConstructor
@Slf4j
public class AgendamentoController {

    private final OrchestratorClient orchestratorClient;

    @PostMapping("/criacao")
    public ResponseEntity<Object> criarAgendamento(HttpServletRequest request, @RequestBody Object payload) {
        log.info("Proxy: criando agendamento");
        HttpHeaders headers = buildForwardHeaders(request);
        ResponseEntity<Object> resp = orchestratorClient.createAgendamentoWithHeaders(payload, headers);
        return ResponseEntity.status(resp.getStatusCode()).body(resp.getBody());
    }

    @PutMapping("/edicao/{id}")
    public ResponseEntity<Object> editarAgendamento(HttpServletRequest request, @PathVariable Long id,
            @RequestBody Object payload) {
        log.info("Proxy: editando agendamento {}", id);
        HttpHeaders headers = buildForwardHeaders(request);
        ResponseEntity<Object> resp = orchestratorClient.updateAgendamentoWithHeaders(id, payload, headers);
        return ResponseEntity.status(resp.getStatusCode()).body(resp.getBody());
    }

    private HttpHeaders buildForwardHeaders(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        Object profileAttr = request.getAttribute("X-User-Profile");
        if (profileAttr != null)
            headers.add("X-User-Profile", profileAttr.toString());
        Object usernameAttr = request.getAttribute("X-Username");
        if (usernameAttr != null)
            headers.add("X-Username", usernameAttr.toString());
        String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (auth != null)
            headers.add(HttpHeaders.AUTHORIZATION, auth);
        return headers;
    }
}

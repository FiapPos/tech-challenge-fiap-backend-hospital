package com.fiap.techchallenge.appointment_service.core.controller;

import com.fiap.techchallenge.appointment_service.core.client.OrchestratorClient;
import org.springframework.security.access.prepost.PreAuthorize;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fiap.techchallenge.appointment_service.core.dto.AgendamentoRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/agendamento")
@RequiredArgsConstructor
@Slf4j
public class AgendamentoController {

    private final OrchestratorClient orchestratorClient;
    private final com.fiap.techchallenge.appointment_service.core.mapper.AgendamentoMapper agendamentoMapper;

    @PostMapping("/criacao")
    @PreAuthorize("hasAnyRole('MEDICO','ENFERMEIRO')")
    public ResponseEntity<Object> criarAgendamento(HttpServletRequest request,
            @Valid @RequestBody AgendamentoRequest payload) {

        log.info("Proxy: criando agendamento (secured)");

        // Converte o DTO validado para o formato esperado pelo saga/orchestrator
        ObjectNode sagaPayload = agendamentoMapper.toSagaPayload(payload);

        HttpHeaders headers = montarCabecalhosEncaminhamento(request);
        ResponseEntity<Object> resp = orchestratorClient.createAgendamentoSaga(sagaPayload, headers);

        return ResponseEntity.status(resp.getStatusCode()).body(resp.getBody());
    }

    private HttpHeaders montarCabecalhosEncaminhamento(HttpServletRequest request) {
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

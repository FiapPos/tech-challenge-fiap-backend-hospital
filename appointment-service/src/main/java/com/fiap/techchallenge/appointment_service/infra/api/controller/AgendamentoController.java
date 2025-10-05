package com.fiap.techchallenge.appointment_service.infra.api.controller;

import com.fiap.techchallenge.appointment_service.core.client.orchestrator.OrchestratorClient;
import org.springframework.security.access.prepost.PreAuthorize;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fiap.techchallenge.appointment_service.core.dto.request.AgendamentoRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agendamento")
@RequiredArgsConstructor
@Slf4j
public class AgendamentoController {

    private final OrchestratorClient orchestratorClient;
    private final com.fiap.techchallenge.appointment_service.core.mapper.AgendamentoMapper agendamentoMapper;

    @PostMapping("/criacao")
    @PreAuthorize("hasAnyRole('MEDICO','ENFERMEIRO')")
    public ResponseEntity<Object> criarAgendamento(@Valid @RequestBody AgendamentoRequest payload) {
        log.info("Proxy: criando agendamento (secured)");

        // Converte DTO validado para o payload esperado pelo saga/orchestrator
        ObjectNode sagaPayload = agendamentoMapper.toSagaPayload(payload);

        ResponseEntity<Object> resp = orchestratorClient.createAgendamento(sagaPayload);

        return ResponseEntity.status(resp.getStatusCode()).body(resp.getBody());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MEDICO')")
    public ResponseEntity<Object> editarAgendamento(@PathVariable("id") String id,
            @RequestBody ObjectNode payload) {
        log.info("Proxy: editar agendamento id={} (secured)", id);

        ResponseEntity<Object> resp = orchestratorClient.updateAgendamento(id, payload);

        return ResponseEntity.status(resp.getStatusCode()).body(resp.getBody());
    }
}

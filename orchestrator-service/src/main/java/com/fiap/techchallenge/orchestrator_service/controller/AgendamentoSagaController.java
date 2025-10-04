package com.fiap.techchallenge.orchestrator_service.controller;
import com.fiap.techchallenge.orchestrator_service.dto.*;
import com.fiap.techchallenge.orchestrator_service.enums.EStatusAgendamento;
import com.fiap.techchallenge.orchestrator_service.service.AgendamentoSagaService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/saga/agendamentos" )
public class AgendamentoSagaController {

    @Autowired
    private AgendamentoSagaService sagaService;

    @PostMapping
    @Transactional
    public ResponseEntity<SagaResponse> iniciarSagaAgendamento(@RequestBody AgendamentoRequest request) {
        SagaResponse response = sagaService.iniciarSaga(request);
        return response.isSucesso() ? ResponseEntity.ok(response) : ResponseEntity.status(500).body(response);
    }

    @PutMapping("/{agendamentoId}")
    public ResponseEntity<SagaResponse> editarAgendamento(
            @PathVariable String agendamentoId,
            @RequestBody AgendamentoUpdateRequest request) {
        SagaResponse response = sagaService.editarSaga(agendamentoId, request);
        return response.isSucesso() ? ResponseEntity.ok(response) : ResponseEntity.status(500).body(response);
    }

    @PutMapping("/{agendamentoId}/cancelar")
    public ResponseEntity<HttpStatus> cancelarAgendamento(@PathVariable String agendamentoId) {
        sagaService.cancelarSaga(agendamentoId);
        return ResponseEntity.ok().build();
    }
}
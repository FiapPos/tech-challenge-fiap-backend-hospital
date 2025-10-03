package com.fiap.techchallenge.orchestrator_service.controller;
import com.fiap.techchallenge.orchestrator_service.dto.*;
import com.fiap.techchallenge.orchestrator_service.service.AgendamentoSagaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/saga/agendamentos" )
public class AgendamentoSagaController {

    @Autowired
    private AgendamentoSagaService sagaService;

    @PostMapping
    public ResponseEntity<SagaResponse> iniciarSagaAgendamento(@RequestBody AgendamentoRequest request) {
        SagaResponse response = sagaService.iniciarSaga(request);
        return response.isSucesso() ? ResponseEntity.ok(response) : ResponseEntity.status(500).body(response);
    }

    @PutMapping("/{appointmentId}")
    public ResponseEntity<SagaResponse> editarAgendamento(
            @PathVariable String appointmentId,
            @RequestBody AgendamentoUpdateRequest request) {
        SagaResponse response = sagaService.editarSaga(appointmentId, request);
        return response.isSucesso() ? ResponseEntity.ok(response) : ResponseEntity.status(500).body(response);
    }

    @GetMapping
    public ResponseEntity<List<AgendamentoResponse>> listarAgendamentos(
            @RequestParam(required = false) String patientId,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(sagaService.listarAgendamentos(patientId, status));
    }

    @GetMapping("/{appointmentId}")
    public ResponseEntity<AgendamentoResponse> buscarAgendamento(@PathVariable String appointmentId) {
        AgendamentoResponse resp = sagaService.buscarAgendamento(appointmentId);
        return resp == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(resp);
    }

    @GetMapping("/{appointmentId}/history")
    public ResponseEntity<List<SagaEventoDto>> historico(@PathVariable String appointmentId) {
        return ResponseEntity.ok(sagaService.buscarHistoricoAgendamento(appointmentId));
    }
}
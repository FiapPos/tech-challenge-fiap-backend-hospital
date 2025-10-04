package com.fiap.techchallenge.orchestrator_service.controller;
<<<<<<< HEAD

=======
>>>>>>> origin/main
import com.fiap.techchallenge.orchestrator_service.dto.AgendamentoRequest;
import com.fiap.techchallenge.orchestrator_service.dto.SagaResponse;
import com.fiap.techchallenge.orchestrator_service.service.AgendamentoSagaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
<<<<<<< HEAD
@RequestMapping("/api/saga/agendamentos")
=======
@RequestMapping("/api/saga/agendamentos" )
>>>>>>> origin/main
public class AgendamentoSagaController {

    @Autowired
    private AgendamentoSagaService sagaService;

    @PostMapping
    public ResponseEntity<SagaResponse> iniciarSagaAgendamento(@RequestBody AgendamentoRequest request) {
        SagaResponse response = sagaService.iniciarSaga(request);
        if (response.isSucesso()) {
            return ResponseEntity.ok(response);
        }
        // Retorna um status de erro se a saga falhou
        return ResponseEntity.status(500).body(response);
    }
}
package com.fiap.techchallenge.agendamento_service.core.controller;

import com.fiap.techchallenge.agendamento_service.core.dto.DadosAgendamento;
import com.fiap.techchallenge.agendamento_service.core.entity.Consulta;
import com.fiap.techchallenge.agendamento_service.core.service.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/consultas" )
public class ConsultaController {

    @Autowired
    private ConsultaService service;

    // Endpoint para criar a consulta (chamado pelo orquestrador)
    @PostMapping
    public ResponseEntity<DadosAgendamento> criarConsultaPendente(@RequestBody DadosAgendamento dto) {
        Consulta consultaSalva = service.criarConsultaPendente(dto);

        // Mapeia a entidade salva de volta para um DTO para a resposta
        dto.setAgendamentoId(consultaSalva.getId());
        dto.setStatusAgendamento(consultaSalva.getStatus());

        return ResponseEntity.ok(dto);
    }

    // Endpoint solicitado pela Sonia para atualizar a consulta
    @PutMapping("/{id}")
    public ResponseEntity<DadosAgendamento> atualizarConsulta(@PathVariable Long id, @RequestBody DadosAgendamento dto) {
        Consulta consultaAtualizada = service.atualizarConsulta(id, dto);
        DadosAgendamento responseDto = new DadosAgendamento(consultaAtualizada);
        return ResponseEntity.ok(responseDto);
    }

    // Endpoint de compensação (chamado pelo orquestrador em caso de falha)
    @DeleteMapping("/cancelar")
    public ResponseEntity<Void> cancelarConsulta(@RequestBody DadosAgendamento dto) {
        service.cancelarConsulta(dto);
        return ResponseEntity.noContent().build();
    }

    // Endpoint de sucesso final (chamado pelo orquestrador)
    @PutMapping("/confirmar")
    public ResponseEntity<Void> confirmarConsulta(@RequestBody DadosAgendamento dto) {
        service.confirmarConsulta(dto);
        return ResponseEntity.noContent().build();
    }
}

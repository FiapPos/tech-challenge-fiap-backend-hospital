package com.fiap.techchallenge.agendamento_service.core.controller;

import com.fiap.techchallenge.agendamento_service.core.dto.DadosAgendamento;
import com.fiap.techchallenge.agendamento_service.core.entity.Consulta;
import com.fiap.techchallenge.agendamento_service.core.service.ConsultaService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/consultas" )
public class ConsultaController {

    @Autowired
    private ConsultaService service;

    @PostMapping
    public ResponseEntity<DadosAgendamento> criarConsultaPendente(@RequestBody DadosAgendamento dto) {
        Consulta consultaSalva = service.criarConsultaPendente(dto);

        dto.setAgendamentoId(consultaSalva.getId());
        dto.setStatusAgendamento(consultaSalva.getStatus());

        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/cancelar")
    public ResponseEntity<Void> cancelarConsulta(@RequestBody DadosAgendamento dto) {
        service.cancelarConsulta(dto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/confirmar")
    public ResponseEntity<Void> confirmarConsulta(@RequestBody DadosAgendamento dto) {
        service.confirmarConsulta(dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosAgendamento> buscarConsulta(@PathVariable Long id) {
        Consulta consulta = service.buscaConsulta(id);
        return ResponseEntity.ok(new DadosAgendamento(consulta));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DadosAgendamento> atualizarConsulta(@PathVariable Long id, @RequestBody DadosAgendamento dto) {
        return ResponseEntity.ok(service.atualizarConsulta(id, dto));
    }
}

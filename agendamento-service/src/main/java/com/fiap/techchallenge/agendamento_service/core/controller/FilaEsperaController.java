package com.fiap.techchallenge.agendamento_service.core.controller;

import com.fiap.techchallenge.agendamento_service.core.dto.FilaEsperaDTO;
import com.fiap.techchallenge.agendamento_service.core.service.FilaEsperaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fila-espera")
@RequiredArgsConstructor
public class FilaEsperaController {

    private final FilaEsperaService filaEsperaService;

    @PostMapping
    public ResponseEntity<FilaEsperaDTO> adicionarNaFila(@RequestBody FilaEsperaDTO dto) {
        return ResponseEntity.ok(filaEsperaService.adicionarNaFila(dto));
    }

    @GetMapping("/prioritarios")
    public ResponseEntity<List<FilaEsperaDTO>> buscarPacientesPrioritarios(
            @RequestParam Long especialidadeId,
            @RequestParam Long hospitalId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataHora) {
        return ResponseEntity.ok(filaEsperaService.buscarPacientesPrioritarios(especialidadeId, hospitalId, dataHora));
    }

    @GetMapping("/prioritarios/medico/{medicoId}")
    public ResponseEntity<List<FilaEsperaDTO>> buscarPacientesPrioritariosComMedico(
            @PathVariable Long medicoId,
            @RequestParam Long especialidadeId,
            @RequestParam Long hospitalId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataHora) {
        return ResponseEntity.ok(filaEsperaService.buscarPacientesPrioritariosComMedico(especialidadeId, hospitalId, medicoId, dataHora));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilaEsperaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(filaEsperaService.buscarPorId(id));
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<FilaEsperaDTO>> buscarFilaPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(filaEsperaService.buscarFilaPorPaciente(pacienteId));
    }

    @PutMapping("/{id}/aceitar")
    public ResponseEntity<FilaEsperaDTO> aceitarProposta(@PathVariable Long id) {
        return ResponseEntity.ok(filaEsperaService.aceitarProposta(id));
    }

    @PutMapping("/{id}/recusar")
    public ResponseEntity<FilaEsperaDTO> recusarProposta(@PathVariable Long id) {
        return ResponseEntity.ok(filaEsperaService.recusarProposta(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerDaFila(@PathVariable Long id) {
        filaEsperaService.removerDaFila(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/notificar")
    public ResponseEntity<FilaEsperaDTO> notificarPaciente(
            @PathVariable Long id,
            @RequestParam Long medicoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataHora,
            @RequestParam(required = false) String nomeMedico,
            @RequestParam(required = false) String nomeHospital) {
        return ResponseEntity.ok(filaEsperaService.notificarPacienteManualmente(id, medicoId, dataHora, nomeMedico, nomeHospital));
    }

    @PostMapping("/alocar")
    public ResponseEntity<Map<String, Object>> alocarPacientes() {
        int alocados = filaEsperaService.alocarProximosDaFila();
        return ResponseEntity.ok(Map.of("mensagem", "Alocação concluída", "pacientesAlocados", alocados));
    }
}

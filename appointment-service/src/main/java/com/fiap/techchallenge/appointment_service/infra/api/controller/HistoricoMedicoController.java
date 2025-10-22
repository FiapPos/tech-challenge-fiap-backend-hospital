package com.fiap.techchallenge.appointment_service.infra.api.controller;

import com.fiap.techchallenge.appointment_service.core.dto.response.HistoricoMedicoDto;
import com.fiap.techchallenge.appointment_service.core.service.HistoricoService;
import com.fiap.techchallenge.appointment_service.infra.security.ValidarPaciente;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/historico")
@RequiredArgsConstructor
@Slf4j
public class HistoricoMedicoController {

    private final HistoricoService historicoIntegrationService;
    private final ValidarPaciente validarPaciente;

    @GetMapping("/paciente/{id}")
    @PreAuthorize("hasAnyRole('PROFESSOR', 'ESTUDANTE', 'COORDENADOR')")
    public ResponseEntity<List<HistoricoMedicoDto>> historicoPaciente(@PathVariable("id") Long pacienteId) {
        log.info("Proxy: buscando historico do estudante id={}", pacienteId);
        validarPaciente.validar(pacienteId);

        List<HistoricoMedicoDto> dados = historicoIntegrationService.buscarTodosAtendimentosPaciente(pacienteId);
        return ResponseEntity.ok(dados);
    }

    @GetMapping("/paciente/{id}/futuros")
    @PreAuthorize("hasAnyRole('PROFESSOR', 'ESTUDANTE', 'COORDENADOR')")
    public ResponseEntity<List<HistoricoMedicoDto>> atendimentosFuturosPaciente(@PathVariable("id") Long pacienteId) {
        log.info("Proxy: buscando atendimentos futuros do estudante id={}", pacienteId);

        validarPaciente.validar(pacienteId);

        List<HistoricoMedicoDto> dados = historicoIntegrationService.buscarAtendimentosFuturosPaciente(pacienteId);
        return ResponseEntity.ok(dados);
    }

    @GetMapping("/medico/{id}")
    @PreAuthorize("hasAnyRole('PROFESSOR', 'COORDENADOR')")
    public ResponseEntity<List<HistoricoMedicoDto>> atendimentosPorMedico(@PathVariable("id") Long medicoId) {
        log.info("Proxy: buscando atendimentos do professor id={}", medicoId);

        List<HistoricoMedicoDto> dados = historicoIntegrationService.buscarAtendimentosPorMedico(medicoId);
        return ResponseEntity.ok(dados);
    }

    @GetMapping("/medico/{id}/futuros")
    @PreAuthorize("hasAnyRole('PROFESSOR', 'COORDENADOR')")
    public ResponseEntity<List<HistoricoMedicoDto>> atendimentosFuturosPorMedico(@PathVariable("id") Long medicoId) {
        log.info("Proxy: buscando atendimentos futuros do professor id={}", medicoId);

        List<HistoricoMedicoDto> dados = historicoIntegrationService.buscarAtendimentosFuturosPorMedico(medicoId);
        return ResponseEntity.ok(dados);
    }
}

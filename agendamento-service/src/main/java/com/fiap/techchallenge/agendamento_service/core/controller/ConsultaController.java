package com.fiap.techchallenge.agendamento_service.core.controller;

import com.fiap.techchallenge.agendamento_service.core.client.UsuarioServiceClient;
import com.fiap.techchallenge.agendamento_service.core.dto.DadosAgendamento;
import com.fiap.techchallenge.agendamento_service.core.dto.UsuarioDTO;
import com.fiap.techchallenge.agendamento_service.core.entity.Consulta;
import com.fiap.techchallenge.agendamento_service.core.repository.ConsultaRepository;
import com.fiap.techchallenge.agendamento_service.core.service.ConsultaService;
import com.fiap.techchallenge.agendamento_service.core.service.SusAgendamentoBotService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static com.fiap.techchallenge.agendamento_service.core.enums.EStatusAgendamento.PENDENTE;

@RestController
@RequestMapping("/api/consultas")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaService service;
    private final UsuarioServiceClient usuarioServiceClient;
    private final ConsultaRepository consultaRepository;
    private final SusAgendamentoBotService susAgendamentoBotService;

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

    @GetMapping("/disponibilidade")
    public ResponseEntity<Map<String, Object>> verificarDisponibilidade(
            @RequestParam Long medicoId,
            @RequestParam Long pacienteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataHora) {
        boolean disponivel = service.isHorarioDisponivel(medicoId, pacienteId, dataHora);
        return ResponseEntity.ok(Map.of(
                "disponivel", disponivel,
                "medicoId", medicoId,
                "pacienteId", pacienteId,
                "dataHora", dataHora.toString()
        ));
    }

    @GetMapping("/hospital/{hospitalId}")
    public ResponseEntity<List<DadosAgendamento>> listarConsultasPorHospital(@PathVariable Long hospitalId) {
        return ResponseEntity.ok(service.listarConsultasPorHospital(hospitalId).stream()
                .map(DadosAgendamento::new)
                .toList());
    }

    @GetMapping("/hospital/{hospitalId}/data")
    public ResponseEntity<List<DadosAgendamento>> listarConsultasPorHospitalEData(
            @PathVariable Long hospitalId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        LocalDateTime dataInicio = data.atStartOfDay();
        LocalDateTime dataFim = data.atTime(LocalTime.MAX);
        return ResponseEntity.ok(service.listarConsultasPorHospitalEData(hospitalId, dataInicio, dataFim).stream()
                .map(DadosAgendamento::new)
                .toList());
    }

    @GetMapping("/hospital/{hospitalId}/especialidade/{especialidadeId}")
    public ResponseEntity<List<DadosAgendamento>> listarConsultasPorHospitalEEspecialidade(
            @PathVariable Long hospitalId,
            @PathVariable Long especialidadeId) {
        return ResponseEntity.ok(service.listarConsultasPorHospitalEEspecialidade(hospitalId, especialidadeId).stream()
                .map(DadosAgendamento::new)
                .toList());
    }

    @GetMapping("/medico/{medicoId}/data")
    public ResponseEntity<List<DadosAgendamento>> listarConsultasPorMedicoEData(
            @PathVariable Long medicoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        LocalDateTime dataInicio = data.atStartOfDay();
        LocalDateTime dataFim = data.atTime(LocalTime.MAX);
        return ResponseEntity.ok(service.listarConsultasPorMedicoEData(medicoId, dataInicio, dataFim).stream()
                .map(DadosAgendamento::new)
                .toList());
    }

/**
 * Endpoint para teste de fluxo completo.
 */
    @GetMapping("/teste")
    public ResponseEntity<String> teste() {

        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime amanhaMesmaHora = agora.plusHours(24);

        consultaRepository.buscarConsultasNasProximas24Horas(
                PENDENTE,
                agora,
                amanhaMesmaHora
        ).forEach(susAgendamentoBotService::enviarSolicitacaoConfirmacaoParaPaciente);

        return ResponseEntity.ok("Notificações enviadas com sucesso");
    }
}

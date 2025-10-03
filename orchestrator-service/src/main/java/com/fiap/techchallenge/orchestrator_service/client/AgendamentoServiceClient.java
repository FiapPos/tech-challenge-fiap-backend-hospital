package com.fiap.techchallenge.orchestrator_service.client;
import com.fiap.techchallenge.orchestrator_service.dto.DadosAgendamento;
import com.fiap.techchallenge.orchestrator_service.dto.SagaEventoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@FeignClient(name = "agendamento-service", url = "${services.agendamento.url}")
public interface AgendamentoServiceClient {

    // --- CRIAÇÃO ---
    @PostMapping("/api/consultas")
    DadosAgendamento criarConsultaPendente(@RequestBody DadosAgendamento consulta);

    // --- COMPENSAÇÃO ---
    @DeleteMapping("/api/consultas/cancelar")
    void cancelarConsulta(@RequestBody DadosAgendamento consulta);

    // --- CONFIRMAÇÃO ---
    @PutMapping("/api/consultas/confirmar")
    void confirmarConsulta(@RequestBody DadosAgendamento consulta);

    // --- BUSCAR UM AGENDAMENTO PELO ID ---
    @GetMapping("/api/consultas/{id}")
    DadosAgendamento buscarConsulta(@PathVariable("id") Long id);

    // --- LISTAR AGENDAMENTOS COM FILTROS ---
    @GetMapping("/api/consultas")
    List<DadosAgendamento> listarConsultas(
            @RequestParam(value = "patientId", required = false) String patientId,
            @RequestParam(value = "status", required = false) String status
    );

    // Editar consulta
    @PutMapping("/api/consultas/{id}")
    void editarConsulta(@PathVariable("id") Long id,
                        @RequestBody DadosAgendamento consulta);

    // --- REGISTRAR EVENTO DA SAGA ---
    @PostMapping("/api/consultas/{id}/eventos")
    void registrarEvento(
            @PathVariable("id") Long agendamentoId,
            @RequestParam("tipo") String tipo,
            @RequestBody SagaEventoDto evento
    );

    // Buscar histórico de eventos da saga
    @GetMapping("/api/consultas/{id}/events")
    List<SagaEventoDto> buscarEventos(@PathVariable("id") Long id);
}

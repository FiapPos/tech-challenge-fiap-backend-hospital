package com.fiap.techchallenge.orchestrator_service.client;
import com.fiap.techchallenge.orchestrator_service.dto.DadosAgendamento;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "agendamento-service", url = "${services.agendamento.url}")
public interface AgendamentoServiceClient {

    @PostMapping("/api/consultas")
    DadosAgendamento criarConsultaPendente(@RequestBody DadosAgendamento consulta);

    @DeleteMapping("/api/consultas/cancelar")
    void cancelarConsulta(@RequestBody DadosAgendamento consulta);

    @PutMapping("/api/consultas/confirmar")
    void confirmarConsulta(@RequestBody DadosAgendamento consulta);

    @GetMapping("/api/consultas/{id}")
    DadosAgendamento buscarConsulta(@PathVariable("id") Long id);

    @PutMapping("/api/consultas/{id}")
    void editarConsulta(@PathVariable("id") Long id,
                        @RequestBody DadosAgendamento consulta);
}

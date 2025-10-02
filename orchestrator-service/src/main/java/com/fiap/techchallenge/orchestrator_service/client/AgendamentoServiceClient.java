package com.fiap.techchallenge.orchestrator_service.client;

import com.fiap.techchallenge.orchestrator_service.dto.ConsultaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "agendamento-service", url = "${services.agendamento.url}")
public interface AgendamentoServiceClient {

    // Cria a consulta e retorna o DTO com o ID gerado
    @PostMapping("/api/consultas")
    ConsultaDTO criarConsultaPendente(@RequestBody ConsultaDTO consulta);

    // Ação de compensação
    @DeleteMapping("/api/consultas/{consultaId}/cancelar")
    void cancelarConsulta(@PathVariable("consultaId") Long consultaId);

    // Ação final de sucesso
    @PutMapping("/api/consultas/{consultaId}/confirmar")
    void confirmarConsulta(@PathVariable("consultaId") Long consultaId);
}
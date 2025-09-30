package com.fiap.techchallenge.agendamento_service.controller;
import com.fiap.techchallenge.agendamento_service.dto.ConsultaDTO;
import com.fiap.techchallenge.agendamento_service.entity.Consulta;
import com.fiap.techchallenge.agendamento_service.service.ConsultaService;
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
    public ResponseEntity<ConsultaDTO> criarConsultaPendente(@RequestBody ConsultaDTO dto) {
        Consulta consultaSalva = service.criarConsultaPendente(dto);

        // Mapeia a entidade salva de volta para um DTO para a resposta
        ConsultaDTO responseDto = new ConsultaDTO();
        responseDto.setId(consultaSalva.getId());
        responseDto.setPacienteId(consultaSalva.getPacienteId());
        responseDto.setMedicoId(consultaSalva.getMedicoId());
        responseDto.setDataHora(consultaSalva.getDataHora());
        responseDto.setStatus(consultaSalva.getStatus());

        return ResponseEntity.ok(responseDto);
    }

    // Endpoint de compensação (chamado pelo orquestrador em caso de falha)
    @DeleteMapping("/{consultaId}/cancelar")
    public ResponseEntity<Void> cancelarConsulta(@PathVariable Long consultaId) {
        service.cancelarConsulta(consultaId);
        return ResponseEntity.noContent().build();
    }

    // Endpoint de sucesso final (chamado pelo orquestrador)
    @PutMapping("/{consultaId}/confirmar")
    public ResponseEntity<Void> confirmarConsulta(@PathVariable Long consultaId) {
        service.confirmarConsulta(consultaId);
        return ResponseEntity.noContent().build();
    }
}

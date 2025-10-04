/*
package com.fiap.techchallenge.agendamento_service;
import com.fiap.techchallenge.agendamento_service.controller.ConsultaController;
import com.fiap.techchallenge.agendamento_service.dto.ConsultaDTO;
import com.fiap.techchallenge.agendamento_service.entity.Consulta;
import com.fiap.techchallenge.agendamento_service.service.ConsultaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;

@WebMvcTest(ConsultaController.class)
@AutoConfigureMockMvc(addFilters = false)
class ConsultaControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConsultaService service;

    @Autowired
    private ObjectMapper objectMapper; // para converter DTO em JSON

    @Test
    void criarConsultaPendente_retorna200() throws Exception {
        ConsultaDTO dto = new ConsultaDTO();
        dto.setPacienteId(1L);
        dto.setMedicoId(1L);
        dto.setDataHora(LocalDateTime.parse("2025-09-25T17:00"));
        dto.setStatus("PENDENTE");

        Consulta consultaSalva = new Consulta();
        consultaSalva.setId(1L);
        consultaSalva.setPacienteId(dto.getPacienteId());
        consultaSalva.setMedicoId(dto.getMedicoId());
        consultaSalva.setDataHora(dto.getDataHora());
        consultaSalva.setStatus(dto.getStatus());

        when(service.criarConsultaPendente(any(ConsultaDTO.class))).thenReturn(consultaSalva);

        mockMvc.perform(post("/api/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void cancelarConsulta_retorna204() throws Exception {
        doNothing().when(service).cancelarConsulta(1L);

        mockMvc.perform(delete("/api/consultas/1/cancelar"))
                .andExpect(status().isNoContent());
    }

    @Test
    void confirmarConsulta_retorna204() throws Exception {
        doNothing().when(service).confirmarConsulta(1L);

        mockMvc.perform(put("/api/consultas/1/confirmar"))
                .andExpect(status().isNoContent());
    }
}
*/

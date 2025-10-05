package com.fiap.techchallenge.appointment_service.controller;

import com.fiap.techchallenge.appointment_service.core.dto.response.HistoricoMedicoDto;
import com.fiap.techchallenge.appointment_service.core.service.HistoricoService;
import com.fiap.techchallenge.appointment_service.infra.security.ValidarPaciente;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = com.fiap.techchallenge.appointment_service.infra.api.controller.HistoricoMedicoController.class)
public class HistoricoMedicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HistoricoService historicoService;

    @MockBean
    private ValidarPaciente validarPaciente;

    @Test
    void GET_paciente_futuros_deve_retornar_lista() throws Exception {
        HistoricoMedicoDto dto = new HistoricoMedicoDto();
        dto.setId(1L);
        dto.setPacienteId(1L);
        dto.setStatus("CRIADA");

        Mockito.when(historicoService.buscarAtendimentosFuturosPaciente(anyLong()))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/api/historico/paciente/1/futuros"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{'id':1,'pacienteId':1,'status':'CRIADA'}]"));
    }

    @Test
    void GET_medico_deve_retornar_lista() throws Exception {
        HistoricoMedicoDto dto = new HistoricoMedicoDto();
        dto.setId(2L);
        dto.setMedicoId(5L);
        dto.setStatus("CRIADA");

        Mockito.when(historicoService.buscarAtendimentosPorMedico(anyLong()))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/api/historico/medico/5"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{'id':2,'medicoId':5,'status':'CRIADA'}]"));
    }
}

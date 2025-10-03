package com.fiap.techchallenge.orchestrator_service;

import com.fiap.techchallenge.orchestrator_service.client.AgendamentoServiceClient;
import com.fiap.techchallenge.orchestrator_service.client.HospitalServiceClient;
import com.fiap.techchallenge.orchestrator_service.client.UsuarioServiceClient;
import com.fiap.techchallenge.orchestrator_service.dto.*;
import com.fiap.techchallenge.orchestrator_service.enums.Perfil;
import com.fiap.techchallenge.orchestrator_service.service.AgendamentoSagaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AgendamentoSagaServiceTest {

    private UsuarioServiceClient usuarioClient;
    private AgendamentoServiceClient agendamentoClient;
    private HospitalServiceClient hospitalClient;
    private AgendamentoSagaService sagaService;

    @BeforeEach
    void setup() {
        usuarioClient = mock(UsuarioServiceClient.class);
        agendamentoClient = mock(AgendamentoServiceClient.class);
        hospitalClient = mock(HospitalServiceClient.class);
        sagaService = new AgendamentoSagaService(usuarioClient, agendamentoClient, hospitalClient);
    }

    @Test
    void testIniciarSaga_sucesso() {
        // Mock do paciente
        UsuarioDTO paciente = new UsuarioDTO();
        paciente.setId(1L);
        paciente.setNome("Paciente Teste");

        // Mock da especialidade
        EspecialidadeDTO especialidade = new EspecialidadeDTO();
        especialidade.setId(1L);
        especialidade.setNome("Cardiologia");

        // Mock do médico
        UsuarioDTO medico = new UsuarioDTO();
        medico.setId(2L);
        medico.setNome("Dr Teste");

        // Mock do hospital
        HospitalDTO hospital = new HospitalDTO();
        hospital.setId(1L);
        hospital.setNome("Hospital Teste");
        hospital.setEndereco("Rua Teste");

        // Configura os mocks
        when(usuarioClient.buscaPor(1L, Perfil.PACIENTE)).thenReturn(paciente);
        when(usuarioClient.buscaPor(1L)).thenReturn(especialidade);
        when(usuarioClient.buscaPor(2L, Perfil.MEDICO, 1L)).thenReturn(medico);
        when(hospitalClient.buscaPor(1L)).thenReturn(hospital);

        // Mock da criação da consulta
        DadosAgendamento criado = new DadosAgendamento();
        criado.setAgendamentoId(100L);
        when(agendamentoClient.criarConsultaPendente(any())).thenReturn(criado);

        // Request
        AgendamentoRequest request = new AgendamentoRequest(
                1L, 2L, 1L, 1L, LocalDateTime.now().plusDays(1)
        );

        // Executa
        SagaResponse response = sagaService.iniciarSaga(request);

        // Verifica
        assertTrue(response.isSucesso());
        assertEquals(100L, response.getConsultaId());
        verify(agendamentoClient, times(1)).criarConsultaPendente(any());
        verify(agendamentoClient, times(1)).confirmarConsulta(any());
    }

    @Test
    void testIniciarSaga_falhaPacienteNaoEncontrado() {
        when(usuarioClient.buscaPor(1L, Perfil.PACIENTE)).thenReturn(null);

        AgendamentoRequest request = new AgendamentoRequest(
                1L, 2L, 1L, 1L, LocalDateTime.now().plusDays(1)
        );

        SagaResponse response = sagaService.iniciarSaga(request);

        assertFalse(response.isSucesso());
        assertTrue(response.getMensagem().contains("Paciente não encontrado"));
    }
}

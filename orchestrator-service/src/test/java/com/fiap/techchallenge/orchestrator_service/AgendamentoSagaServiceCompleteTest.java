package com.fiap.techchallenge.orchestrator_service;
import com.fiap.techchallenge.orchestrator_service.client.AgendamentoServiceClient;
import com.fiap.techchallenge.orchestrator_service.client.HospitalServiceClient;
import com.fiap.techchallenge.orchestrator_service.client.UsuarioServiceClient;
import com.fiap.techchallenge.orchestrator_service.dto.*;
import com.fiap.techchallenge.orchestrator_service.enums.Perfil;
import com.fiap.techchallenge.orchestrator_service.enums.EStatusAgendamento;
import com.fiap.techchallenge.orchestrator_service.service.AgendamentoSagaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AgendamentoSagaServiceCompleteTest {

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

    // ========================= INICIAR SAGA =========================
    @Test
    void testIniciarSaga_sucesso() {
        UsuarioDTO paciente = new UsuarioDTO(); paciente.setId(1L); paciente.setNome("Paciente");
        EspecialidadeDTO especialidade = new EspecialidadeDTO(); especialidade.setId(1L); especialidade.setNome("Cardio");
        UsuarioDTO medico = new UsuarioDTO(); medico.setId(2L); medico.setNome("Dr Teste");
        HospitalDTO hospital = new HospitalDTO(); hospital.setId(1L); hospital.setNome("Hospital Teste"); hospital.setEndereco("Rua Teste");

        when(usuarioClient.buscaPor(1L, Perfil.PACIENTE)).thenReturn(paciente);
        when(usuarioClient.buscaPor(1L)).thenReturn(especialidade);
        when(usuarioClient.buscaPor(2L, Perfil.MEDICO, 1L)).thenReturn(medico);
        when(hospitalClient.buscaPor(1L)).thenReturn(hospital);

        DadosAgendamento criado = new DadosAgendamento();
        criado.setAgendamentoId(100L);
        when(agendamentoClient.criarConsultaPendente(any())).thenReturn(criado);

        AgendamentoRequest request = new AgendamentoRequest(1L,2L,1L,1L, LocalDateTime.now().plusDays(1));

        SagaResponse response = sagaService.iniciarSaga(request);

        assertTrue(response.isSucesso());
        assertEquals(100L, response.getConsultaId());
    }

    // ========================= EDITAR SAGA =========================
    @Test
    void testEditarSaga_sucesso() {
        // Dados atuais do agendamento
        DadosAgendamento atual = new DadosAgendamento();
        atual.setAgendamentoId(100L);
        atual.setDataHoraAgendamento(LocalDateTime.now().plusDays(1));
        atual.setStatusAgendamento(EStatusAgendamento.CRIADA);

        // Mock do buscarConsulta: garantir que retorna o agendamento correto
        when(agendamentoClient.buscarConsulta(100L)).thenReturn(atual);

        // Mock do editarConsulta: não faz nada, apenas verifica chamada
        doNothing().when(agendamentoClient).editarConsulta(eq(100L), any(DadosAgendamento.class));

        // Requisição de atualização
        AgendamentoUpdateRequest updateRequest = new AgendamentoUpdateRequest();
        LocalDateTime novaData = LocalDateTime.now().plusDays(2);
        updateRequest.setDataHora(novaData.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        updateRequest.setServicoId("S1");

        // Executa a edição
        // Observação: converter a String "100" para Long deve acontecer dentro do service
        SagaResponse response = sagaService.editarSaga("100", updateRequest);

        // Verifica se o retorno foi sucesso
        assertTrue(response.isSucesso(), "O retorno do editarSaga deveria ser sucesso");

        // Verifica se o editarConsulta foi chamado corretamente
        verify(agendamentoClient, times(1))
                .editarConsulta(eq(100L), any(DadosAgendamento.class));
    }


    @Test
    void testEditarSaga_falhaNaoEncontrado() {
        when(agendamentoClient.buscarConsulta(100L)).thenReturn(null);

        AgendamentoUpdateRequest updateRequest = new AgendamentoUpdateRequest();
        updateRequest.setDataHora(LocalDateTime.now().plusDays(2).toString());

        SagaResponse response = sagaService.editarSaga("100", updateRequest);

        assertFalse(response.isSucesso());
        assertTrue(response.getMensagem().contains("não encontrado"));
    }

    // ========================= LISTAR AGENDAMENTOS =========================
    @Test
    void testListarAgendamentos() {
        DadosAgendamento agendamento = new DadosAgendamento();
        agendamento.setAgendamentoId(100L);
        agendamento.setPacienteId(1L);
        agendamento.setDataHoraAgendamento(LocalDateTime.now());
        agendamento.setServicoId("S1");
        agendamento.setSagaId("SAGA123");

        when(agendamentoClient.listarConsultas("1", "CRIADA")).thenReturn(List.of(agendamento));

        List<AgendamentoResponse> responses = sagaService.listarAgendamentos("1", "CRIADA");

        assertEquals(1, responses.size());
        assertEquals("S1", responses.get(0).getServicoId());
    }

    // ========================= BUSCAR HISTÓRICO =========================
    @Test
    void testBuscarHistorico() {
        SagaEventoDto evento = new SagaEventoDto(1L, "SAGA123", "CRIACAO", "{}", LocalDateTime.now().toString());
        when(agendamentoClient.buscarEventos(100L)).thenReturn(List.of(evento));

        List<SagaEventoDto> historico = sagaService.buscarHistoricoAgendamento("100");

        assertEquals(1, historico.size());
        assertEquals("SAGA123", historico.get(0).getSagaId());
    }
}

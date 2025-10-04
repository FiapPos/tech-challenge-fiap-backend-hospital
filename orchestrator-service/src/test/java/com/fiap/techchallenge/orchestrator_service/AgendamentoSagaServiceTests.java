<<<<<<< HEAD
package com.fiap.techchallenge.orchestrator_service;

=======
/*
package com.fiap.techchallenge.orchestrator_service;
>>>>>>> origin/main
import com.fiap.techchallenge.orchestrator_service.client.AgendamentoServiceClient;
import com.fiap.techchallenge.orchestrator_service.client.NotificacaoServiceClient;
import com.fiap.techchallenge.orchestrator_service.client.UsuarioServiceClient;
import com.fiap.techchallenge.orchestrator_service.dto.AgendamentoRequest;
import com.fiap.techchallenge.orchestrator_service.dto.ConsultaDTO;
import com.fiap.techchallenge.orchestrator_service.dto.NotificacaoDTO;
import com.fiap.techchallenge.orchestrator_service.dto.SagaResponse;
import com.fiap.techchallenge.orchestrator_service.service.AgendamentoSagaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendamentoSagaServiceTests {

    @InjectMocks
    private AgendamentoSagaService sagaService; // A classe real que estamos testando

    @Mock
    private UsuarioServiceClient usuarioClient; // Mock para o serviço de usuário

    @Mock
    private AgendamentoServiceClient agendamentoClient; // Mock para o serviço de agendamento

    @Mock
    private NotificacaoServiceClient notificacaoClient; // Mock para o serviço de notificação

    @Test
    @DisplayName("Deve Concluir a Saga com Sucesso Quando Todos os Serviços Respondem Corretamente")
    void iniciarSaga_deveConcluirComSucesso_quandoTodosOsServicosRespondemCorretamente() {
        // 1. ARRANGE (Preparação)
        // Define a requisição de entrada para o teste
        AgendamentoRequest request = new AgendamentoRequest(1L, 2L, LocalDateTime.now());

<<<<<<< HEAD
        // Define o DTO que o mock do agendamentoClient deve retornar ao criar a
        // consulta
=======
        // Define o DTO que o mock do agendamentoClient deve retornar ao criar a consulta
>>>>>>> origin/main
        ConsultaDTO consultaCriada = new ConsultaDTO(100L, 1L, 2L, request.getDataHora(), "PENDENTE");

        // Configura o comportamento dos Mocks para o cenário de sucesso
        when(usuarioClient.existe(1L)).thenReturn(true);
        when(usuarioClient.existe(2L)).thenReturn(true);
        when(agendamentoClient.criarConsultaPendente(any(ConsultaDTO.class))).thenReturn(consultaCriada);
        // Usamos doNothing() para mocks de métodos que retornam 'void'
        doNothing().when(notificacaoClient).enviarNotificacao(any(NotificacaoDTO.class));
        doNothing().when(agendamentoClient).confirmarConsulta(100L);

        // 2. ACT (Ação)
        // Executa o método principal que queremos testar
        SagaResponse response = sagaService.iniciarSaga(request);

        // 3. ASSERT (Verificação)
        // Verifica se a resposta da saga indica sucesso
        assertTrue(response.isSucesso());
        assertEquals("Agendamento concluído com sucesso.", response.getMensagem());
        assertEquals(100L, response.getConsultaId());

<<<<<<< HEAD
        // Verifica se os métodos dos mocks foram chamados na ordem e quantidade
        // corretas
=======
        // Verifica se os métodos dos mocks foram chamados na ordem e quantidade corretas
>>>>>>> origin/main
        verify(usuarioClient, times(1)).existe(1L);
        verify(usuarioClient, times(1)).existe(2L);
        verify(agendamentoClient, times(1)).criarConsultaPendente(any(ConsultaDTO.class));
        verify(notificacaoClient, times(1)).enviarNotificacao(any(NotificacaoDTO.class));
        verify(agendamentoClient, times(1)).confirmarConsulta(100L);

        // Garante que nenhuma ação de compensação foi chamada
        verify(agendamentoClient, never()).cancelarConsulta(anyLong());
    }

    @Test
    @DisplayName("Deve Falhar a Saga Imediatamente se o Paciente Não Existe")
    void iniciarSaga_deveFalhar_quandoPacienteNaoExiste() {
        // 1. ARRANGE
        AgendamentoRequest request = new AgendamentoRequest(999L, 2L, LocalDateTime.now());

        // Configura o mock para simular que o paciente não existe
        when(usuarioClient.existe(999L)).thenReturn(false);

        // 2. ACT
        SagaResponse response = sagaService.iniciarSaga(request);

        // 3. ASSERT
        assertFalse(response.isSucesso());
        assertEquals("Falha no processo de agendamento: Paciente não encontrado.", response.getMensagem());

        // Garante que a saga parou no início e não tentou chamar os outros serviços
        verify(agendamentoClient, never()).criarConsultaPendente(any(ConsultaDTO.class));
        verify(notificacaoClient, never()).enviarNotificacao(any(NotificacaoDTO.class));
        verify(agendamentoClient, never()).cancelarConsulta(anyLong());
    }

    @Test
    @DisplayName("Deve Acionar a Compensação Corretamente se o Serviço de Notificação Falhar")
    void iniciarSaga_deveAcionarCompensacao_quandoNotificacaoServiceFalha() {
        // 1. ARRANGE
        AgendamentoRequest request = new AgendamentoRequest(1L, 2L, LocalDateTime.now());
        ConsultaDTO consultaCriada = new ConsultaDTO(100L, 1L, 2L, request.getDataHora(), "PENDENTE");

        // Configura os mocks que devem funcionar antes da falha
        when(usuarioClient.existe(1L)).thenReturn(true);
        when(usuarioClient.existe(2L)).thenReturn(true);
        when(agendamentoClient.criarConsultaPendente(any(ConsultaDTO.class))).thenReturn(consultaCriada);

        // !! SIMULA A FALHA !!
        // Configura o mock para lançar uma exceção quando a notificação for enviada
        doThrow(new RuntimeException("Serviço de notificação fora do ar"))
                .when(notificacaoClient).enviarNotificacao(any(NotificacaoDTO.class));

        // 2. ACT
        SagaResponse response = sagaService.iniciarSaga(request);

        // 3. ASSERT
        // Verifica se a saga reportou a falha corretamente
        assertFalse(response.isSucesso());
        assertTrue(response.getMensagem().contains("Serviço de notificação fora do ar"));

        // !! A VERIFICAÇÃO MAIS IMPORTANTE DO PADRÃO SAGA !!
        // Verifica se a ação de compensação (cancelar a consulta) foi chamada
        verify(agendamentoClient, times(1)).cancelarConsulta(100L);

        // Garante que o passo final da saga (confirmar a consulta) NUNCA foi chamado
        verify(agendamentoClient, never()).confirmarConsulta(anyLong());
    }
<<<<<<< HEAD
}
=======
}
*/
>>>>>>> origin/main

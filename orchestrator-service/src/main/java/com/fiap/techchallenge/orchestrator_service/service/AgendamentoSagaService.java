package com.fiap.techchallenge.orchestrator_service.service;

import com.fiap.techchallenge.orchestrator_service.client.AgendamentoServiceClient;
import com.fiap.techchallenge.orchestrator_service.client.NotificacaoServiceClient;
import com.fiap.techchallenge.orchestrator_service.client.UsuarioServiceClient;
import com.fiap.techchallenge.orchestrator_service.dto.AgendamentoRequest;
import com.fiap.techchallenge.orchestrator_service.dto.ConsultaDTO;
import com.fiap.techchallenge.orchestrator_service.dto.NotificacaoDTO;
import com.fiap.techchallenge.orchestrator_service.dto.SagaResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
@Service
public class AgendamentoSagaService {

    @Autowired
    private UsuarioServiceClient usuarioClient;
    @Autowired
    private AgendamentoServiceClient agendamentoClient;
    @Autowired
    private NotificacaoServiceClient notificacaoClient;

    public SagaResponse iniciarSaga(AgendamentoRequest request) {
        // Lista para armazenar as ações de compensação
        List<Consumer<Void>> compensations = new ArrayList<>();
        Long consultaId = null;

        try {
            // Passo 1 e 2: Validar paciente e médico
            log.info("SAGA - Passo 1: Validando paciente {}", request.getPacienteId());
            if (!usuarioClient.existe(request.getPacienteId())) {
                throw new SagaException("Paciente não encontrado.");
            }
            log.info("SAGA - Passo 2: Validando médico {}", request.getMedicoId());
            if (!usuarioClient.existe(request.getMedicoId())) {
                throw new SagaException("Médico não encontrado.");
            }

            // Passo 3: Criar consulta pendente
            log.info("SAGA - Passo 3: Criando consulta pendente...");
            ConsultaDTO consultaPendente = new ConsultaDTO(request.getPacienteId(), request.getMedicoId(),
                    request.getDataHora());
            ConsultaDTO consultaCriada = agendamentoClient.criarConsultaPendente(consultaPendente);
            consultaId = consultaCriada.getId();
            log.info("SAGA - Consulta pendente criada com ID: {}", consultaId);

            // Adicionar compensação para o passo 3
            final Long finalConsultaId = consultaId;
            compensations.add(v -> {
                log.warn("SAGA - COMPENSAÇÃO: Cancelando consulta ID: {}", finalConsultaId);
                agendamentoClient.cancelarConsulta(finalConsultaId);
            });

            // Passo 4: Enviar notificação
            log.info("SAGA - Passo 4: Solicitando envio de notificação...");
            NotificacaoDTO notificacao = new NotificacaoDTO(request.getPacienteId(), "Sua consulta foi pré-agendada.");
            notificacaoClient.enviarNotificacao(notificacao);
            // (Opcional) Adicionar compensação para notificação, se aplicável

            // Passo 5: Confirmar a consulta
            log.info("SAGA - Passo 5: Confirmando consulta ID: {}", consultaId);
            agendamentoClient.confirmarConsulta(consultaId);

            log.info("SAGA - Concluída com sucesso!");
            return new SagaResponse(true, "Agendamento concluído com sucesso.", consultaId);

        } catch (Exception e) {
            log.error("SAGA - FALHA: Ocorreu um erro durante a saga de agendamento.", e);
            // Reverter a ordem das compensações para executar da última para a primeira
            Collections.reverse(compensations);
            // Executar todas as compensações registradas
            compensations.forEach(compensation -> {
                try {
                    compensation.accept(null);
                } catch (Exception compEx) {
                    log.error("SAGA - FALHA CRÍTICA: Falha ao executar compensação.", compEx);
                    // Aqui, você pode registrar a falha em um "dead letter queue" ou notificar
                    // administradores
                }
            });
            return new SagaResponse(false, "Falha no processo de agendamento: " + e.getMessage(), consultaId);
        }
    }

    // Classe auxiliar para exceções da Saga
    private static class SagaException extends RuntimeException {
        public SagaException(String message) {
            super(message);
        }
    }
}
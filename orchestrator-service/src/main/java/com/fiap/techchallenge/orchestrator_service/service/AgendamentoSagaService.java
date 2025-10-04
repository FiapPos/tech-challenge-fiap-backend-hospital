package com.fiap.techchallenge.orchestrator_service.service;
<<<<<<< HEAD

import com.fiap.techchallenge.orchestrator_service.client.AgendamentoServiceClient;
import com.fiap.techchallenge.orchestrator_service.client.NotificacaoServiceClient;
import com.fiap.techchallenge.orchestrator_service.client.UsuarioServiceClient;
import com.fiap.techchallenge.orchestrator_service.dto.AgendamentoRequest;
import com.fiap.techchallenge.orchestrator_service.dto.ConsultaDTO;
import com.fiap.techchallenge.orchestrator_service.dto.NotificacaoDTO;
import com.fiap.techchallenge.orchestrator_service.dto.SagaResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
=======
import com.fiap.techchallenge.orchestrator_service.client.AgendamentoServiceClient;
import com.fiap.techchallenge.orchestrator_service.client.HospitalServiceClient;
import com.fiap.techchallenge.orchestrator_service.client.UsuarioServiceClient;
import com.fiap.techchallenge.orchestrator_service.dto.*;
import com.fiap.techchallenge.orchestrator_service.enums.Perfil;
import lombok.extern.slf4j.Slf4j;
>>>>>>> origin/main
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

<<<<<<< HEAD
=======
import static com.fiap.techchallenge.orchestrator_service.enums.Perfil.MEDICO;

>>>>>>> origin/main
@Slf4j
@Service
public class AgendamentoSagaService {

<<<<<<< HEAD
    @Autowired
    private UsuarioServiceClient usuarioClient;
    @Autowired
    private AgendamentoServiceClient agendamentoClient;
    @Autowired
    private NotificacaoServiceClient notificacaoClient;
=======
    private final UsuarioServiceClient usuarioClient;
    private final AgendamentoServiceClient agendamentoClient;
    private final HospitalServiceClient hospitalServiceClient;

    public AgendamentoSagaService(UsuarioServiceClient usuarioClient, AgendamentoServiceClient agendamentoClient, HospitalServiceClient hospitalServiceClient) {
        this.usuarioClient = usuarioClient;
        this.agendamentoClient = agendamentoClient;
        this.hospitalServiceClient = hospitalServiceClient;
    }
>>>>>>> origin/main

    public SagaResponse iniciarSaga(AgendamentoRequest request) {
        // Lista para armazenar as ações de compensação
        List<Consumer<Void>> compensations = new ArrayList<>();
        Long consultaId = null;

        try {
<<<<<<< HEAD
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
=======
            DadosAgendamento consultaPendente = new DadosAgendamento();

            // Validações
            log.info("SAGA - Passo 1: Validando paciente {}", request.getPacienteId());
            UsuarioDTO paciente = usuarioClient.buscaPor(request.getPacienteId(), Perfil.PACIENTE);
            if (paciente == null) throw new SagaException("Paciente não encontrado.");
            consultaPendente.setPacienteId(paciente.getId());
            consultaPendente.setNomePaciente(paciente.getNome());

            EspecialidadeDTO especialidade = usuarioClient.buscaPor(request.getEspecialidadeId());
            if (especialidade == null) throw new SagaException("Paciente não encontrado.");


            log.info("SAGA - Passo 2: Validando médico {}", request.getMedicoId());
            UsuarioDTO medico = usuarioClient.buscaPor(request.getMedicoId(), MEDICO, especialidade.getId());
            if (medico == null) throw new SagaException("Médico não encontrado.");
            consultaPendente.setMedicoId(medico.getId());
            consultaPendente.setNomeMedico(medico.getNome());
            consultaPendente.setEspecializacao(especialidade.getNome());

            HospitalDTO dadosHospital = hospitalServiceClient.buscaPor(request.getHospitalId());
            if (dadosHospital == null) throw new SagaException("Hospital não encontrado.");
            consultaPendente.setHospitalId(dadosHospital.getId());
            consultaPendente.setEnderecoHospital(dadosHospital.getEndereco());
            consultaPendente.setNomeHospital(dadosHospital.getNome());

            // Passo 3: Criar consulta pendente
            log.info("SAGA - Passo 3: Criando consulta pendente...");

            consultaPendente.setDataHoraAgendamento(request.getDataHora());

            DadosAgendamento consultaCriada = agendamentoClient.criarConsultaPendente(consultaPendente);
            consultaId = consultaCriada.getAgendamentoId();
>>>>>>> origin/main
            log.info("SAGA - Consulta pendente criada com ID: {}", consultaId);

            // Adicionar compensação para o passo 3
            final Long finalConsultaId = consultaId;
            compensations.add(v -> {
<<<<<<< HEAD
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
=======
                log.warn("SAGA - COMPENSAÇÃO: Cancelando consulta ID: {}", consultaCriada);
                agendamentoClient.cancelarConsulta(consultaCriada);
            });

            // Passo 5: Confirmar a consulta
            log.info("SAGA - Passo 5: Confirmando consulta ID: {}", consultaId);
            agendamentoClient.confirmarConsulta(consultaCriada);
>>>>>>> origin/main

            log.info("SAGA - Concluída com sucesso!");
            return new SagaResponse(true, "Agendamento concluído com sucesso.", consultaId);

        } catch (Exception e) {
            log.error("SAGA - FALHA: Ocorreu um erro durante a saga de agendamento.", e);
<<<<<<< HEAD
            // Reverter a ordem das compensações para executar da última para a primeira
            Collections.reverse(compensations);
            // Executar todas as compensações registradas
=======
            Collections.reverse(compensations);
>>>>>>> origin/main
            compensations.forEach(compensation -> {
                try {
                    compensation.accept(null);
                } catch (Exception compEx) {
                    log.error("SAGA - FALHA CRÍTICA: Falha ao executar compensação.", compEx);
<<<<<<< HEAD
                    // Aqui, você pode registrar a falha em um "dead letter queue" ou notificar
                    // administradores
=======
>>>>>>> origin/main
                }
            });
            return new SagaResponse(false, "Falha no processo de agendamento: " + e.getMessage(), consultaId);
        }
    }

<<<<<<< HEAD
    // Classe auxiliar para exceções da Saga
=======
>>>>>>> origin/main
    private static class SagaException extends RuntimeException {
        public SagaException(String message) {
            super(message);
        }
    }
}
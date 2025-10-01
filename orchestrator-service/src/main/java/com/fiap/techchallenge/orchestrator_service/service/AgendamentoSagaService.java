package com.fiap.techchallenge.orchestrator_service.service;
import com.fiap.techchallenge.orchestrator_service.client.AgendamentoServiceClient;
import com.fiap.techchallenge.orchestrator_service.client.HospitalServiceClient;
import com.fiap.techchallenge.orchestrator_service.client.UsuarioServiceClient;
import com.fiap.techchallenge.orchestrator_service.dto.*;
import com.fiap.techchallenge.orchestrator_service.enums.Perfil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static com.fiap.techchallenge.orchestrator_service.enums.Perfil.MEDICO;

@Slf4j
@Service
public class AgendamentoSagaService {

    private final UsuarioServiceClient usuarioClient;
    private final AgendamentoServiceClient agendamentoClient;
    private final HospitalServiceClient hospitalServiceClient;

    public AgendamentoSagaService(UsuarioServiceClient usuarioClient, AgendamentoServiceClient agendamentoClient, HospitalServiceClient hospitalServiceClient) {
        this.usuarioClient = usuarioClient;
        this.agendamentoClient = agendamentoClient;
        this.hospitalServiceClient = hospitalServiceClient;
    }

    public SagaResponse iniciarSaga(AgendamentoRequest request) {
        // Lista para armazenar as ações de compensação
        List<Consumer<Void>> compensations = new ArrayList<>();
        Long consultaId = null;

        try {
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
            log.info("SAGA - Consulta pendente criada com ID: {}", consultaId);

            // Adicionar compensação para o passo 3
            final Long finalConsultaId = consultaId;
            compensations.add(v -> {
                log.warn("SAGA - COMPENSAÇÃO: Cancelando consulta ID: {}", consultaCriada);
                agendamentoClient.cancelarConsulta(consultaCriada);
            });

            // Passo 5: Confirmar a consulta
            log.info("SAGA - Passo 5: Confirmando consulta ID: {}", consultaId);
            agendamentoClient.confirmarConsulta(consultaCriada);

            log.info("SAGA - Concluída com sucesso!");
            return new SagaResponse(true, "Agendamento concluído com sucesso.", consultaId);

        } catch (Exception e) {
            log.error("SAGA - FALHA: Ocorreu um erro durante a saga de agendamento.", e);
            Collections.reverse(compensations);
            compensations.forEach(compensation -> {
                try {
                    compensation.accept(null);
                } catch (Exception compEx) {
                    log.error("SAGA - FALHA CRÍTICA: Falha ao executar compensação.", compEx);
                }
            });
            return new SagaResponse(false, "Falha no processo de agendamento: " + e.getMessage(), consultaId);
        }
    }

    private static class SagaException extends RuntimeException {
        public SagaException(String message) {
            super(message);
        }
    }
}
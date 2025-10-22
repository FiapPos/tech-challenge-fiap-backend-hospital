package com.fiap.techchallenge.orchestrator_service.service;

import com.fiap.techchallenge.orchestrator_service.client.AgendamentoServiceClient;
import com.fiap.techchallenge.orchestrator_service.client.HospitalServiceClient;
import com.fiap.techchallenge.orchestrator_service.client.UsuarioServiceClient;
import com.fiap.techchallenge.orchestrator_service.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static com.fiap.techchallenge.orchestrator_service.enums.EStatusAgendamento.CANCELADA;
import static com.fiap.techchallenge.orchestrator_service.enums.Perfil.PROFESSOR;
import static com.fiap.techchallenge.orchestrator_service.enums.Perfil.ESTUDANTE;

@Slf4j
@Service
public class AgendamentoSagaService {

    private final UsuarioServiceClient usuarioClient;
    private final AgendamentoServiceClient agendamentoClient;
    private final HospitalServiceClient hospitalServiceClient;

    public AgendamentoSagaService(UsuarioServiceClient usuarioClient,
                                  AgendamentoServiceClient agendamentoClient,
                                  HospitalServiceClient hospitalServiceClient) {
        this.usuarioClient = usuarioClient;
        this.agendamentoClient = agendamentoClient;
        this.hospitalServiceClient = hospitalServiceClient;
    }

    public SagaResponse iniciarSaga(AgendamentoRequest request) {
        List<Consumer<Void>> compensations = new ArrayList<>();
        Long consultaId = null;

        try {
            DadosAgendamento consultaPendente = new DadosAgendamento();

            log.info("SAGA - Validando estudante {}", request.getPacienteId());
            UsuarioDTO estudante = usuarioClient.buscaPor(request.getPacienteId(), ESTUDANTE);
            if (estudante == null) throw new SagaException("Estudante não encontrado.");
            consultaPendente.setPacienteId(estudante.getId());
            consultaPendente.setNomePaciente(estudante.getNome());

            EspecialidadeDTO especialidade = usuarioClient.buscaPor(request.getEspecialidadeId());
            if (especialidade == null) throw new SagaException("Especialidade não encontrada.");

            log.info("SAGA - Validando professor {}", request.getMedicoId());
            UsuarioDTO professor = usuarioClient.buscaPor(request.getMedicoId(), PROFESSOR, especialidade.getId());
            if (professor == null) throw new SagaException("Professor não encontrado.");
            consultaPendente.setMedicoId(professor.getId());
            consultaPendente.setNomeMedico(professor.getNome());
            consultaPendente.setEspecialidadeId(especialidade.getId());
            consultaPendente.setEspecializacao(especialidade.getNome());

            HospitalDTO dadosHospital = hospitalServiceClient.buscaPor(request.getHospitalId());
            if (dadosHospital == null) throw new SagaException("Hospital não encontrado.");
            consultaPendente.setHospitalId(dadosHospital.getId());
            consultaPendente.setNomeHospital(dadosHospital.getNome());
            consultaPendente.setEnderecoHospital(dadosHospital.getEndereco());

            consultaPendente.setDataHoraAgendamento(request.getDataHora());

            DadosAgendamento consultaCriada = agendamentoClient.criarConsultaPendente(consultaPendente);
            consultaId = consultaCriada.getAgendamentoId();
            log.info("SAGA - Consulta pendente criada com ID: {}", consultaId);

            final Long finalConsultaId = consultaId;
            compensations.add(v -> {
                log.warn("SAGA - COMPENSAÇÃO: Cancelando consulta ID: {}", consultaCriada);
                agendamentoClient.cancelarConsulta(consultaCriada);
            });

            log.info("SAGA - Confirmando consulta ID: {}", consultaId);
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

    public SagaResponse editarSaga(String appointmentId, AgendamentoUpdateRequest request) {
        List<Consumer<Void>> compensations = new ArrayList<>();
        try {
            DadosAgendamento atual = agendamentoClient.buscarConsulta(Long.parseLong(appointmentId));
            Long professorId = request.getMedicoId() == null ? atual.getMedicoId() : request.getMedicoId();
            Long especialidadeId = request.getEspecialidadeId() == null ? atual.getEspecialidadeId() : request.getEspecialidadeId();
            Long hospitalId = request.getHospitalId() == null ? atual.getHospitalId() : request.getHospitalId();
            LocalDateTime dataHora = request.getDataHora() == null ? atual.getDataHoraAgendamento() : request.getDataHora();

            EspecialidadeDTO especialidade = usuarioClient.buscaPor(especialidadeId);
            if (especialidade == null) throw new SagaException("Especialidade não encontrada.");

            UsuarioDTO estudante = usuarioClient.buscaPor(request.getPacienteId(), ESTUDANTE);
            if (estudante == null) throw new SagaException("Estudante não encontrado.");

            UsuarioDTO professor = usuarioClient.buscaPor(professorId, PROFESSOR, especialidade.getId());
            if (professor == null) throw new SagaException("Professor não encontrado.");

            HospitalDTO hospital = hospitalServiceClient.buscaPor(hospitalId);
            if (hospital == null) throw new SagaException("Hospital não encontrado.");

            compensations.add(v -> agendamentoClient.editarConsulta(atual.getAgendamentoId(), atual));
            atual.atualiza(especialidade, professor, hospital, dataHora, request.getObservacoes(), estudante);

            agendamentoClient.editarConsulta(atual.getAgendamentoId(), atual);
            log.info("SAGA - Edição concluída com sucesso para ID: {}", appointmentId);
            return new SagaResponse(true, "Agendamento editado com sucesso.", atual.getAgendamentoId());

        } catch (Exception e) {
            log.error("SAGA - FALHA na edição do agendamento.", e);
            Collections.reverse(compensations);
            compensations.forEach(comp -> {
                try {
                    comp.accept(null);
                } catch (Exception compEx) {
                    log.error("SAGA - FALHA CRÍTICA ao executar compensação da edição.", compEx);
                }
            });
            return new SagaResponse(false, "Falha ao editar agendamento: " + e.getMessage(), null);
        }
    }

    public void cancelarSaga(String agendamentoId) {
        DadosAgendamento atual = agendamentoClient.buscarConsulta(Long.parseLong(agendamentoId));
        if (CANCELADA.equals(atual.getStatusAgendamento()))
            throw new IllegalStateException("Consulta já está cancelada.");

        EspecialidadeDTO especialidade = usuarioClient.buscaPor(atual.getEspecialidadeId());
        if (especialidade == null) throw new SagaException("Especialidade não encontrada.");

        UsuarioDTO estudante = usuarioClient.buscaPor(atual.getPacienteId(), ESTUDANTE);
        if (estudante == null) throw new SagaException("Estudante não encontrado.");

        UsuarioDTO professor = usuarioClient.buscaPor(atual.getMedicoId(), PROFESSOR, especialidade.getId());
        if (professor == null) throw new SagaException("Professor não encontrado.");

        HospitalDTO hospital = hospitalServiceClient.buscaPor(atual.getHospitalId());
        if (hospital == null) throw new SagaException("Hospital não encontrado.");

        atual.atualiza(especialidade, professor, hospital, estudante);

        agendamentoClient.cancelarConsulta(atual);
    }

    private static class SagaException extends RuntimeException {
        public SagaException(String message) {
            super(message);
        }
    }
}

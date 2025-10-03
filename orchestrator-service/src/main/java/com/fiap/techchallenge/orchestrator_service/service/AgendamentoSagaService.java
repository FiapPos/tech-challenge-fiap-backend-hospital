package com.fiap.techchallenge.orchestrator_service.service;
import com.fiap.techchallenge.orchestrator_service.client.AgendamentoServiceClient;
import com.fiap.techchallenge.orchestrator_service.client.HospitalServiceClient;
import com.fiap.techchallenge.orchestrator_service.client.UsuarioServiceClient;
import com.fiap.techchallenge.orchestrator_service.dto.*;
import com.fiap.techchallenge.orchestrator_service.enums.Perfil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

    public AgendamentoSagaService(UsuarioServiceClient usuarioClient,
                                  AgendamentoServiceClient agendamentoClient,
                                  HospitalServiceClient hospitalServiceClient) {
        this.usuarioClient = usuarioClient;
        this.agendamentoClient = agendamentoClient;
        this.hospitalServiceClient = hospitalServiceClient;
    }

    // --- CRIAÇÃO ---
    public SagaResponse iniciarSaga(AgendamentoRequest request) {
        List<Consumer<Void>> compensations = new ArrayList<>();
        Long consultaId = null;

        try {
            DadosAgendamento consultaPendente = new DadosAgendamento();

            // Validar paciente
            log.info("SAGA - Validando paciente {}", request.getPacienteId());
            UsuarioDTO paciente = usuarioClient.buscaPor(request.getPacienteId(), Perfil.PACIENTE);
            if (paciente == null) throw new SagaException("Paciente não encontrado.");
            consultaPendente.setPacienteId(paciente.getId());
            consultaPendente.setNomePaciente(paciente.getNome());

            // Validar especialidade
            EspecialidadeDTO especialidade = usuarioClient.buscaPor(request.getEspecialidadeId());
            if (especialidade == null) throw new SagaException("Especialidade não encontrada.");

            // Validar médico
            log.info("SAGA - Validando médico {}", request.getMedicoId());
            UsuarioDTO medico = usuarioClient.buscaPor(request.getMedicoId(), MEDICO, especialidade.getId());
            if (medico == null) throw new SagaException("Médico não encontrado.");
            consultaPendente.setMedicoId(medico.getId());
            consultaPendente.setNomeMedico(medico.getNome());
            consultaPendente.setEspecializacao(especialidade.getNome());

            // Validar hospital
            HospitalDTO dadosHospital = hospitalServiceClient.buscaPor(request.getHospitalId());
            if (dadosHospital == null) throw new SagaException("Hospital não encontrado.");
            consultaPendente.setHospitalId(dadosHospital.getId());
            consultaPendente.setNomeHospital(dadosHospital.getNome());
            consultaPendente.setEnderecoHospital(dadosHospital.getEndereco());

            // Definir data
            consultaPendente.setDataHoraAgendamento(request.getDataHora());

            // Criar consulta pendente
            DadosAgendamento consultaCriada = agendamentoClient.criarConsultaPendente(consultaPendente);
            consultaId = consultaCriada.getAgendamentoId();
            log.info("SAGA - Consulta pendente criada com ID: {}", consultaId);

            // Adicionar compensação
            final Long finalConsultaId = consultaId;
            compensations.add(v -> {
                log.warn("SAGA - COMPENSAÇÃO: Cancelando consulta ID: {}", consultaCriada);
                agendamentoClient.cancelarConsulta(consultaCriada);
            });

            // Confirmar consulta
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

    // --- EDIÇÃO ---
    public SagaResponse editarSaga(String appointmentId, AgendamentoUpdateRequest request) {
        List<Consumer<Void>> compensations = new ArrayList<>();
        try {
            DadosAgendamento atual = agendamentoClient.buscarConsulta(Long.parseLong(appointmentId));
            if (atual == null) return new SagaResponse(false, "Agendamento não encontrado.", null);

            // Salvar cópia para compensação
            DadosAgendamento copiaAnterior = new DadosAgendamento(atual);

            // Atualizar campos
            if (request.getDataHora() != null) {
                try {
                    // Tenta ler como ISO_LOCAL_DATE_TIME (com segundos e nanos)
                    atual.setDataHoraAgendamento(LocalDateTime.parse(request.getDataHora(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                } catch (DateTimeParseException e) {
                    // Se falhar, tenta o formato antigo sem segundos
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                    atual.setDataHoraAgendamento(LocalDateTime.parse(request.getDataHora(), formatter));
                }
            }
            if (request.getServicoId() != null) atual.setServicoId(request.getServicoId());
            if (request.getObservacoes() != null) atual.setObservacoes(request.getObservacoes());

            // Compensação para reverter edição
            compensations.add(v -> agendamentoClient.editarConsulta(copiaAnterior.getAgendamentoId(), copiaAnterior));

            // Aplicar edição
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


    // --- LISTAGEM ---
    public List<AgendamentoResponse> listarAgendamentos(String patientId, String status) {
        List<DadosAgendamento> agendamentos = agendamentoClient.listarConsultas(patientId, status);
        List<AgendamentoResponse> responses = new ArrayList<>();
        for (DadosAgendamento d : agendamentos) {
            responses.add(mapToResponse(d));
        }
        return responses;
    }

    // --- BUSCAR AGENDAMENTO ---
    public AgendamentoResponse buscarAgendamento(String appointmentId) {
        try {
            DadosAgendamento dados = agendamentoClient.buscarConsulta(Long.parseLong(appointmentId));
            if (dados == null) return null;
            return AgendamentoResponse.builder()
                    .appointmentId(String.valueOf(dados.getAgendamentoId()))
                    .patientId(String.valueOf(dados.getPacienteId()))
                    .dataHora(dados.getDataHoraFormatada())
                    .servicoId(dados.getServicoId())
                    .sagaId(dados.getSagaId())
                    .status(dados.getStatusAgendamento() != null ? dados.getStatusAgendamento().name() : null)
                    .atualizadoEm(dados.getAtualizadoEm() != null ? dados.getAtualizadoEm().toString() : null)
                    .build();
        } catch (Exception e) {
            log.error("Erro ao buscar agendamento: {}", e.getMessage());
            return null;
        }
    }


    // --- HISTÓRICO ---
    public List<SagaEventoDto> buscarHistoricoAgendamento(String appointmentId) {
        return agendamentoClient.buscarEventos(Long.parseLong(appointmentId));
    }

    // --- MAPEAMENTO ---
    private AgendamentoResponse mapToResponse(DadosAgendamento d) {
        AgendamentoResponse resp = new AgendamentoResponse();
        resp.setAppointmentId(String.valueOf(d.getAgendamentoId()));
        resp.setPatientId(String.valueOf(d.getPacienteId()));
        resp.setDataHora(d.getDataHoraFormatada());
        resp.setServicoId(d.getServicoId());
        resp.setSagaId(d.getSagaId());
        resp.setStatus(d.getStatusAgendamento() != null ? d.getStatusAgendamento().name() : null);
        resp.setAtualizadoEm(d.getAtualizadoEm() != null ? d.getAtualizadoEm().toString() : null);
        return resp;
    }

    // --- EXCEÇÃO INTERNA ---
    private static class SagaException extends RuntimeException {
        public SagaException(String message) {
            super(message);
        }
    }
}

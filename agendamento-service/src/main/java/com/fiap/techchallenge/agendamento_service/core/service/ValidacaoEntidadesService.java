package com.fiap.techchallenge.agendamento_service.core.service;

import com.fiap.techchallenge.agendamento_service.core.client.HospitalServiceClient;
import com.fiap.techchallenge.agendamento_service.core.client.UsuarioServiceClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidacaoEntidadesService {

    private final UsuarioServiceClient usuarioServiceClient;
    private final HospitalServiceClient hospitalServiceClient;

    public void validarPaciente(Long pacienteId) {
        if (pacienteId == null) {
            throw new IllegalArgumentException("ID do paciente é obrigatório");
        }
        try {
            log.info("Validando paciente ID: {}", pacienteId);
            usuarioServiceClient.buscarUsuarioPorId(pacienteId, "PACIENTE");
            log.info("Paciente {} validado com sucesso", pacienteId);
        } catch (FeignException e) {
            log.error("Erro ao validar paciente {} - status {}: {}", pacienteId, e.status(), e.getMessage());
            tratarErroFeign("Paciente", pacienteId, e);
        }
    }

    public void validarMedico(Long medicoId) {
        if (medicoId == null) {
            return;
        }
        try {
            log.info("Validando médico ID: {}", medicoId);
            usuarioServiceClient.buscarUsuarioPorId(medicoId, "MEDICO");
            log.info("Médico {} validado com sucesso", medicoId);
        } catch (FeignException e) {
            log.error("Erro ao validar médico {} - status {}: {}", medicoId, e.status(), e.getMessage());
            tratarErroFeign("Médico", medicoId, e);
        }
    }

    public void validarHospital(Long hospitalId) {
        if (hospitalId == null) {
            throw new IllegalArgumentException("ID do hospital é obrigatório");
        }
        try {
            log.info("Validando hospital ID: {}", hospitalId);
            hospitalServiceClient.buscarHospitalPorId(hospitalId);
            log.info("Hospital {} validado com sucesso", hospitalId);
        } catch (FeignException e) {
            log.error("Erro ao validar hospital {} - status {}: {}", hospitalId, e.status(), e.getMessage());
            tratarErroFeign("Hospital", hospitalId, e);
        }
    }

    public void validarEspecialidade(Long especialidadeId) {
        if (especialidadeId == null) {
            throw new IllegalArgumentException("ID da especialidade é obrigatório");
        }
        try {
            log.info("Validando especialidade ID: {}", especialidadeId);
            usuarioServiceClient.buscarEspecialidadePorId(especialidadeId);
            log.info("Especialidade {} validada com sucesso", especialidadeId);
        } catch (FeignException e) {
            log.error("Erro ao validar especialidade {} - status {}: {}", especialidadeId, e.status(), e.getMessage());
            tratarErroFeign("Especialidade", especialidadeId, e);
        }
    }

    public void validarEntidadesFilaEspera(Long pacienteId, Long medicoId, Long hospitalId, Long especialidadeId) {
        log.info("Validando entidades - Paciente: {}, Médico: {}, Hospital: {}, Especialidade: {}",
                pacienteId, medicoId, hospitalId, especialidadeId);
        validarPaciente(pacienteId);
        validarMedico(medicoId);
        validarHospital(hospitalId);
        validarEspecialidade(especialidadeId);
        log.info("Todas as entidades validadas com sucesso");
    }

    private void tratarErroFeign(String entidade, Long id, FeignException e) {
        int status = e.status();
        switch (status) {
            case 404, 204 -> throw new IllegalArgumentException(entidade + " não encontrado(a) com ID: " + id);
            case 400 -> throw new IllegalArgumentException(entidade + " inválido(a) com ID: " + id);
            case 401, 403 -> throw new IllegalArgumentException(entidade + " não encontrado(a) com ID: " + id);
            case 500, 502, 503, 504 -> throw new IllegalStateException("Serviço temporariamente indisponível. Tente novamente mais tarde.");
            default -> throw new IllegalArgumentException(entidade + " não encontrado(a) com ID: " + id);
        }
    }
}

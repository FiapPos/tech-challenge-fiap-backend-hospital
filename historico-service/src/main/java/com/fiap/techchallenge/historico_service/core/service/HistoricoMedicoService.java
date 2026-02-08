package com.fiap.techchallenge.historico_service.core.service;

import com.fiap.techchallenge.historico_service.core.dto.DadosAgendamento;
import com.fiap.techchallenge.historico_service.core.entity.HistoricoMedico;
import com.fiap.techchallenge.historico_service.core.enums.EStatusAgendamento;
import com.fiap.techchallenge.historico_service.core.repository.HistoricoMedicoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistoricoMedicoService {

    private final HistoricoMedicoRepository repository;

    @Transactional
    public HistoricoMedico salvarHistorico(DadosAgendamento dados) {
        log.info("Salvando histórico médico para agendamento ID: {}", dados.getAgendamentoId());

        HistoricoMedico historico = HistoricoMedico.builder()
                .agendamentoId(dados.getAgendamentoId())
                .pacienteId(dados.getPacienteId())
                .hospitalId(dados.getHospitalId())
                .medicoId(dados.getMedicoId())
                .nomePaciente(dados.getNomePaciente())
                .nomeMedico(dados.getNomeMedico())
                .nomeHospital(dados.getNomeHospital())
                .enderecoHospital(dados.getEnderecoHospital())
                .especializacao(dados.getEspecializacao())
                .statusAgendamento(dados.getStatusAgendamento())
                .dataHoraAgendamento(dados.getDataHoraAgendamento())
                .build();

        return repository.save(historico);
    }

    @Transactional
    public Optional<HistoricoMedico> atualizarHistorico(DadosAgendamento dados) {
        log.info("Atualizando histórico médico para agendamento ID: {}", dados.getAgendamentoId());

        return repository.findByAgendamentoId(dados.getAgendamentoId())
                .map(historico -> {
                    historico.setStatusAgendamento(dados.getStatusAgendamento());
                    historico.setAtualizadoEm(LocalDateTime.now());
                    return repository.save(historico);
                });
    }

    public List<HistoricoMedico> buscarTodosAtendimentosPaciente(Long pacienteId) {
        log.info("Buscando todos os atendimentos do paciente ID: {}", pacienteId);
        return repository.findByPacienteIdOrderByDataHoraAgendamentoDesc(pacienteId);
    }

    public List<HistoricoMedico> buscarAtendimentosFuturosPaciente(Long pacienteId) {
        log.info("Buscando atendimentos futuros do paciente ID: {}", pacienteId);
        return repository.findConsultasFuturasPorPaciente(pacienteId, LocalDateTime.now());
    }

    public List<HistoricoMedico> buscarAtendimentosPassadosPaciente(Long pacienteId) {
        log.info("Buscando atendimentos passados do paciente ID: {}", pacienteId);
        return repository.findConsultasPassadasPorPaciente(pacienteId, LocalDateTime.now());
    }

    public List<HistoricoMedico> buscarAtendimentosPorStatus(Long pacienteId, EStatusAgendamento status) {
        log.info("Buscando atendimentos do paciente ID: {} com status: {}", pacienteId, status);
        return repository.findByPacienteIdAndStatusAgendamentoOrderByDataHoraAgendamentoDesc(pacienteId, status);
    }

    public List<HistoricoMedico> buscarAtendimentosPorPeriodo(Long pacienteId, LocalDateTime dataInicio, LocalDateTime dataFim) {
        log.info("Buscando atendimentos do paciente ID: {} entre {} e {}", pacienteId, dataInicio, dataFim);
        return repository.findConsultasPorPeriodo(pacienteId, dataInicio, dataFim);
    }

    public List<HistoricoMedico> buscarAtendimentosPorMedico(Long medicoId) {
        log.info("Buscando atendimentos do médico ID: {}", medicoId);
        return repository.findByMedicoIdOrderByDataHoraAgendamentoDesc(medicoId);
    }

    public List<HistoricoMedico> buscarAtendimentosFuturosPorMedico(Long medicoId) {
        log.info("Buscando atendimentos futuros do médico ID: {}", medicoId);
        return repository.findConsultasFuturasPorMedico(medicoId, LocalDateTime.now());
    }

    public List<HistoricoMedico> buscarAtendimentosPorHospital(Long hospitalId) {
        log.info("Buscando atendimentos do hospital ID: {}", hospitalId);
        return repository.findByHospitalIdOrderByDataHoraAgendamentoDesc(hospitalId);
    }

    public HistoricoMedicoRepository getRepository() {
        return repository;
    }
}

package com.fiap.techchallenge.agendamento_service.core.service;

import com.fiap.techchallenge.agendamento_service.core.dto.DadosAgendamento;
import com.fiap.techchallenge.agendamento_service.core.entity.Consulta;
import com.fiap.techchallenge.agendamento_service.core.enums.EStatusAgendamento;
import com.fiap.techchallenge.agendamento_service.core.producer.KafkaProducer;
import com.fiap.techchallenge.agendamento_service.core.repository.ConsultaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.fiap.techchallenge.agendamento_service.core.enums.EStatusAgendamento.*;
import static com.fiap.techchallenge.agendamento_service.core.repository.ConsultaRepository.DURACAO_CONSULTA_MINUTOS;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsultaService {

    private final ConsultaRepository repository;
    private final KafkaProducer kafkaProducer;
    private final FilaEsperaService filaEsperaService;
    private final ValidacaoEntidadesService validacaoEntidadesService;
    private final SusAgendamentoBotService susAgendamentoBotService;

    @Transactional
    public Consulta criarConsultaPendente(DadosAgendamento dto) {
        validacaoEntidadesService.validarPaciente(dto.getPacienteId());
        validacaoEntidadesService.validarMedico(dto.getMedicoId());
        validacaoEntidadesService.validarHospital(dto.getHospitalId());
        validacaoEntidadesService.validarEspecialidade(dto.getEspecialidadeId());
        validarConflitoHorario(dto);

        Consulta consulta = new Consulta();
        consulta.setPacienteId(dto.getPacienteId());
        consulta.setMedicoId(dto.getMedicoId());
        consulta.setEspecialidadeId(dto.getEspecialidadeId());
        consulta.setHospitalId(dto.getHospitalId());
        consulta.setDataHora(dto.getDataHoraAgendamento());
        consulta.setStatus(EStatusAgendamento.PENDENTE);
        consulta.setNomePaciente(dto.getNomePaciente());
        consulta.setNomeMedico(dto.getNomeMedico());
        consulta.setNomeHospital(dto.getNomeHospital());
        consulta.setEnderecoHospital(dto.getEnderecoHospital());
        consulta.setEspecializacao(dto.getEspecializacao());
        consulta.setObservacoes(dto.getObservacoes());

        Consulta consultaSalva = repository.save(consulta);

        dto.setAgendamentoId(consultaSalva.getId());
        dto.setStatusAgendamento(consultaSalva.getStatus());
        kafkaProducer.enviarEventosParaHistorico(dto);

        log.info("Consulta criada - ID: {}, paciente: {}, horário: {}",
                consultaSalva.getId(), dto.getPacienteId(), dto.getDataHoraAgendamento());

        return consultaSalva;
    }

    private void validarConflitoHorario(DadosAgendamento dto) {
        List<EStatusAgendamento> statusExcluidos = List.of(CANCELADA);
        LocalDateTime dataHora = dto.getDataHoraAgendamento();
        LocalDateTime dataHoraInicio = dataHora.minusMinutes(DURACAO_CONSULTA_MINUTOS);
        LocalDateTime dataHoraFim = dataHora.plusMinutes(DURACAO_CONSULTA_MINUTOS);

        List<Consulta> conflitoPaciente = repository.findConflitoPaciente(
                dto.getPacienteId(), dataHoraInicio, dataHoraFim, statusExcluidos);

        if (!conflitoPaciente.isEmpty()) {
            log.warn("Conflito: Paciente {} já possui consulta no intervalo de {} min do horário {}",
                    dto.getPacienteId(), DURACAO_CONSULTA_MINUTOS, dto.getDataHoraAgendamento());
            throw new IllegalStateException(
                    String.format("Paciente já possui consulta agendada no intervalo de %d minutos deste horário",
                            DURACAO_CONSULTA_MINUTOS));
        }

        List<Consulta> conflitoMedico = repository.findConflitoMedico(
                dto.getMedicoId(), dataHoraInicio, dataHoraFim, statusExcluidos);

        if (!conflitoMedico.isEmpty()) {
            log.warn("Conflito: Médico {} já possui consulta no intervalo de {} min do horário {}",
                    dto.getMedicoId(), DURACAO_CONSULTA_MINUTOS, dto.getDataHoraAgendamento());
            throw new IllegalStateException(
                    String.format("Médico já possui consulta agendada no intervalo de %d minutos deste horário",
                            DURACAO_CONSULTA_MINUTOS));
        }

        log.info("Validação de conflito OK - paciente: {}, médico: {}, horário: {}",
                dto.getPacienteId(), dto.getMedicoId(), dto.getDataHoraAgendamento());
    }

    public boolean isHorarioDisponivel(Long medicoId, Long pacienteId, LocalDateTime dataHora) {
        LocalDateTime dataHoraInicio = dataHora.minusMinutes(DURACAO_CONSULTA_MINUTOS);
        LocalDateTime dataHoraFim = dataHora.plusMinutes(DURACAO_CONSULTA_MINUTOS);

        boolean medicoOcupado = repository.existsByMedicoIdAndDataHoraBetweenAndStatusNot(
                medicoId, dataHoraInicio, dataHoraFim, CANCELADA);
        boolean pacienteOcupado = repository.existsByPacienteIdAndDataHoraBetweenAndStatusNot(
                pacienteId, dataHoraInicio, dataHoraFim, CANCELADA);

        return !medicoOcupado && !pacienteOcupado;
    }

    @Transactional
    public void cancelarConsulta(DadosAgendamento dto) {
        Consulta consulta = repository.findById(dto.getAgendamentoId())
                .orElseThrow(() -> new EntityNotFoundException("Consulta não encontrada com ID: " + dto.getAgendamentoId()));

        consulta.setStatus(CANCELADA);
        repository.save(consulta);

        log.info("Consulta {} cancelada. Iniciando redirecionamento para fila de espera.", dto.getAgendamentoId());
        DadosAgendamento evento = new DadosAgendamento(consulta);
        evento.setSagaId(dto.getSagaId());
        kafkaProducer.enviarEventosParaNotificacao(evento);
        susAgendamentoBotService.enviaCancelamento(consulta);

        try {
            DadosAgendamento consultaParaRedirecionamento = DadosAgendamento.builder()
                    .agendamentoId(consulta.getId())
                    .pacienteId(consulta.getPacienteId())
                    .medicoId(consulta.getMedicoId())
                    .especialidadeId(consulta.getEspecialidadeId())
                    .hospitalId(consulta.getHospitalId())
                    .dataHoraAgendamento(consulta.getDataHora())
                    .nomeMedico(consulta.getNomeMedico())
                    .nomeHospital(consulta.getNomeHospital())
                    .especializacao(consulta.getEspecializacao())
                    .build();

            filaEsperaService
                    .processarRedirecionamento(consultaParaRedirecionamento)
                    .ifPresent(
                            filaEspera -> susAgendamentoBotService
                                    .enviarSolicitacaoConfirmacaoParaPaciente(
                                            filaEspera.getPacienteId(),
                                            filaEspera.getId(),
                                            consulta
                                    )
                    );
        } catch (Exception e) {
            log.error("Erro ao processar redirecionamento para fila de espera", e);
        }
    }

    @Transactional
    public void confirmarConsulta(DadosAgendamento dto) {
        Consulta consulta = repository.findById(dto.getAgendamentoId())
                .orElseThrow(() -> new EntityNotFoundException("Consulta não encontrada com ID: " + dto.getAgendamentoId()));

        consulta.setStatus(CRIADA);
        repository.save(consulta);

        DadosAgendamento evento = new DadosAgendamento(consulta);
        evento.setSagaId(dto.getSagaId());
        kafkaProducer.enviarEventosParaNotificacao(evento);
        susAgendamentoBotService.enviarConfirmacao(consulta);
    }

    public DadosAgendamento atualizarConsulta(Long id, DadosAgendamento dto) {
        Consulta consulta = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Consulta não encontrada com ID: " + id));

        if (dto.getDataHoraAgendamento() != null && !dto.getDataHoraAgendamento().equals(consulta.getDataHora())) {
            validarConflitoHorario(dto);
        }

        consulta.atualiza(dto);
        repository.save(consulta);

        DadosAgendamento evento = new DadosAgendamento(consulta);
        evento.setSagaId(dto.getSagaId());
        kafkaProducer.enviarEventosParaNotificacao(evento);
        susAgendamentoBotService.enviaAtualizacao(consulta);
        return evento;
    }

    public Consulta buscaConsulta(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Consulta não encontrada com ID: " + id));
    }

    public List<Consulta> listarConsultasPorHospital(Long hospitalId) {
        log.info("Buscando consultas ativas do hospital {}", hospitalId);
        return repository.findByHospitalIdAtivas(hospitalId, CANCELADA);
    }

    public List<Consulta> listarConsultasPorHospitalEData(Long hospitalId, LocalDateTime dataInicio, LocalDateTime dataFim) {
        log.info("Buscando consultas do hospital {} entre {} e {}", hospitalId, dataInicio, dataFim);
        return repository.findByHospitalIdAndData(hospitalId, dataInicio, dataFim, CANCELADA);
    }

    public List<Consulta> listarConsultasPorHospitalEEspecialidade(Long hospitalId, Long especialidadeId) {
        log.info("Buscando consultas do hospital {} para especialidade {}", hospitalId, especialidadeId);
        return repository.findByHospitalIdAndEspecialidadeId(hospitalId, especialidadeId, CANCELADA);
    }

    public List<Consulta> listarConsultasPorMedicoEData(Long medicoId, LocalDateTime dataInicio, LocalDateTime dataFim) {
        log.info("Buscando consultas do médico {} entre {} e {}", medicoId, dataInicio, dataFim);
        return repository.findByMedicoIdAndData(medicoId, dataInicio, dataFim, CANCELADA);
    }
}
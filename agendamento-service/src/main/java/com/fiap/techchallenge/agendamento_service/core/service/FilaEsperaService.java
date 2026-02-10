package com.fiap.techchallenge.agendamento_service.core.service;

import com.fiap.techchallenge.agendamento_service.core.dto.DadosAgendamento;
import com.fiap.techchallenge.agendamento_service.core.dto.FilaEsperaDTO;
import com.fiap.techchallenge.agendamento_service.core.entity.Consulta;
import com.fiap.techchallenge.agendamento_service.core.entity.FilaEspera;
import com.fiap.techchallenge.agendamento_service.core.enums.EStatusAgendamento;
import com.fiap.techchallenge.agendamento_service.core.enums.EStatusFilaEspera;
import com.fiap.techchallenge.agendamento_service.core.producer.KafkaProducer;
import com.fiap.techchallenge.agendamento_service.core.repository.ConsultaRepository;
import com.fiap.techchallenge.agendamento_service.core.repository.FilaEsperaRepository;
import jakarta.persistence.EntityNotFoundException;
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
public class FilaEsperaService {

    private final FilaEsperaRepository filaEsperaRepository;
    private final ConsultaRepository consultaRepository;
    private final KafkaProducer kafkaProducer;
    private final ValidacaoEntidadesService validacaoEntidadesService;

    @Transactional
    public FilaEsperaDTO adicionarNaFila(FilaEsperaDTO dto) {
        log.info("Adicionando paciente {} na fila - especialidade: {}, hospital: {}",
                dto.getPacienteId(), dto.getEspecialidadeId(), dto.getHospitalId());

        validacaoEntidadesService.validarEntidadesFilaEspera(
                dto.getPacienteId(), dto.getMedicoId(), dto.getHospitalId(), dto.getEspecialidadeId());

        validarDatasObrigatorias(dto);
        validarPacienteJaNaFila(dto);

        FilaEspera filaEspera = dto.toEntity();
        filaEspera.calcularPesoPrioridade();
        FilaEspera salvo = filaEsperaRepository.save(filaEspera);

        log.info("Paciente {} adicionado - peso: {} (PCD: {}, Idoso: {}, Gestante: {})",
                salvo.getPacienteId(), salvo.getPesoPrioridade(), salvo.getPcd(), salvo.getIdoso(), salvo.getGestante());

        return new FilaEsperaDTO(salvo);
    }

    @Transactional
    public Optional<FilaEsperaDTO> processarRedirecionamento(DadosAgendamento consultaCancelada) {
        log.info("Processando redirecionamento - especialidade: {}, hospital: {}, dataHora: {}",
                consultaCancelada.getEspecialidadeId(), consultaCancelada.getHospitalId(),
                consultaCancelada.getDataHoraAgendamento());

        return filaEsperaRepository.findProximoPacientePrioritario(
                EStatusFilaEspera.AGUARDANDO,
                consultaCancelada.getEspecialidadeId(),
                consultaCancelada.getHospitalId(),
                consultaCancelada.getDataHoraAgendamento())
            .map(paciente -> {
                log.info("Paciente prioritário encontrado: {} - peso: {}",
                        paciente.getPacienteId(), paciente.getPesoPrioridade());

                paciente.setStatus(EStatusFilaEspera.NOTIFICADO);
                paciente.setNotificadoEm(LocalDateTime.now());
                filaEsperaRepository.save(paciente);

                enviarNotificacaoVagaDisponivel(paciente, consultaCancelada);
                return new FilaEsperaDTO(paciente);
            });
    }

    public List<FilaEsperaDTO> buscarPacientesPrioritarios(Long especialidadeId, Long hospitalId, LocalDateTime dataHora) {
        return filaEsperaRepository.findPacientesAguardandoPorPrioridade(
                EStatusFilaEspera.AGUARDANDO, especialidadeId, hospitalId, dataHora)
            .stream()
            .map(FilaEsperaDTO::new)
            .toList();
    }

    public List<FilaEsperaDTO> buscarPacientesPrioritariosComMedico(Long especialidadeId, Long hospitalId,
                                                                     Long medicoId, LocalDateTime dataHora) {
        return filaEsperaRepository.findPacientesAguardandoPorPrioridadeComMedico(
                EStatusFilaEspera.AGUARDANDO, especialidadeId, hospitalId, medicoId, dataHora)
            .stream()
            .map(FilaEsperaDTO::new)
            .toList();
    }

    @Transactional
    public FilaEsperaDTO aceitarProposta(Long filaEsperaId) {
        FilaEspera fila = buscarFilaOuErro(filaEsperaId);
        validarStatusNotificado(fila);

        fila.setStatus(EStatusFilaEspera.ACEITO);
        fila.setAtualizadoEm(LocalDateTime.now());
        FilaEspera salvo = filaEsperaRepository.save(fila);

        log.info("Paciente {} ACEITOU proposta e SAIU da fila", salvo.getPacienteId());
        enviarConfirmacaoAceite(salvo);

        return new FilaEsperaDTO(salvo);
    }

    @Transactional
    public FilaEsperaDTO recusarProposta(Long filaEsperaId) {
        FilaEspera fila = buscarFilaOuErro(filaEsperaId);
        validarStatusNotificado(fila);

        fila.setStatus(EStatusFilaEspera.AGUARDANDO);
        fila.setNotificadoEm(null);
        fila.setAtualizadoEm(LocalDateTime.now());
        FilaEspera salvo = filaEsperaRepository.save(fila);

        log.info("Paciente {} RECUSOU proposta e VOLTOU para fila", salvo.getPacienteId());
        return new FilaEsperaDTO(salvo);
    }

    @Transactional
    public void removerDaFila(Long filaEsperaId) {
        FilaEspera fila = buscarFilaOuErro(filaEsperaId);
        fila.setStatus(EStatusFilaEspera.REMOVIDO);
        filaEsperaRepository.save(fila);
        log.info("Paciente {} removido da fila", fila.getPacienteId());
    }

    @Transactional
    public FilaEsperaDTO notificarPacienteManualmente(Long filaEsperaId, Long medicoId, LocalDateTime dataHora,
                                                       String nomeMedico, String nomeHospital) {
        FilaEspera fila = buscarFilaOuErro(filaEsperaId);
        validarStatusAguardando(fila);

        fila.setStatus(EStatusFilaEspera.NOTIFICADO);
        fila.setNotificadoEm(LocalDateTime.now());
        fila.setAtualizadoEm(LocalDateTime.now());
        FilaEspera salvo = filaEsperaRepository.save(fila);

        log.info("Paciente {} NOTIFICADO manualmente - medicoId: {}, dataHora: {}",
                salvo.getPacienteId(), medicoId, dataHora);

        enviarPropostaManual(fila, medicoId, dataHora, nomeMedico, nomeHospital);
        return new FilaEsperaDTO(salvo);
    }

    @Transactional
    public void expirarPropostasAntigas() {
        LocalDateTime limite = LocalDateTime.now().minusHours(24);
        List<FilaEspera> expirados = filaEsperaRepository.findNotificadosExpirados(EStatusFilaEspera.NOTIFICADO, limite);

        expirados.forEach(fila -> {
            fila.setStatus(EStatusFilaEspera.AGUARDANDO);
            fila.setNotificadoEm(null);
            fila.setAtualizadoEm(LocalDateTime.now());
            filaEsperaRepository.save(fila);
            log.info("Proposta expirada - paciente {} VOLTOU para fila", fila.getPacienteId());
        });

        log.info("Total de propostas expiradas: {}", expirados.size());
    }

    @Transactional
    public int alocarProximosDaFila() {
        List<FilaEspera> aguardando = filaEsperaRepository.findAllAguardandoOrdenadoPorPrioridade(EStatusFilaEspera.AGUARDANDO);
        int alocados = 0;

        for (FilaEspera fila : aguardando) {
            try {
                Consulta consultaSalva = criarConsultaParaPaciente(fila);
                atualizarFilaParaAceito(fila);
                enviarEventosKafka(fila, consultaSalva);

                log.info("Paciente {} ALOCADO - consultaId: {}, prioridade: {} (PCD: {}, Idoso: {}, Gestante: {})",
                        fila.getPacienteId(), consultaSalva.getId(), fila.getPesoPrioridade(),
                        fila.getPcd(), fila.getIdoso(), fila.getGestante());
                alocados++;
            } catch (Exception e) {
                log.error("Erro ao alocar paciente {}: {}", fila.getPacienteId(), e.getMessage());
            }
        }
        return alocados;
    }

    public List<FilaEsperaDTO> buscarFilaPorPaciente(Long pacienteId) {
        return filaEsperaRepository.findByPacienteIdAndStatus(pacienteId, EStatusFilaEspera.AGUARDANDO)
            .stream()
            .map(FilaEsperaDTO::new)
            .toList();
    }

    public FilaEsperaDTO buscarPorId(Long id) {
        return filaEsperaRepository.findById(id)
            .map(FilaEsperaDTO::new)
            .orElseThrow(() -> new EntityNotFoundException("Entrada na fila não encontrada: " + id));
    }

    private void validarDatasObrigatorias(FilaEsperaDTO dto) {
        if (dto.getDataHoraDesejadaInicio() == null || dto.getDataHoraDesejadaFim() == null) {
            throw new IllegalArgumentException("Data/hora de início e fim são obrigatórios");
        }
        if (dto.getDataHoraDesejadaInicio().isAfter(dto.getDataHoraDesejadaFim())) {
            throw new IllegalArgumentException("Data/hora de início não pode ser posterior à data/hora de fim");
        }
    }

    private void validarPacienteJaNaFila(FilaEsperaDTO dto) {
        if (filaEsperaRepository.existsByPacienteIdAndEspecialidadeIdAndHospitalIdAndStatus(
                dto.getPacienteId(), dto.getEspecialidadeId(), dto.getHospitalId(), EStatusFilaEspera.AGUARDANDO)) {
            throw new IllegalStateException("Paciente já está na fila de espera para esta especialidade e hospital");
        }
    }

    private FilaEspera buscarFilaOuErro(Long filaEsperaId) {
        return filaEsperaRepository.findById(filaEsperaId)
            .orElseThrow(() -> new EntityNotFoundException("Entrada na fila não encontrada: " + filaEsperaId));
    }

    private void validarStatusNotificado(FilaEspera fila) {
        if (fila.getStatus() != EStatusFilaEspera.NOTIFICADO) {
            throw new IllegalStateException("Entrada na fila não está com status NOTIFICADO");
        }
    }

    private void validarStatusAguardando(FilaEspera fila) {
        if (fila.getStatus() != EStatusFilaEspera.AGUARDANDO) {
            throw new IllegalStateException("Paciente não está com status AGUARDANDO. Status atual: " + fila.getStatus());
        }
    }

    private Consulta criarConsultaParaPaciente(FilaEspera fila) {
        String prioridadeDesc = montarDescricaoPrioridade(fila);

        Consulta consulta = new Consulta();
        consulta.setPacienteId(fila.getPacienteId());
        consulta.setMedicoId(fila.getMedicoId() != null ? fila.getMedicoId() : 0L);
        consulta.setEspecialidadeId(fila.getEspecialidadeId());
        consulta.setHospitalId(fila.getHospitalId());
        consulta.setDataHora(fila.getDataHoraDesejadaInicio());
        consulta.setStatus(EStatusAgendamento.CRIADA);
        consulta.setNomePaciente(fila.getNomePaciente());
        consulta.setNomeHospital("Hospital ID: " + fila.getHospitalId());
        consulta.setNomeMedico("Médico a definir");
        consulta.setEspecializacao("Especialidade ID: " + fila.getEspecialidadeId());
        consulta.setEnderecoHospital("Endereço a confirmar");
        consulta.setObservacoes("ALOCADO_AUTOMATICAMENTE - Prioridade: " + prioridadeDesc + " (Peso: " + fila.getPesoPrioridade() + ")");
        return consultaRepository.save(consulta);
    }

    private void atualizarFilaParaAceito(FilaEspera fila) {
        fila.setStatus(EStatusFilaEspera.ACEITO);
        fila.setAtualizadoEm(LocalDateTime.now());
        filaEsperaRepository.save(fila);
    }

    private void enviarEventosKafka(FilaEspera fila, Consulta consulta) {
        String prioridadeDesc = montarDescricaoPrioridade(fila);

        DadosAgendamento dados = DadosAgendamento.builder()
                .agendamentoId(consulta.getId())
                .pacienteId(fila.getPacienteId())
                .especialidadeId(fila.getEspecialidadeId())
                .hospitalId(fila.getHospitalId())
                .medicoId(fila.getMedicoId() != null ? fila.getMedicoId() : 0L)
                .dataHoraAgendamento(fila.getDataHoraDesejadaInicio())
                .nomePaciente(fila.getEmailPaciente() != null ? fila.getEmailPaciente() : "Paciente " + fila.getPacienteId())
                .nomeMedico("Médico a definir")
                .nomeHospital("Hospital ID: " + fila.getHospitalId())
                .enderecoHospital("Endereço a confirmar")
                .especializacao("Especialidade ID: " + fila.getEspecialidadeId())
                .statusAgendamento(EStatusAgendamento.CRIADA)
                .observacoes("ALOCADO_AUTOMATICAMENTE - Prioridade: " + prioridadeDesc + " (Peso: " + fila.getPesoPrioridade() + ")")
                .build();

        kafkaProducer.enviarEventosParaNotificacao(dados);
        kafkaProducer.enviarEventosParaHistorico(dados);
    }

    private String montarDescricaoPrioridade(FilaEspera fila) {
        StringBuilder desc = new StringBuilder();
        if (Boolean.TRUE.equals(fila.getPcd())) desc.append("PCD ");
        if (Boolean.TRUE.equals(fila.getIdoso())) desc.append("Idoso ");
        if (Boolean.TRUE.equals(fila.getGestante())) desc.append("Gestante ");
        return desc.isEmpty() ? "Normal" : desc.toString().trim();
    }

    private void enviarNotificacaoVagaDisponivel(FilaEspera paciente, DadosAgendamento consultaDisponivel) {
        DadosAgendamento notificacao = DadosAgendamento.builder()
                .pacienteId(paciente.getPacienteId())
                .especialidadeId(paciente.getEspecialidadeId())
                .hospitalId(paciente.getHospitalId())
                .medicoId(consultaDisponivel.getMedicoId())
                .dataHoraAgendamento(consultaDisponivel.getDataHoraAgendamento())
                .nomePaciente(paciente.getEmailPaciente())
                .nomeHospital(consultaDisponivel.getNomeHospital())
                .nomeMedico(consultaDisponivel.getNomeMedico())
                .especializacao(consultaDisponivel.getEspecializacao())
                .statusAgendamento(EStatusAgendamento.PROPOSTA_VAGA)
                .observacoes("PROPOSTA_VAGA_DISPONIVEL - Fila ID: " + paciente.getId())
                .build();

        log.info("Enviando notificação de vaga para paciente {}", paciente.getPacienteId());
        kafkaProducer.enviarPropostaVagaDisponivel(notificacao);
    }

    private void enviarConfirmacaoAceite(FilaEspera paciente) {
        DadosAgendamento confirmacao = DadosAgendamento.builder()
                .pacienteId(paciente.getPacienteId())
                .especialidadeId(paciente.getEspecialidadeId())
                .hospitalId(paciente.getHospitalId())
                .nomePaciente(paciente.getEmailPaciente())
                .statusAgendamento(EStatusAgendamento.CRIADA)
                .observacoes("PROPOSTA_ACEITA - Paciente saiu da fila de espera")
                .build();

        log.info("Enviando confirmação de aceite para paciente {}", paciente.getPacienteId());
        kafkaProducer.enviarEventosParaNotificacao(confirmacao);
    }

    private void enviarPropostaManual(FilaEspera fila, Long medicoId, LocalDateTime dataHora,
                                       String nomeMedico, String nomeHospital) {
        DadosAgendamento proposta = DadosAgendamento.builder()
                .pacienteId(fila.getPacienteId())
                .medicoId(medicoId)
                .especialidadeId(fila.getEspecialidadeId())
                .hospitalId(fila.getHospitalId())
                .dataHoraAgendamento(dataHora)
                .nomePaciente(fila.getEmailPaciente())
                .nomeMedico(nomeMedico != null ? nomeMedico : "Médico")
                .nomeHospital(nomeHospital != null ? nomeHospital : "Hospital")
                .statusAgendamento(EStatusAgendamento.PROPOSTA_VAGA)
                .observacoes("PROPOSTA_MANUAL - Fila ID: " + fila.getId())
                .build();

        kafkaProducer.enviarPropostaVagaDisponivel(proposta);
    }
}

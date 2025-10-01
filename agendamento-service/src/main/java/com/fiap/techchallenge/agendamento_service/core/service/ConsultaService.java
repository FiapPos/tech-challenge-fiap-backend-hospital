package com.fiap.techchallenge.agendamento_service.core.service;

import com.fiap.techchallenge.agendamento_service.core.dto.DadosAgendamento;
import com.fiap.techchallenge.agendamento_service.core.entity.Consulta;
import com.fiap.techchallenge.agendamento_service.core.enums.EStatusAgendamento;
import com.fiap.techchallenge.agendamento_service.core.producer.KafkaProducer;
import com.fiap.techchallenge.agendamento_service.core.repository.ConsultaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.fiap.techchallenge.agendamento_service.core.enums.EStatusAgendamento.CANCELADA;
import static com.fiap.techchallenge.agendamento_service.core.enums.EStatusAgendamento.CRIADA;

@Service
public class ConsultaService {

    private ConsultaRepository repository;
    private KafkaProducer kafkaProducer;

    public ConsultaService(ConsultaRepository repository, KafkaProducer kafkaProducer) {
        this.repository = repository;
        this.kafkaProducer = kafkaProducer;
    }

    @Transactional
    public Consulta criarConsultaPendente(DadosAgendamento dto) {
        Consulta consulta = new Consulta();
        consulta.setPacienteId(dto.getPacienteId());
        consulta.setMedicoId(dto.getMedicoId());
        consulta.setDataHora(dto.getDataHoraAgendamento());
        consulta.setStatus(EStatusAgendamento.PENDENTE); // Status inicial definido pela Saga
        return repository.save(consulta);
    }

    @Transactional
    public void cancelarConsulta(DadosAgendamento dto) {
        Consulta consulta = repository.findById(dto.getAgendamentoId())
                .orElseThrow(() -> new EntityNotFoundException("Consulta não encontrada com ID: " + dto.getAgendamentoId()));

        consulta.setStatus(CANCELADA);
        dto.setStatusAgendamento(CANCELADA);
        repository.save(consulta);

        // Publica evento de cancelamento no Kafka
        kafkaProducer.enviarEventos(dto);
    }

    @Transactional
    public void confirmarConsulta(DadosAgendamento dto) {
        Consulta consulta = repository.findById(dto.getAgendamentoId())
                .orElseThrow(() -> new EntityNotFoundException("Consulta não encontrada com ID: " + dto.getAgendamentoId()));

        consulta.setStatus(CRIADA);
        dto.setStatusAgendamento(CRIADA);
        repository.save(consulta);

        // Publica evento de confirmação no Kafka
        kafkaProducer.enviarEventos(dto);
    }
}
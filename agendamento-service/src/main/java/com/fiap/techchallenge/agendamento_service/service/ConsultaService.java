package com.fiap.techchallenge.agendamento_service.service;
import com.fiap.techchallenge.agendamento_service.dto.ConsultaDTO;
import com.fiap.techchallenge.agendamento_service.dto.EventoConsultaDTO;
import com.fiap.techchallenge.agendamento_service.entity.Consulta;
import com.fiap.techchallenge.agendamento_service.repository.ConsultaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository repository;

    @Autowired
    private KafkaProducerService kafkaProducer;

    @Transactional
    public Consulta criarConsultaPendente(ConsultaDTO dto) {
        Consulta consulta = new Consulta();
        consulta.setPacienteId(dto.getPacienteId());
        consulta.setMedicoId(dto.getMedicoId());
        consulta.setDataHora(dto.getDataHora());
        consulta.setStatus("PENDENTE"); // Status inicial definido pela Saga
        return repository.save(consulta);
    }

    @Transactional
    public void cancelarConsulta(Long id) {
        Consulta consulta = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Consulta não encontrada com ID: " + id));

        consulta.setStatus("CANCELADA");
        repository.save(consulta);

        // Publica evento de cancelamento no Kafka
        EventoConsultaDTO evento = new EventoConsultaDTO(consulta.getId(), consulta.getPacienteId(), consulta.getDataHora(), "CONSULTA_CANCELADA");
        kafkaProducer.enviarEventoConsulta(evento);
    }

    @Transactional
    public void confirmarConsulta(Long id) {
        Consulta consulta = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Consulta não encontrada com ID: " + id));

        consulta.setStatus("CONFIRMADA");
        repository.save(consulta);

        // Publica evento de confirmação no Kafka
        EventoConsultaDTO evento = new EventoConsultaDTO(consulta.getId(), consulta.getPacienteId(), consulta.getDataHora(), "CONSULTA_CONFIRMADA");
        kafkaProducer.enviarEventoConsulta(evento);
    }
}
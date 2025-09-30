package com.fiap.techchallenge.agendamento_service.service;
import com.fiap.techchallenge.agendamento_service.dto.EventoConsultaDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaProducerService {

    @Value("${app.kafka.topic.consultas}")
    private String topicoConsultas;

    @Autowired
    private KafkaTemplate<String, EventoConsultaDTO> kafkaTemplate;

    public void enviarEventoConsulta(EventoConsultaDTO evento) {
        try {
            log.info("Enviando evento para o tópico Kafka '{}': {}", topicoConsultas, evento);
            kafkaTemplate.send(topicoConsultas, evento.getConsultaId().toString(), evento);
        } catch (Exception e) {
            log.error("Erro ao enviar evento para o Kafka", e);
        }
    }
}
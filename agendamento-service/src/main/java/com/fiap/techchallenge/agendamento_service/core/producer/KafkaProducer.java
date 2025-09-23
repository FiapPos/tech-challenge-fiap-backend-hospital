package com.fiap.techchallenge.agendamento_service.core.producer;

import com.fiap.techchallenge.agendamento_service.core.dto.AgendamentoCriado;
import com.fiap.techchallenge.agendamento_service.core.dto.AgendamentoEditado;
import com.fiap.techchallenge.agendamento_service.core.dto.EventoOrquestrador;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    private final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topic.agendamento-criado}")
    private String topicoAgendamentoCriado;

    @Value("${spring.kafka.topic.agendamento-editado}")
    private String topicoAgendamentoEditado;

    @Value("${spring.kafka.topic.orquestrador}")
    private String topicoOrquestrador;

    public KafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendAgendamentoCriado(AgendamentoCriado agendamentoCriado) {
        try {
            logger.info("Enviando evento de agendamento criado: {}", agendamentoCriado);
            kafkaTemplate.send(topicoAgendamentoCriado, agendamentoCriado);
            logger.info("Evento de agendamento criado enviado com sucesso: {}", agendamentoCriado);
            sendEvent(EventoOrquestrador.constroiEventoSucesso());
        } catch (Exception ex) {
            logger.error("Não foi possível enviar o evento de agendamento criado: {}", agendamentoCriado, ex);
            sendEvent(EventoOrquestrador.constroiEventoFalha());
        }
    }

    public void sendAgendamentoEditado(AgendamentoEditado agendamentoEditado) {
        try {
            logger.info("Enviando evento de agendamento editado: {}", agendamentoEditado);
            kafkaTemplate.send(topicoAgendamentoEditado, agendamentoEditado);
            logger.info("Evento de agendamento editado enviado com sucesso: {}", agendamentoEditado);
            sendEvent(EventoOrquestrador.constroiEventoSucesso());
        } catch (Exception ex) {
            logger.error("Não foi possível enviar o evento de agendamento editado: {}", agendamentoEditado, ex);
            sendEvent(EventoOrquestrador.constroiEventoFalha());
        }
    }

    private void sendEvent(EventoOrquestrador eventoOrquestrador) {
        try {
            logger.info("Enviando evento para o tópico {} com os dados {}", topicoOrquestrador, eventoOrquestrador);
            kafkaTemplate.send(topicoOrquestrador, eventoOrquestrador);
        } catch (Exception ex) {
            logger.info("Erro ao tentar enviar evento para o tópico {} com os dados {}", topicoOrquestrador, eventoOrquestrador, ex);
        }
    }
}

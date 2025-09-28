package com.fiap.techchallenge.agendamento_service.core.producer;

import com.fiap.techchallenge.agendamento_service.core.dto.Agendamento;
import com.fiap.techchallenge.agendamento_service.core.dto.EventoOrquestrador;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducerTest {

    private final Logger logger = LoggerFactory.getLogger(KafkaProducerTest.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final KafkaProducer kafkaProducer;

    public KafkaProducerTest(KafkaTemplate<String, Object> kafkaTemplate, KafkaProducer kafkaProducer) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaProducer = kafkaProducer;
    }

    /**
     *
     * @param agendamento
     * Só para testes
     */
    public void sendAgendamento(Agendamento agendamento) {
        if (agendamento.getAtualizadoEm() != null) testeEventoAtualizacao(agendamento);
        else testeEventoCriacao(agendamento);
    }

    /**
     *
     * @param agendamento
     * Só para testes
     */
    private void testeEventoAtualizacao(Agendamento agendamento) {
        try {
            logger.info("Enviando evento de agendamento editado: {}", agendamento);
            kafkaTemplate.send("notificacao-sucesso", agendamento);
            logger.info("Evento de agendamento editado enviado com sucesso: {}", agendamento);
            kafkaProducer.sendEvent(EventoOrquestrador.constroiEventoSucesso());
        } catch (Exception ex) {
            logger.error("Não foi possível enviar o evento de agendamento editado: {}", agendamento, ex);
            kafkaProducer.sendEvent(EventoOrquestrador.constroiEventoFalha());
        }
    }

    /**
     *
     * @param agendamento
     * Só para testes
     */
    private void testeEventoCriacao(Agendamento agendamento) {
        try {
            logger.info("Enviando evento de agendamento criado: {}", agendamento);
            kafkaTemplate.send("notificacao-sucesso", agendamento);
            logger.info("Evento de agendamento criado enviado com sucesso: {}", agendamento);
            kafkaProducer.sendEvent(EventoOrquestrador.constroiEventoSucesso());
        } catch (Exception ex) {
            logger.error("Não foi possível enviar o evento de agendamento criado: {}", agendamento, ex);
            kafkaProducer.sendEvent(EventoOrquestrador.constroiEventoFalha());
        }
    }
}

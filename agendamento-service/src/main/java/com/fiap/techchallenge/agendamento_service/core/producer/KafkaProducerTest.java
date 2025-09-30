package com.fiap.techchallenge.agendamento_service.core.producer;

import com.fiap.techchallenge.agendamento_service.core.dto.Evento;
import com.fiap.techchallenge.agendamento_service.core.enums.ESagaStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static com.fiap.techchallenge.agendamento_service.core.enums.ESagaStatus.FAIL;
import static com.fiap.techchallenge.agendamento_service.core.enums.ESagaStatus.SUCCESS;
import static com.fiap.techchallenge.agendamento_service.core.producer.KafkaProducer.FONTE_ATUAL;

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
    public void sendAgendamento(Evento agendamento) {
        if (agendamento.getAtualizadoEm() != null) testeEventoAtualizacao(agendamento);
        else testeEventoCriacao(agendamento);
    }

    /**
     *
     * @param agendamento
     * Só para testes
     */
    private void testeEventoAtualizacao(Evento agendamento) {
        try {
            Evento agendamentoSucesso = constroiEvento(agendamento, SUCCESS);
            logger.info("Enviando evento de agendamento editado: {}", agendamentoSucesso);
            kafkaTemplate.send("notificacao-sucesso", agendamentoSucesso);

            logger.info("Evento de agendamento editado enviado com sucesso: {}", agendamentoSucesso);
            kafkaProducer.sendEvent(agendamentoSucesso);
        } catch (Exception ex) {
            Evento agendamentoFalha = constroiEvento(agendamento, FAIL);
            logger.error("Não foi possível enviar o evento de agendamento editado: {}", agendamentoFalha, ex);
            kafkaProducer.sendEvent(agendamentoFalha);
        }
    }

    private Evento constroiEvento(Evento agendamento, ESagaStatus status) {
        agendamento.setStatus(status);
        agendamento.setFonte(FONTE_ATUAL);
        return agendamento;
    }

    /**
     *
     * @param agendamento
     * Só para testes
     */
    private void testeEventoCriacao(Evento agendamento) {
        try {
            Evento agendamentoSucesso = constroiEvento(agendamento, SUCCESS);
            logger.info("Enviando evento de agendamento criado: {}", agendamentoSucesso);
            kafkaTemplate.send("notificacao-sucesso", agendamentoSucesso);

            logger.info("Evento de agendamento criado enviado com sucesso: {}", agendamentoSucesso);
            kafkaProducer.sendEvent(agendamentoSucesso);
        } catch (Exception ex) {
            Evento agendamentoFalha = constroiEvento(agendamento, FAIL);
            logger.error("Não foi possível enviar o evento de agendamento criado: {}", agendamentoFalha, ex);
            kafkaProducer.sendEvent(agendamentoFalha);
        }
    }
}

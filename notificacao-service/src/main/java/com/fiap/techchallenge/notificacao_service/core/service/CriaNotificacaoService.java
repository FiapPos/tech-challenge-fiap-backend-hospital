package com.fiap.techchallenge.notificacao_service.core.service;

import com.fiap.techchallenge.notificacao_service.core.dto.*;
import com.fiap.techchallenge.notificacao_service.core.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CriaNotificacaoService {

    private final Logger logger = LoggerFactory.getLogger(CriaNotificacaoService.class);
    private final KafkaProducer kafkaProducer;

    public CriaNotificacaoService(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    public void execute(AgendamentoCriadoEvento agendamentoCriado) {
        NotificacaoAgendamentoCriacao notificacao = new NotificacaoAgendamentoCriacao(
                agendamentoCriado.getNomePaciente(),
                agendamentoCriado.getDataHora(),
                agendamentoCriado.getEspecializacao(),
                agendamentoCriado.getValor()
        );
        logger.info("Notificação de criação: {}", notificacao.getTemplateDeMensagem());
        kafkaProducer.sendEvent(EventoOrquestrador.constroiEventoSucesso());
    }

    public void execute(AgendamentoEditadoEvento agendamentoEditado) {
        NotificacaoAgendamentoEdicao notificacao = new NotificacaoAgendamentoEdicao(
                agendamentoEditado.getNomePaciente(),
                agendamentoEditado.getDataHora(),
                agendamentoEditado.getEspecializacao(),
                agendamentoEditado.getValor()
        );
        logger.info("Notificação de edição: {}", notificacao.getTemplateDeMensagem());
        kafkaProducer.sendEvent(EventoOrquestrador.constroiEventoSucesso());
    }
}

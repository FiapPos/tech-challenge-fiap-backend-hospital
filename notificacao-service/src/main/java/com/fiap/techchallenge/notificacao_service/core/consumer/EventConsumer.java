package com.fiap.techchallenge.notificacao_service.core.consumer;

import com.fiap.techchallenge.notificacao_service.core.dto.AgendamentoCriadoEvento;
import com.fiap.techchallenge.notificacao_service.core.dto.AgendamentoEditadoEvento;
import com.fiap.techchallenge.notificacao_service.core.dto.NotificacaoAgendamentoCriacao;
import com.fiap.techchallenge.notificacao_service.core.dto.NotificacaoAgendamentoEdicao;
import com.fiap.techchallenge.notificacao_service.core.service.CriaNotificacaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class EventConsumer {

    private final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    private final CriaNotificacaoService criaNotificacaoService;

    public EventConsumer(CriaNotificacaoService criaNotificacaoService) {
        this.criaNotificacaoService = criaNotificacaoService;
    }

    @KafkaListener(topics = "${spring.kafka.topic.agendamento-criado}",
                   groupId = "${spring.kafka.consumer.group-id}",
                   containerFactory = "agendamentoCriadoKafkaListenerContainerFactory")
    public void consumirAgendamentoCriado(AgendamentoCriadoEvento agendamentoCriado, Acknowledgment acknowledgement) {
        try {
            logger.info("Processando evento de agendamento criado: {}", agendamentoCriado);
 
            NotificacaoAgendamentoCriacao notificacao = criaNotificacaoService.execute(agendamentoCriado);
            logger.info("Notificação de criação: {}", notificacao.getTemplateDeMensagem());
            
            acknowledgement.acknowledge();
        } catch (Exception e) {
            logger.error("Erro ao processar agendamento criado: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "${spring.kafka.topic.agendamento-editado}", 
                   groupId = "${spring.kafka.consumer.group-id}",
                   containerFactory = "agendamentoEditadoKafkaListenerContainerFactory")
    public void consumirAgendamentoEditado(AgendamentoEditadoEvento agendamentoEditado, Acknowledgment acknowledgement) {
        try {
            logger.info("Processando evento de agendamento editado: {}", agendamentoEditado);
 
            NotificacaoAgendamentoEdicao notificacao = criaNotificacaoService.execute(agendamentoEditado);
            logger.info("Notificação de edição: {}", notificacao.getTemplateDeMensagem());
            
            acknowledgement.acknowledge();
        } catch (Exception e) {
            logger.error("Erro ao processar agendamento editado: {}", e.getMessage(), e);
        }
    }

}

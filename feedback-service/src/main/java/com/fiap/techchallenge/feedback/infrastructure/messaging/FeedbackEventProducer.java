package com.fiap.techchallenge.feedback.infrastructure.messaging;

import com.fiap.techchallenge.feedback.core.domain.model.Feedback;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class FeedbackEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC_FEEDBACK_CRIADO = "feedback.criado";
    private static final String TOPIC_FEEDBACK_CRITICO = "feedback.critico";
    private static final String TOPIC_FEEDBACK_RESPONDIDO = "feedback.respondido";

    public void publicarFeedbackCriado(Feedback feedback) {
        try {
            Map<String, Object> evento = criarEventoFeedback(feedback, "FEEDBACK_CRIADO");
            kafkaTemplate.send(TOPIC_FEEDBACK_CRIADO, feedback.getId().toString(), evento);
            log.info("Evento de feedback criado publicado: {}", feedback.getId());
        } catch (Exception e) {
            log.error("Erro ao publicar evento de feedback criado", e);
        }
    }

    public void publicarFeedbackCritico(Feedback feedback) {
        try {
            Map<String, Object> evento = criarEventoFeedback(feedback, "FEEDBACK_CRITICO");
            evento.put("prioridade", "ALTA");
            kafkaTemplate.send(TOPIC_FEEDBACK_CRITICO, feedback.getId().toString(), evento);
            log.warn("Evento de feedback crítico publicado: ID {}, Nota {}", feedback.getId(), feedback.getNota());
        } catch (Exception e) {
            log.error("Erro ao publicar evento de feedback crítico", e);
        }
    }

    public void publicarFeedbackRespondido(Feedback feedback) {
        try {
            Map<String, Object> evento = criarEventoFeedback(feedback, "FEEDBACK_RESPONDIDO");
            evento.put("respondidoPor", feedback.getRespondidoPor());
            evento.put("resposta", feedback.getResposta());
            kafkaTemplate.send(TOPIC_FEEDBACK_RESPONDIDO, feedback.getId().toString(), evento);
            log.info("Evento de feedback respondido publicado: {}", feedback.getId());
        } catch (Exception e) {
            log.error("Erro ao publicar evento de feedback respondido", e);
        }
    }

    private Map<String, Object> criarEventoFeedback(Feedback feedback, String tipoEvento) {
        Map<String, Object> evento = new HashMap<>();
        evento.put("feedbackId", feedback.getId());
        evento.put("aulaId", feedback.getAulaId());
        evento.put("cursoId", feedback.getCursoId());
        evento.put("estudanteId", feedback.getEstudanteId());
        evento.put("professorId", feedback.getProfessorId());
        evento.put("nota", feedback.getNota());
        evento.put("status", feedback.getStatus().toString());
        evento.put("categoria", feedback.getCategoria() != null ? feedback.getCategoria().toString() : null);
        evento.put("dataCriacao", feedback.getDataCriacao().toString());
        evento.put("tipoEvento", tipoEvento);
        return evento;
    }
}


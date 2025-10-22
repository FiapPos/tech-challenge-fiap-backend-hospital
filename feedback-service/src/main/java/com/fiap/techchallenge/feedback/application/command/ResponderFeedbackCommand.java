package com.fiap.techchallenge.feedback.application.command;

import com.fiap.techchallenge.feedback.application.dto.FeedbackResponseDTO;
import com.fiap.techchallenge.feedback.application.dto.RespostaFeedbackDTO;
import com.fiap.techchallenge.feedback.core.domain.model.Feedback;
import com.fiap.techchallenge.feedback.core.domain.model.StatusFeedback;
import com.fiap.techchallenge.feedback.core.domain.repository.FeedbackRepository;
import com.fiap.techchallenge.feedback.infrastructure.messaging.FeedbackEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResponderFeedbackCommand {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackEventProducer eventProducer;

    @Transactional
    public FeedbackResponseDTO executar(Long feedbackId, RespostaFeedbackDTO respostaDTO, Long adminId) {
        registrarLog(adminId, feedbackId);

        Feedback feedback = buscarFeedback(feedbackId);
        atualizarResposta(feedback, respostaDTO, adminId);
        Feedback savedFeedback = salvarFeedback(feedback);
        publicarEvento(savedFeedback);

        return converterParaDTO(savedFeedback);
    }

    private void registrarLog(Long adminId, Long feedbackId) {
        log.info("Administrador {} respondendo feedback {}", adminId, feedbackId);
    }

    private Feedback buscarFeedback(Long feedbackId) {
        return feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback não encontrado: " + feedbackId));
    }

    private void atualizarResposta(Feedback feedback, RespostaFeedbackDTO respostaDTO, Long adminId) {
        feedback.setResposta(respostaDTO.getResposta());
        feedback.setRespondidoPor(adminId);
        feedback.setDataResposta(LocalDateTime.now());
        feedback.setDataAtualizacao(LocalDateTime.now());
        feedback.setStatus(StatusFeedback.RESPONDIDO);
    }

    private Feedback salvarFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    private void publicarEvento(Feedback feedback) {
        eventProducer.publicarFeedbackRespondido(feedback);
    }

    private FeedbackResponseDTO converterParaDTO(Feedback feedback) {
        return FeedbackResponseDTO.fromEntity(feedback);
    }
}

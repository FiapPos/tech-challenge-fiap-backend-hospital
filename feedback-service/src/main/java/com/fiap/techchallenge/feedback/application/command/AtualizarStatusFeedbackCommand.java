package com.fiap.techchallenge.feedback.application.command;

import com.fiap.techchallenge.feedback.core.domain.model.Feedback;
import com.fiap.techchallenge.feedback.core.domain.model.StatusFeedback;
import com.fiap.techchallenge.feedback.core.domain.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AtualizarStatusFeedbackCommand {

    private final FeedbackRepository feedbackRepository;

    @Transactional
    public void executar(Long feedbackId, StatusFeedback novoStatus) {
        Feedback feedback = buscarFeedback(feedbackId);
        atualizarStatus(feedback, novoStatus);
        salvarFeedback(feedback);
    }

    private Feedback buscarFeedback(Long feedbackId) {
        return feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback não encontrado: " + feedbackId));
    }

    private void atualizarStatus(Feedback feedback, StatusFeedback novoStatus) {
        feedback.setStatus(novoStatus);
    }

    private void salvarFeedback(Feedback feedback) {
        feedbackRepository.save(feedback);
    }
}

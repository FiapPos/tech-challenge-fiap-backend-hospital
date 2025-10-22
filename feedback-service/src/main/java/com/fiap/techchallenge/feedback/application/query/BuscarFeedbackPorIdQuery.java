package com.fiap.techchallenge.feedback.application.query;

import com.fiap.techchallenge.feedback.application.dto.FeedbackResponseDTO;
import com.fiap.techchallenge.feedback.core.domain.model.Feedback;
import com.fiap.techchallenge.feedback.core.domain.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BuscarFeedbackPorIdQuery {

    private final FeedbackRepository feedbackRepository;

    @Transactional(readOnly = true)
    public FeedbackResponseDTO executar(Long id) {
        Feedback feedback = buscarFeedback(id);
        return converterParaDTO(feedback);
    }

    private Feedback buscarFeedback(Long id) {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback não encontrado: " + id));
    }

    private FeedbackResponseDTO converterParaDTO(Feedback feedback) {
        return FeedbackResponseDTO.fromEntity(feedback);
    }
}

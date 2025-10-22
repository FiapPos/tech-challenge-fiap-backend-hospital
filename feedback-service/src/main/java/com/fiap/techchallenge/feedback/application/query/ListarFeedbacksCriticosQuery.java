package com.fiap.techchallenge.feedback.application.query;

import com.fiap.techchallenge.feedback.application.dto.FeedbackResponseDTO;
import com.fiap.techchallenge.feedback.core.domain.model.Feedback;
import com.fiap.techchallenge.feedback.core.domain.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListarFeedbacksCriticosQuery {

    private final FeedbackRepository feedbackRepository;

    @Value("${feedback.nota-critica:3}")
    private Integer notaCritica;

    @Transactional(readOnly = true)
    public List<FeedbackResponseDTO> executar() {
        List<Feedback> feedbacks = buscarFeedbacksCriticos();
        return converterParaListaDTO(feedbacks);
    }

    private List<Feedback> buscarFeedbacksCriticos() {
        return feedbackRepository.findFeedbacksCriticos(notaCritica);
    }

    private List<FeedbackResponseDTO> converterParaListaDTO(List<Feedback> feedbacks) {
        return feedbacks.stream()
                .map(FeedbackResponseDTO::fromEntity)
                .toList();
    }
}

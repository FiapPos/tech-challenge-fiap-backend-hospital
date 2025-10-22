package com.fiap.techchallenge.feedback.application.query;

import com.fiap.techchallenge.feedback.application.dto.FeedbackResponseDTO;
import com.fiap.techchallenge.feedback.core.domain.model.Feedback;
import com.fiap.techchallenge.feedback.core.domain.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ListarFeedbacksPorEstudanteQuery {

    private final FeedbackRepository feedbackRepository;

    @Transactional(readOnly = true)
    public List<FeedbackResponseDTO> executar(Long estudanteId) {
        List<Feedback> feedbacks = buscarFeedbacksPorEstudante(estudanteId);
        return converterParaListaDTO(feedbacks);
    }

    private List<Feedback> buscarFeedbacksPorEstudante(Long estudanteId) {
        return feedbackRepository.findByEstudanteId(estudanteId);
    }

    private List<FeedbackResponseDTO> converterParaListaDTO(List<Feedback> feedbacks) {
        return feedbacks.stream()
                .map(FeedbackResponseDTO::fromEntity)
                .toList();
    }
}

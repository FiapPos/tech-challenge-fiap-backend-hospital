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
public class ListarFeedbacksPorCursoQuery {

    private final FeedbackRepository feedbackRepository;

    @Transactional(readOnly = true)
    public List<FeedbackResponseDTO> executar(Long cursoId) {
        List<Feedback> feedbacks = buscarFeedbacksPorCurso(cursoId);
        return converterParaListaDTO(feedbacks);
    }

    private List<Feedback> buscarFeedbacksPorCurso(Long cursoId) {
        return feedbackRepository.findByCursoId(cursoId);
    }

    private List<FeedbackResponseDTO> converterParaListaDTO(List<Feedback> feedbacks) {
        return feedbacks.stream()
                .map(FeedbackResponseDTO::fromEntity)
                .toList();
    }
}

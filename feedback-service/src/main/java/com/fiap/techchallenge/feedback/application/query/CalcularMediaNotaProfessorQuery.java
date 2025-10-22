package com.fiap.techchallenge.feedback.application.query;

import com.fiap.techchallenge.feedback.application.dto.MediaNotaProfessorResponseDTO;
import com.fiap.techchallenge.feedback.core.domain.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CalcularMediaNotaProfessorQuery {

    private final FeedbackRepository feedbackRepository;

    @Transactional(readOnly = true)
    public MediaNotaProfessorResponseDTO executar(Long professorId) {
        Double media = calcularMedia(professorId);
        Long total = contarFeedbacks(professorId);
        Double mediaArredondada = arredondarMedia(media);

        return construirResponse(professorId, mediaArredondada, total);
    }

    private Double calcularMedia(Long professorId) {
        return feedbackRepository.calcularMediaNotaProfessor(professorId);
    }

    private Long contarFeedbacks(Long professorId) {
        return feedbackRepository.countByProfessorId(professorId);
    }

    private Double arredondarMedia(Double media) {
        return media != null ? Math.round(media * 100.0) / 100.0 : 0.0;
    }

    private MediaNotaProfessorResponseDTO construirResponse(Long professorId, Double media, Long total) {
        return MediaNotaProfessorResponseDTO.builder()
                .professorId(professorId)
                .media(media)
                .totalFeedbacks(total)
                .build();
    }
}

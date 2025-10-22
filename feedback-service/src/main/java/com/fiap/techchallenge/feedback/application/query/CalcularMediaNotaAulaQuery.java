package com.fiap.techchallenge.feedback.application.query;

import com.fiap.techchallenge.feedback.application.dto.MediaNotaAulaResponseDTO;
import com.fiap.techchallenge.feedback.core.domain.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CalcularMediaNotaAulaQuery {

    private final FeedbackRepository feedbackRepository;

    @Transactional(readOnly = true)
    public MediaNotaAulaResponseDTO executar(Long aulaId) {
        Double media = calcularMedia(aulaId);
        Long total = contarFeedbacks(aulaId);
        Double mediaArredondada = arredondarMedia(media);

        return construirResponse(aulaId, mediaArredondada, total);
    }

    private Double calcularMedia(Long aulaId) {
        return feedbackRepository.calcularMediaNotaAula(aulaId);
    }

    private Long contarFeedbacks(Long aulaId) {
        return feedbackRepository.countByAulaId(aulaId);
    }

    private Double arredondarMedia(Double media) {
        return media != null ? Math.round(media * 100.0) / 100.0 : 0.0;
    }

    private MediaNotaAulaResponseDTO construirResponse(Long aulaId, Double media, Long total) {
        return MediaNotaAulaResponseDTO.builder()
                .aulaId(aulaId)
                .media(media)
                .totalFeedbacks(total)
                .build();
    }
}

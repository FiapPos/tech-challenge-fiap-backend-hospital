package com.fiap.techchallenge.feedback.application.query;

import com.fiap.techchallenge.feedback.application.dto.MediaNotaCursoResponseDTO;
import com.fiap.techchallenge.feedback.core.domain.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CalcularMediaNotaCursoQuery {

    private final FeedbackRepository feedbackRepository;

    @Transactional(readOnly = true)
    public MediaNotaCursoResponseDTO executar(Long cursoId) {
        Double media = calcularMedia(cursoId);
        Long total = contarFeedbacks(cursoId);
        Double mediaArredondada = arredondarMedia(media);

        return construirResponse(cursoId, mediaArredondada, total);
    }

    private Double calcularMedia(Long cursoId) {
        return feedbackRepository.calcularMediaNotaCurso(cursoId);
    }

    private Long contarFeedbacks(Long cursoId) {
        return feedbackRepository.countByCursoId(cursoId);
    }

    private Double arredondarMedia(Double media) {
        return media != null ? Math.round(media * 100.0) / 100.0 : 0.0;
    }

    private MediaNotaCursoResponseDTO construirResponse(Long cursoId, Double media, Long total) {
        return MediaNotaCursoResponseDTO.builder()
                .cursoId(cursoId)
                .media(media)
                .totalFeedbacks(total)
                .build();
    }
}

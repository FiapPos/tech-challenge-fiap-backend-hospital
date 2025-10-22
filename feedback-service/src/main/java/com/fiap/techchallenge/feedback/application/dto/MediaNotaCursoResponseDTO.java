package com.fiap.techchallenge.feedback.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaNotaCursoResponseDTO {
    private Long cursoId;
    private Double media;
    private Long totalFeedbacks;
}


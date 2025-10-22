package com.fiap.techchallenge.feedback.application.dto;

import com.fiap.techchallenge.feedback.core.domain.model.CategoriaFeedback;
import com.fiap.techchallenge.feedback.core.domain.model.Feedback;
import com.fiap.techchallenge.feedback.core.domain.model.StatusFeedback;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResponseDTO {

    private Long id;
    private Long aulaId;
    private Long cursoId;
    private Long estudanteId;
    private Long professorId;
    private Integer nota;
    private String comentario;
    private StatusFeedback status;
    private CategoriaFeedback categoria;
    private Boolean anonimo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private String resposta;
    private LocalDateTime dataResposta;

    public static FeedbackResponseDTO fromEntity(Feedback feedback) {
        return FeedbackResponseDTO.builder()
                .id(feedback.getId())
                .aulaId(feedback.getAulaId())
                .cursoId(feedback.getCursoId())
                .estudanteId(feedback.getAnonimo() ? null : feedback.getEstudanteId())
                .professorId(feedback.getProfessorId())
                .nota(feedback.getNota())
                .comentario(feedback.getComentario())
                .status(feedback.getStatus())
                .categoria(feedback.getCategoria())
                .anonimo(feedback.getAnonimo())
                .dataCriacao(feedback.getDataCriacao())
                .dataAtualizacao(feedback.getDataAtualizacao())
                .resposta(feedback.getResposta())
                .dataResposta(feedback.getDataResposta())
                .build();
    }
}


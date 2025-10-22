package com.fiap.techchallenge.feedback.application.dto;

import com.fiap.techchallenge.feedback.core.domain.model.CategoriaFeedback;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackRequestDTO {

    @NotNull(message = "O ID da aula é obrigatório")
    private Long aulaId;

    @NotNull(message = "O ID do curso é obrigatório")
    private Long cursoId;

    private Long professorId;

    @NotNull(message = "A nota é obrigatória")
    @Min(value = 1, message = "A nota mínima é 1")
    @Max(value = 5, message = "A nota máxima é 5")
    private Integer nota;

    @NotBlank(message = "O comentário é obrigatório")
    @Size(min = 10, max = 1000, message = "O comentário deve ter entre 10 e 1000 caracteres")
    private String comentario;

    private CategoriaFeedback categoria;

    private Boolean anonimo;
}


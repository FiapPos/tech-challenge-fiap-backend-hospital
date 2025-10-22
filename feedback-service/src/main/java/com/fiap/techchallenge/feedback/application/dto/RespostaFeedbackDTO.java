package com.fiap.techchallenge.feedback.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RespostaFeedbackDTO {

    @NotBlank(message = "A resposta é obrigatória")
    @Size(min = 10, max = 1000, message = "A resposta deve ter entre 10 e 1000 caracteres")
    private String resposta;
}


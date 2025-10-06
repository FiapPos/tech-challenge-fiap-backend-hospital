package com.fiap.techchallenge.appointment_service.core.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoRequest {

    @NotNull
    private Long pacienteId;

    @NotNull
    private Long medicoId;

    @NotNull
    private Long hospitalId;

    @NotNull
    private Long especialidadeId;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataHora;

}

package br.com.fiap.techchallenge.core.dtos.especialidadesDoMedico;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssociarEspecialidadeMedicoDto {
    @NotNull
    private Long especialidadeId;
}

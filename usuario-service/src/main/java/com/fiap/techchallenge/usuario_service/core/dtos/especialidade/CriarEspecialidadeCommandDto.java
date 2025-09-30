package com.fiap.techchallenge.usuario_service.core.dtos.especialidade;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CriarEspecialidadeCommandDto(
        @NotBlank(message = "{especialidade.nome.obrigatorio}") @Size(max = 120, message = "{especialidade.nome.tamanho.maximo}") String novoNome,
        @Size(max = 500, message = "{especialidade.descricao.tamanho.maximo}") String novaDescricao) {
}

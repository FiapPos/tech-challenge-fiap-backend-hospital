package br.com.fiap.techchallenge.core.dtos.especialidade;

import jakarta.validation.constraints.Size;

public record AtualizarEspecialidadeCommandDto(
        @Size(max = 120, message = "{especialidade.nome.tamanho.maximo}") String novoNome,
        @Size(max = 500, message = "{especialidade.descricao.tamanho.maximo}") String novaDescricao) {
}
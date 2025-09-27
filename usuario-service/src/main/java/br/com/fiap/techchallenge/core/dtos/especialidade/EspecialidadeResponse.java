package br.com.fiap.techchallenge.core.dtos.especialidade;

import br.com.fiap.techchallenge.core.domain.entities.Especialidade;

import java.time.LocalDateTime;

public record EspecialidadeResponse(
        Long id,
        String nome,
        String descricao,
        boolean ativo,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao) {
    public static EspecialidadeResponse fromDomain(Especialidade e) {
        return new EspecialidadeResponse(
                e.getId(),
                e.getNome(),
                e.getDescricao(),
                e.isAtivo(),
                e.getDataCriacao(),
                e.getDataAtualizacao());
    }
}

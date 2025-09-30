package com.fiap.techchallenge.usuario_service.core.queries.resultadoItem.especialidade;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ListarEspecialidadePorResultadoItem {
    private Long id;
    private String nome;
    private String descricao;
    private boolean ativo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
}

package com.fiap.techchallenge.usuario_service.core.queries.resultadoItem.especialidade;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EncontraEspecialidadeItem {
    private Long id;
    private String nome;
}

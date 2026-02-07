package com.fiap.techchallenge.usuario_service.core.queries.resultadoItem.usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EncontraUsuarioItem {
    private Long id;
    private String nome;
    private long chatId;
}
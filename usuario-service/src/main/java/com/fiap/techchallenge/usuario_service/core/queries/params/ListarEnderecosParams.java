package com.fiap.techchallenge.usuario_service.core.queries.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ListarEnderecosParams {
    private Long usuarioId;
    private Long restauranteId;

}

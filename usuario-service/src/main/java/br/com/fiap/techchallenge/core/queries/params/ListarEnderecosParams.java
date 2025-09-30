package br.com.fiap.techchallenge.core.queries.params;

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

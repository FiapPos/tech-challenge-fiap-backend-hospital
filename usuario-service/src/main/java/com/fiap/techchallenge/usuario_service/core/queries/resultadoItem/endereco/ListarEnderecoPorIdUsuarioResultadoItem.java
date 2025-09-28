package com.fiap.techchallenge.usuario_service.core.queries.resultadoItem.endereco;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ListarEnderecoPorIdUsuarioResultadoItem {
    private Long id;
    private String rua;
    private String cep;
    private String numero;
    private String bairro;
    private String cidade;
    private Long usuarioId;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
}

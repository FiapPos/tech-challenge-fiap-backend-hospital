package com.fiap.techchallenge.usuario_service.core.dtos.endereco;

import lombok.Data;

@Data
public class DeletarEnderecoComandoDto {
    private Long enderecoId;
    private Long usuarioId;
}

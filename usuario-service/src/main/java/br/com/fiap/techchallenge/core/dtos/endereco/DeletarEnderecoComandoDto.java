package br.com.fiap.techchallenge.core.dtos.endereco;

import lombok.Data;

@Data
public class DeletarEnderecoComandoDto {
    private Long enderecoId;
    private Long usuarioId;
}

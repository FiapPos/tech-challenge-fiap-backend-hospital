package com.fiap.techchallenge.usuario_service.core.dtos.login;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AtualizaCredenciaisComandoDto(

                @NotNull(message = "{senha.nao.nula}") @Size(min = 8, message = "{senha.tamanho.minimo}") String senha,

                @NotNull(message = "{confirmacao.senha.nao.nula}") @Size(min = 8, message = "{confirmacao.senha.tamanho.minimo}") String confirmacaoSenha

) {
}
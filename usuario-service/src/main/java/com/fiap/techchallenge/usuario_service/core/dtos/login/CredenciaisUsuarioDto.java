package com.fiap.techchallenge.usuario_service.core.dtos.login;

import com.fiap.techchallenge.usuario_service.core.enums.Perfil;
import jakarta.validation.constraints.NotBlank;

public record CredenciaisUsuarioDto(
                @NotBlank(message = "{login.nao.nulo}") String login,
                @NotBlank(message = "{senha.nao.nula}") String senha,
                Perfil perfil) {
}
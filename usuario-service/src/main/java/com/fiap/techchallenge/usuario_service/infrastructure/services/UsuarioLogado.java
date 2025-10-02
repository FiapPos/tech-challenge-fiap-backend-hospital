package com.fiap.techchallenge.usuario_service.infrastructure.services;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Usuario;
import com.fiap.techchallenge.usuario_service.core.dtos.login.DetalhesUsuarioDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Optional;

@Component
@RequestScope
public class UsuarioLogado {

    public Optional<Usuario> getUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
            return Optional.empty();
        Object principal = authentication.getPrincipal();
        if (principal instanceof DetalhesUsuarioDto(Usuario usuario)) {
            return Optional.ofNullable(usuario);
        }
        return Optional.empty();
    }

    public Long getUsuarioId() {
        return getUsuario().map(Usuario::getId).orElse(null);
    }
}
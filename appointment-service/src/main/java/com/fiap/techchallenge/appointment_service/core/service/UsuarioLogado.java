package com.fiap.techchallenge.appointment_service.core.service;

import com.fiap.techchallenge.appointment_service.core.client.OrchestratorClient;
import com.fiap.techchallenge.appointment_service.core.dto.UserDetailsImpl;
import com.fiap.techchallenge.appointment_service.core.dto.UsuarioResponse;
import com.fiap.techchallenge.appointment_service.config.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Slf4j
@Component
@RequestScope
@RequiredArgsConstructor
public class UsuarioLogado {

    private final OrchestratorClient orchestratorClient;

    public Authentication getAutenticacaoAtual() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ValidationException("usuario.nao.autenticado");
        }
        return authentication;
    }

    public String extrairUsernameAutenticacao(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else if (principal instanceof UserDetailsImpl userDetailsImpl) {
            return userDetailsImpl.getUsername();
        } else {
            return principal.toString();
        }
    }

    public UsuarioResponse buscarUsuarioPorUsername(String username) {
        UsuarioResponse usuario = orchestratorClient.findUsuarioByLogin(username);
        if (usuario == null) {
            throw new ValidationException("usuario.nao.encontrado");
        }
        return usuario;
    }

    public UsuarioResponse getUsuarioLogado() {
        Authentication authentication = getAutenticacaoAtual();

        if (authentication.getPrincipal() instanceof UserDetailsImpl userDetailsImpl) {
            return userDetailsImpl.usuario();
        }

        String username = extrairUsernameAutenticacao(authentication);
        return buscarUsuarioPorUsername(username);
    }

    public boolean isUsuarioAutorizado(Long usuarioLogadoId, Long usuarioId) {
        return usuarioLogadoId != null && usuarioLogadoId.equals(usuarioId);
    }

    public void validarAcessoUsuario(Long usuarioId) {
        UsuarioResponse usuarioAutenticado = getUsuarioLogado();
        if (!isUsuarioAutorizado(usuarioAutenticado.getId(), usuarioId)) {
            throw new ValidationException("acesso.nao.autorizado");
        }
    }

    public boolean isAdmin() {
        return hasPerfil("ADMIN");
    }

    public boolean hasPerfil(String perfilName) {
        UsuarioResponse usuario = getUsuarioLogado();
        return usuario.getPerfil() != null && perfilName.equals(usuario.getPerfil().getTipo());
    }

    public void requireAdmin() {
        if (!isAdmin()) {
            throw new ValidationException("acesso.admin.necessario");
        }
    }

    public void requirePerfil(String perfilName) {
        if (!hasPerfil(perfilName)) {
            throw new ValidationException("acesso.perfil.necessario: " + perfilName);
        }
    }

    public boolean canAccessResource(Long resourceOwnerId) {
        UsuarioResponse usuario = getUsuarioLogado();
        return isAdmin() || usuario.getId().equals(resourceOwnerId);
    }
}
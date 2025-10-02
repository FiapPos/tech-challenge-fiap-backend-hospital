package com.fiap.techchallenge.usuario_service.infrastructure.services;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Usuario;
import com.fiap.techchallenge.usuario_service.core.gateways.UsuarioRepository;
import com.fiap.techchallenge.usuario_service.core.exceptions.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutorizacaoService {

    private final UsuarioRepository usuarioRepository;

    public Authentication getAutenticacaoAtual() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ForbiddenException("usuario.nao.autenticado");
        }
        return authentication;
    }

    public String extrairUsernameAutenticacao(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    public Usuario buscarUsuarioPorUsername(String username) {
        return usuarioRepository.findByLogin(username)
                .orElseThrow(() -> new ForbiddenException("usuario.nao.encontrado"));
    }

    public Usuario getUsuarioLogado() {
        Authentication authentication = getAutenticacaoAtual();
        String username = extrairUsernameAutenticacao(authentication);
        return buscarUsuarioPorUsername(username);
    }

    public boolean isUsuarioAutorizado(Long usuarioLogadoId, Long usuarioId) {
        return usuarioLogadoId.equals(usuarioId);
    }

    public void validarAcessoUsuario(Long usuarioId) {
        Usuario usuarioAutenticado = getUsuarioLogado();
        if (!isUsuarioAutorizado(usuarioAutenticado.getId(), usuarioId)) {
            throw new ForbiddenException("acesso.nao.autorizado");
        }
    }
}
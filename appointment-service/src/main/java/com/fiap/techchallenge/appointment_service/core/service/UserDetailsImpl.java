package com.fiap.techchallenge.appointment_service.core.service;

import com.fiap.techchallenge.appointment_service.core.dto.response.UsuarioResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Implementação de UserDetails usada internamente pelo serviço de autorização.
 */
public class UserDetailsImpl implements UserDetails {

    private final UsuarioResponse usuario;

    public UserDetailsImpl(UsuarioResponse usuario) {
        this.usuario = usuario;
    }

    public UsuarioResponse getUsuario() {
        return usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (usuario != null && usuario.getPerfil() != null && usuario.getPerfil().getTipo() != null) {
            return List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getPerfil().getTipo()));
        }
        return List.of();
    }

    @Override
    public String getPassword() {
        return usuario != null ? usuario.getSenha() : null;
    }

    @Override
    public String getUsername() {
        return usuario != null ? usuario.getLogin() : null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return usuario == null || usuario.isAtivo();
    }
}

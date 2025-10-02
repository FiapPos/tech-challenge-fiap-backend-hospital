package com.fiap.techchallenge.usuario_service.core.domain.usecases.login;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Usuario;
import com.fiap.techchallenge.usuario_service.core.gateways.UsuarioRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutenticaUsuarioComando {

    private final UsuarioRepository usuarioRepository;

    public AutenticaUsuarioComando(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario getByLogin(String login) {
        return usuarioRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
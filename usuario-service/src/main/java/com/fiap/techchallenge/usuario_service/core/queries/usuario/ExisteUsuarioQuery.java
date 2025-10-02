package com.fiap.techchallenge.usuario_service.core.queries.usuario;

import com.fiap.techchallenge.usuario_service.core.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExisteUsuarioQuery {

    private final UsuarioRepository usuarioRepository;

    public boolean execute(Long usuarioId) {
        return usuarioRepository.existsById(usuarioId);
        // alternadamente poderíamos adicionar existsById no gateway/repository
    }
}

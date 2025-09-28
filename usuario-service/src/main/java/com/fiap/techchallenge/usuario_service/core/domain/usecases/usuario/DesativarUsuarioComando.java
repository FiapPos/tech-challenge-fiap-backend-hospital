package com.fiap.techchallenge.usuario_service.core.domain.usecases.usuario;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Usuario;
import com.fiap.techchallenge.usuario_service.core.gateways.UsuarioRepository;
import com.fiap.techchallenge.usuario_service.core.shared.CompartilhadoService;
import com.fiap.techchallenge.usuario_service.infrastructure.services.ValidarUsuarioExistente;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DesativarUsuarioComando {

    private final UsuarioRepository usuarioRepository;
    private final ValidarUsuarioExistente validarUsuarioExistente;
    private final CompartilhadoService sharedService;

    public Usuario execute(Long id) {
        Usuario usuario = validarUsuarioExistente.execute(id);
        desativarUsuario(usuario);
        return usuarioRepository.save(usuario);
    }

    private void desativarUsuario(Usuario usuario) {
        usuario.setAtivo(false);
        usuario.setDataDesativacao(sharedService.getCurrentDateTime());
    }
}
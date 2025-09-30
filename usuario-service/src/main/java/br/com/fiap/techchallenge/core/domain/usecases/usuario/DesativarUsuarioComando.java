package br.com.fiap.techchallenge.core.domain.usecases.usuario;

import br.com.fiap.techchallenge.core.domain.entities.Usuario;
import br.com.fiap.techchallenge.core.gateways.UsuarioRepository;
import br.com.fiap.techchallenge.core.shared.CompartilhadoService;
import br.com.fiap.techchallenge.infrastructure.services.ValidarUsuarioExistente;
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
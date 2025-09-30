package com.fiap.techchallenge.usuario_service.core.domain.usecases.usuario;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Usuario;
import com.fiap.techchallenge.usuario_service.core.domain.entities.PerfilDoUsuario;
import com.fiap.techchallenge.usuario_service.core.enums.Perfil;
import com.fiap.techchallenge.usuario_service.core.utils.usuario.perfilUsuario.VerificarPerfilUsuarioExistente;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssociarPerfilUsuarioComando {
    private final VerificarPerfilUsuarioExistente verificarPerfilExistente;

    public void execute(Usuario usuario, Perfil perfil) {
        verificarPerfilExistente.execute(usuario, perfil);
        associarPerfilAoUsuario(usuario, perfil);
    }

    private void associarPerfilAoUsuario(Usuario usuario, Perfil perfil) {
        PerfilDoUsuario perfilUsuario = new PerfilDoUsuario();
        perfilUsuario.setUsuario(usuario);
        perfilUsuario.setPerfil(perfil);
        // O domínio mantém um único PerfilDoUsuario em Usuario#perfil; setamos
        // diretamente
        usuario.setPerfil(perfilUsuario);
    }
}

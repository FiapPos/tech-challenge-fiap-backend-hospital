package com.fiap.techchallenge.usuario_service.core.utils.usuario.perfilUsuario;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Usuario;
import com.fiap.techchallenge.usuario_service.core.enums.Perfil;
import com.fiap.techchallenge.usuario_service.core.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificarPerfilUsuarioExistente {

    public void execute(Usuario usuario, Perfil perfil) {
        boolean perfilJaExiste = verificarPerfilExiste(usuario, perfil);

        if (perfilJaExiste) {
            throw new BadRequestException("perfil.usuario.ja.existente");
        }
    }

    private boolean verificarPerfilExiste(Usuario usuario, Perfil perfil) {

        return usuario.getPerfis().stream()
                .anyMatch(ut -> ut.getPerfil().equals(perfil));
    }
}

package com.fiap.techchallenge.usuario_service.core.utils;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Usuario;
import com.fiap.techchallenge.usuario_service.core.enums.Perfil;
import com.fiap.techchallenge.usuario_service.core.exceptions.BadRequestException;
import org.springframework.stereotype.Service;

/**
 * Utilitário para validação do perfil de professor em um {@link Usuario}.
 * Se o usuário não possuir o perfil PROFESSOR, lança BadRequestException
 */
@Service
public class ValidarPerfilProfessor {

    public void execute(Usuario usuario) {
        if (!isProfessor(usuario)) {
            throw new BadRequestException("usuario.nao.professor");
        }
    }

    public boolean isProfessor(Usuario usuario) {
        if (usuario == null) {
            return false;
        }
        if (usuario.getPerfis() != null && !usuario.getPerfis().isEmpty()) {
            return usuario.getPerfis().stream()
                    .anyMatch(ut -> ut.getPerfil() == Perfil.PROFESSOR);
        }
        try {
            Long perfilId = usuario.getPerfilId();
            return perfilId != null && Perfil.PROFESSOR.getCodigo().equals(perfilId);
        } catch (Exception ignored) {
            return false;
        }
    }
}

package br.com.fiap.techchallenge.core.utils;

import br.com.fiap.techchallenge.core.domain.entities.Usuario;
import br.com.fiap.techchallenge.core.enums.Perfil;
import br.com.fiap.techchallenge.core.exceptions.BadRequestException;
import org.springframework.stereotype.Service;

/**
 * Utilitário para validação do perfil de médico em um {@link Usuario}.
 * Se o usuário não possuir o perfil MEDICO, lança BadRequestException
 */
@Service
public class ValidarPerfilMedico {

    public void execute(Usuario usuario) {
        if (!isMedico(usuario)) {
            throw new BadRequestException("usuario.nao.medico");
        }
    }

    public boolean isMedico(Usuario usuario) {
        if (usuario == null) {
            return false;
        }
        if (usuario.getPerfis() != null && !usuario.getPerfis().isEmpty()) {
            return usuario.getPerfis().stream()
                    .anyMatch(ut -> ut.getPerfil() == Perfil.MEDICO);
        }
        try {
            Long perfilId = usuario.getPerfilId();
            return perfilId != null && Perfil.MEDICO.getCodigo().equals(perfilId);
        } catch (Exception ignored) {
            return false;
        }
    }
}

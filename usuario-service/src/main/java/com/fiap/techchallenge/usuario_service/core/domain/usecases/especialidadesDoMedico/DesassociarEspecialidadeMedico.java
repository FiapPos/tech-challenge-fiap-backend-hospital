package com.fiap.techchallenge.usuario_service.core.domain.usecases.especialidadesDoMedico;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Usuario;
import com.fiap.techchallenge.usuario_service.core.gateways.UsuarioRepository;
import com.fiap.techchallenge.usuario_service.infrastructure.services.ValidarUsuarioExistente;
import com.fiap.techchallenge.usuario_service.core.utils.ValidarPerfilProfessor;
import com.fiap.techchallenge.usuario_service.core.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DesassociarEspecialidadeMedico {

    private final UsuarioRepository usuarioRepository;
    private final ValidarUsuarioExistente validarUsuarioExistente;
    private final ValidarPerfilProfessor validarPerfilProfessor;

    public void execute(Long usuarioId, Long especialidadeId) {
        if (especialidadeId == null) {
            throw new BusinessException("especialidade.id.obrigatorio");
        }
        Usuario usuario = validarUsuarioExistente.execute(usuarioId);
        validarPerfilProfessor.execute(usuario);

        boolean removed = usuario.getEspecialidades().removeIf(e -> e.getId().equals(especialidadeId));
        if (!removed) {
            throw new BusinessException("especialidade.nao.associada");
        }
        usuarioRepository.save(usuario);
    }
}

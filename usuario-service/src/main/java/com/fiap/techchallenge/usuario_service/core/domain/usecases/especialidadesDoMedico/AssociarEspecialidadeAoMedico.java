package com.fiap.techchallenge.usuario_service.core.domain.usecases.especialidadesDoMedico;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Usuario;
import com.fiap.techchallenge.usuario_service.core.domain.entities.Especialidade;
import com.fiap.techchallenge.usuario_service.core.dtos.especialidadesDoMedico.AssociarEspecialidadeMedicoDto;
import com.fiap.techchallenge.usuario_service.core.gateways.UsuarioRepository;
import com.fiap.techchallenge.usuario_service.core.gateways.EspecialidadeRepository;
import com.fiap.techchallenge.usuario_service.core.exceptions.BusinessException;
import com.fiap.techchallenge.usuario_service.infrastructure.services.ValidarUsuarioExistente;
import com.fiap.techchallenge.usuario_service.core.utils.ValidarPerfilProfessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AssociarEspecialidadeAoMedico {

    private final UsuarioRepository usuarioRepository;
    private final EspecialidadeRepository especialidadeRepository;
    private final ValidarUsuarioExistente validarUsuarioExistente;
    private final ValidarPerfilProfessor validarPerfilProfessor;

    public void execute(Long usuarioId, AssociarEspecialidadeMedicoDto dto) {
        Usuario usuario = validarUsuarioExistente.execute(usuarioId);
        validarPerfilProfessor.execute(usuario);
        Especialidade especialidade = especialidadeRepository.findById(dto.getEspecialidadeId())
                .orElseThrow(() -> new BusinessException("especialidade.nao.encontrada"));

        validarEspecialidadeJaAssociada(usuario, dto.getEspecialidadeId());

        validarLimiteEspecialidades(usuario);

        usuario.getEspecialidades().add(especialidade);
        usuarioRepository.save(usuario);
    }

    private void validarLimiteEspecialidades(Usuario usuario) {
        if (usuario.getEspecialidades() != null && usuario.getEspecialidades().size() >= 3) {
            throw new BusinessException("professor.limite.especialidades");
        }
    }

    private void validarEspecialidadeJaAssociada(Usuario usuario, Long especialidadeId) {
        if (usuario.getEspecialidades() != null &&
                usuario.getEspecialidades().stream().anyMatch(e -> e.getId().equals(especialidadeId))) {
            throw new BusinessException("especialidade.ja.associada");
        }
    }
}

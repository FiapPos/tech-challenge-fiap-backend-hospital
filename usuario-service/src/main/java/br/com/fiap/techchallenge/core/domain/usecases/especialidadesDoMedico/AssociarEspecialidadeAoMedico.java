package br.com.fiap.techchallenge.core.domain.usecases.especialidadesDoMedico;

import br.com.fiap.techchallenge.core.domain.entities.Usuario;
import br.com.fiap.techchallenge.core.domain.entities.Especialidade;
import br.com.fiap.techchallenge.core.dtos.especialidadesDoMedico.AssociarEspecialidadeMedicoDto;
import br.com.fiap.techchallenge.core.gateways.UsuarioRepository;
import br.com.fiap.techchallenge.core.gateways.EspecialidadeRepository;
import br.com.fiap.techchallenge.core.exceptions.BusinessException;
import br.com.fiap.techchallenge.infrastructure.services.ValidarUsuarioExistente;
import br.com.fiap.techchallenge.core.utils.ValidarPerfilMedico;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AssociarEspecialidadeAoMedico {

    private final UsuarioRepository usuarioRepository;
    private final EspecialidadeRepository especialidadeRepository;
    private final ValidarUsuarioExistente validarUsuarioExistente;
    private final ValidarPerfilMedico validarPerfilMedico;

    public void execute(Long usuarioId, AssociarEspecialidadeMedicoDto dto) {
        Usuario usuario = validarUsuarioExistente.execute(usuarioId);
        validarPerfilMedico.execute(usuario);
        Especialidade especialidade = especialidadeRepository.findById(dto.getEspecialidadeId())
                .orElseThrow(() -> new BusinessException("especialidade.nao.encontrada"));

        validarEspecialidadeJaAssociada(usuario, dto.getEspecialidadeId());

        validarLimiteEspecialidades(usuario);

        usuario.getEspecialidades().add(especialidade);
        usuarioRepository.save(usuario);
    }

    private void validarLimiteEspecialidades(Usuario usuario) {
        if (usuario.getEspecialidades() != null && usuario.getEspecialidades().size() >= 3) {
            throw new BusinessException("medico.limite.especialidades");
        }
    }

    private void validarEspecialidadeJaAssociada(Usuario usuario, Long especialidadeId) {
        if (usuario.getEspecialidades() != null &&
                usuario.getEspecialidades().stream().anyMatch(e -> e.getId().equals(especialidadeId))) {
            throw new BusinessException("especialidade.ja.associada");
        }
    }
}

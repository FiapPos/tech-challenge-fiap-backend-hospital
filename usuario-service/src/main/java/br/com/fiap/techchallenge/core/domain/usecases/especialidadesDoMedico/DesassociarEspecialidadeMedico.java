package br.com.fiap.techchallenge.core.domain.usecases.especialidadesDoMedico;

import br.com.fiap.techchallenge.core.domain.entities.Usuario;
import br.com.fiap.techchallenge.core.gateways.UsuarioRepository;
import br.com.fiap.techchallenge.core.gateways.EspecialidadeRepository;
import br.com.fiap.techchallenge.infrastructure.services.ValidarUsuarioExistente;
import br.com.fiap.techchallenge.core.utils.ValidarPerfilMedico;
import br.com.fiap.techchallenge.core.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DesassociarEspecialidadeMedico {

    private final UsuarioRepository usuarioRepository;
    private final EspecialidadeRepository especialidadeRepository;
    private final ValidarUsuarioExistente validarUsuarioExistente;
    private final ValidarPerfilMedico validarPerfilMedico;

    public void execute(Long usuarioId, Long especialidadeId) {
        if (especialidadeId == null) {
            throw new BusinessException("especialidade.id.obrigatorio");
        }
        Usuario usuario = validarUsuarioExistente.execute(usuarioId);
        validarPerfilMedico.execute(usuario);

        boolean removed = usuario.getEspecialidades().removeIf(e -> e.getId().equals(especialidadeId));
        if (!removed) {
            throw new BusinessException("especialidade.nao.associada");
        }
        usuarioRepository.save(usuario);
    }
}

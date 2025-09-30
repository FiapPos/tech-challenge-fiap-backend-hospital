package br.com.fiap.techchallenge.core.domain.usecases.usuario;

import br.com.fiap.techchallenge.core.dtos.usuario.CriarUsuarioComandoDto;
import br.com.fiap.techchallenge.core.domain.entities.Usuario;
import br.com.fiap.techchallenge.core.domain.entities.Especialidade;
import br.com.fiap.techchallenge.core.gateways.UsuarioRepository;
import br.com.fiap.techchallenge.core.gateways.EspecialidadeRepository;
import br.com.fiap.techchallenge.infrastructure.services.ValidarLoginExistente;
import br.com.fiap.techchallenge.infrastructure.services.ValidarEmailExistente;
import br.com.fiap.techchallenge.infrastructure.services.ValidarCpfExistente;
import br.com.fiap.techchallenge.core.utils.usuario.CriarUsuarioBase;
import br.com.fiap.techchallenge.core.enums.Perfil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class CriarUsuarioComando {
    private final UsuarioRepository usuarioRepository;
    private final EspecialidadeRepository especialidadeRepository;
    private final ValidarEmailExistente validarEmailExistente;
    private final ValidarCpfExistente validarCpfExistente;
    private final ValidarLoginExistente validarLoginExistente;
    private final CriarUsuarioBase criarUsuarioBase;

    public Usuario execute(CriarUsuarioComandoDto dto) {
        validarDadosUsuario(dto);
        Usuario usuario = criarUsuarioBase.execute(dto);
        associarPerfis(usuario, dto);

        if (Perfil.MEDICO.getCodigo().equals(dto.getPerfilId())) {
            associarEspecialidadesSeMedico(usuario, dto);
        }
        return salvarUsuario(usuario);
    }

    private void validarDadosUsuario(CriarUsuarioComandoDto dto) {
        validarEmailExistente.execute(dto.getEmail());
        validarCpfExistente.execute(dto.getCpf());
        validarLoginExistente.execute(dto.getLogin());
    }

    private void associarPerfis(Usuario usuario, CriarUsuarioComandoDto dto) {
        Perfil.fromCodigo(dto.getPerfilId());
        usuario.setPerfilId(dto.getPerfilId());
    }

    private void associarEspecialidadesSeMedico(Usuario usuario, CriarUsuarioComandoDto dto) {

        if (dto.getEspecialidadeIds() == null || dto.getEspecialidadeIds().isEmpty())
            return;

        var especialidades = dto.getEspecialidadeIds().stream()
                .distinct()
                .map(especialidadeRepository::findById)
                .filter(java.util.Optional::isPresent)
                .map(java.util.Optional::get)
                .map(e -> {
                    Especialidade esp = new Especialidade();
                    esp.setId(e.getId());
                    return esp;
                })
                .toList();
        usuario.setEspecialidades(especialidades);
    }

    private Usuario salvarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
}

package com.fiap.techchallenge.usuario_service.core.domain.usecases.usuario;

import com.fiap.techchallenge.usuario_service.core.dtos.usuario.CriarUsuarioComandoDto;
import com.fiap.techchallenge.usuario_service.core.domain.entities.Usuario;
import com.fiap.techchallenge.usuario_service.core.domain.entities.Especialidade;
import com.fiap.techchallenge.usuario_service.core.gateways.UsuarioRepository;
import com.fiap.techchallenge.usuario_service.core.gateways.EspecialidadeRepository;
import com.fiap.techchallenge.usuario_service.infrastructure.services.ValidarLoginExistente;
import com.fiap.techchallenge.usuario_service.infrastructure.services.ValidarEmailExistente;
import com.fiap.techchallenge.usuario_service.infrastructure.services.ValidarCpfExistente;
import com.fiap.techchallenge.usuario_service.core.utils.usuario.CriarUsuarioBase;
import com.fiap.techchallenge.usuario_service.core.enums.Perfil;
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

        if (Perfil.PROFESSOR.getCodigo().equals(dto.getPerfilId())) {
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

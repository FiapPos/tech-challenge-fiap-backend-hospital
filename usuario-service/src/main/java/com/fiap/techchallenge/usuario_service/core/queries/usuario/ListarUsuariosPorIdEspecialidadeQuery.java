package com.fiap.techchallenge.usuario_service.core.queries.usuario;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Usuario;
import com.fiap.techchallenge.usuario_service.core.gateways.EspecialidadeRepository;
import com.fiap.techchallenge.usuario_service.core.gateways.UsuarioRepository;
import com.fiap.techchallenge.usuario_service.core.queries.perfil.ListarPorPerfilUsuario;
import com.fiap.techchallenge.usuario_service.core.queries.resultadoItem.usuario.ListarUsuariosResultadoItem;
import com.fiap.techchallenge.usuario_service.core.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListarUsuariosPorIdEspecialidadeQuery {

    private final UsuarioRepository usuarioRepository;
    private final EspecialidadeRepository especialidadeRepository;
    private final ListarPorPerfilUsuario listarPorPerfilUsuario;

    public List<ListarUsuariosResultadoItem> execute(Long especialidadeId) {
        validarEspecialidadeExistente(especialidadeId);

        List<Usuario> usuarios = usuarioRepository.findAll().stream()
                .filter(u -> u.getEspecialidades() != null
                        && u.getEspecialidades().stream()
                                .anyMatch(e -> e.getId() != null && e.getId().equals(especialidadeId)))
                .toList();

        return usuarios.stream()
                .map(this::mapToResultadoItem)
                .toList();
    }

    private void validarEspecialidadeExistente(Long especialidadeId) {
        especialidadeRepository.findById(especialidadeId)
                .orElseThrow(() -> new BusinessException("especialidade.nao.encontrada"));
    }

    private ListarUsuariosResultadoItem mapToResultadoItem(Usuario usuario) {
        return ListarUsuariosResultadoItem.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .cpf(usuario.getCpf())
                .telefone(usuario.getTelefone())
                .login(usuario.getLogin())
                .perfil(listarPorPerfilUsuario.execute(usuario.getPerfis()))
                .dataNascimento(usuario.getDataNascimento())
                .dataCriacao(usuario.getDataCriacao())
                .dataAtualizacao(usuario.getDataAtualizacao())
                .build();
    }
}

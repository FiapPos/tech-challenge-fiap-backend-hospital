package br.com.fiap.techchallenge.core.queries.usuario;

import br.com.fiap.techchallenge.core.domain.entities.Usuario;
import br.com.fiap.techchallenge.core.gateways.EspecialidadeRepository;
import br.com.fiap.techchallenge.core.gateways.UsuarioRepository;
import br.com.fiap.techchallenge.core.queries.params.ListarUsuariosParams;
import br.com.fiap.techchallenge.core.queries.perfil.ListarPorPerfilUsuario;
import br.com.fiap.techchallenge.core.queries.resultadoItem.usuario.ListarUsuariosResultadoItem;
import br.com.fiap.techchallenge.core.exceptions.BusinessException;
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

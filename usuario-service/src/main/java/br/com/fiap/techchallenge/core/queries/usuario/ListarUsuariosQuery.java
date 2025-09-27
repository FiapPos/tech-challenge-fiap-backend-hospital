package br.com.fiap.techchallenge.core.queries.usuario;

import br.com.fiap.techchallenge.core.domain.entities.Usuario;
import br.com.fiap.techchallenge.core.gateways.UsuarioRepository;
import br.com.fiap.techchallenge.core.queries.params.ListarUsuariosParams;
import br.com.fiap.techchallenge.core.queries.resultadoItem.usuario.ListarUsuariosResultadoItem;
import br.com.fiap.techchallenge.core.queries.perfil.ListarPorPerfilUsuario;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListarUsuariosQuery {

    private final UsuarioRepository usuarioRepository;
    private final ListarPorPerfilUsuario listarPorPerfilUsuario;

    public List<ListarUsuariosResultadoItem> execute(ListarUsuariosParams params) {
        List<Usuario> usuarios = buscarUsuarios(params);
        return mapToResultadoItemList(usuarios);
    }

    private List<ListarUsuariosResultadoItem> mapToResultadoItemList(List<Usuario> usuarios) {
        return usuarios.stream()
                .map(this::mapToResultadoItem)
                .collect(Collectors.toList());
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

    private List<Usuario> buscarUsuarios(ListarUsuariosParams params) {
        if (params.getAtivo() == null) {
            return usuarioRepository.findAll();
        }
        return usuarioRepository.findByAtivo(params.getAtivo());
    }
}
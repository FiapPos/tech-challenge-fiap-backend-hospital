package com.fiap.techchallenge.usuario_service.core.queries.usuario;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Usuario;
import com.fiap.techchallenge.usuario_service.core.exceptions.NotFoundException;
import com.fiap.techchallenge.usuario_service.core.gateways.UsuarioRepository;
import com.fiap.techchallenge.usuario_service.core.queries.perfil.ListarPorPerfilUsuario;
import com.fiap.techchallenge.usuario_service.core.queries.resultadoItem.usuario.ListarUsuariosResultadoItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListarUsuarioPorLoginQuery {

    private final UsuarioRepository usuarioRepository;
    private final ListarPorPerfilUsuario listarPorPerfilUsuario;

    public ListarUsuariosResultadoItem execute(String login) {
        Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new NotFoundException("usuario.nao.encontrado"));
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

package com.fiap.techchallenge.usuario_service.core.queries.usuario;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Usuario;
import com.fiap.techchallenge.usuario_service.core.enums.Perfil;
import com.fiap.techchallenge.usuario_service.core.exceptions.NotFoundException;
import com.fiap.techchallenge.usuario_service.core.gateways.UsuarioRepository;
import com.fiap.techchallenge.usuario_service.core.queries.resultadoItem.usuario.EncontraUsuarioItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BuscaUsuarioPorIdQuery {

    private final UsuarioRepository usuarioRepository;

    public EncontraUsuarioItem execute(Long id, Perfil perfil, Optional<Long> especialidadeId) {

        Optional<Usuario> usuarioOptional = especialidadeId
                .flatMap(espId -> usuarioRepository.findByIdAndPerfilAndEspecialidadeId(id, perfil, espId))
                .or(() -> usuarioRepository.findByIdAndPerfil(id, perfil));

        Usuario usuario = usuarioOptional.orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        return EncontraUsuarioItem.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .chatId(usuario.getChatId() == null ? 0L : usuario.getChatId())
                .build();
    }


}

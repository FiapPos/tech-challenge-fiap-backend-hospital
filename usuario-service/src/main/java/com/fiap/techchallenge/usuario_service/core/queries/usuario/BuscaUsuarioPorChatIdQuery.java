package com.fiap.techchallenge.usuario_service.core.queries.usuario;

import com.fiap.techchallenge.usuario_service.core.exceptions.NotFoundException;
import com.fiap.techchallenge.usuario_service.core.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuscaUsuarioPorChatIdQuery {

    private final UsuarioRepository usuarioRepository;

    public Long execute(Long chatId) {
        return usuarioRepository
                .findByChatId(chatId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"))
                .getId();
    }


}

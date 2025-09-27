package br.com.fiap.techchallenge.infrastructure.services;

import br.com.fiap.techchallenge.core.exceptions.BadRequestException;
import br.com.fiap.techchallenge.core.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidarLoginExistente {
    private final UsuarioRepository usuarioRepository;

    public void execute(String login) {
        if (usuarioRepository.existsByLogin(login)) {
            throw new BadRequestException("login.duplicado");
        }
    }
}
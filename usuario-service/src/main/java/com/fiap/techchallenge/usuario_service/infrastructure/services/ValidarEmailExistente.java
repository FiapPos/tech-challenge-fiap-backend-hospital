package com.fiap.techchallenge.usuario_service.infrastructure.services;

import com.fiap.techchallenge.usuario_service.core.exceptions.BadRequestException;
import com.fiap.techchallenge.usuario_service.core.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidarEmailExistente {

    private final UsuarioRepository usuarioRepository;

    public void execute(String email) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new BadRequestException("email.duplicado");
        }
    }
}
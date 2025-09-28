package com.fiap.techchallenge.usuario_service.infrastructure.services;

import com.fiap.techchallenge.usuario_service.core.exceptions.BadRequestException;
import com.fiap.techchallenge.usuario_service.core.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidarCpfExistente {
    private final UsuarioRepository usuarioRepository;

    public void execute(String cpf) {
        if (usuarioRepository.existsByCpf(cpf)) {
            throw new BadRequestException("cpf.duplicado");
        }
    }
}

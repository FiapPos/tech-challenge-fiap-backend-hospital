package com.fiap.techchallenge.usuario_service.core.utils;

import com.fiap.techchallenge.usuario_service.core.exceptions.BadRequestException;
import com.fiap.techchallenge.usuario_service.core.gateways.EnderecoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidarCepDoUsuario {

    private final EnderecoRepository enderecoRepository;

    public void validarCepDuplicado(Long usuarioId, String cep) {
        if (enderecoRepository.existsByUsuarioIdAndCep(usuarioId, cep)) {
            throw new BadRequestException("endereco.cep.duplicado");
        }
    }
}

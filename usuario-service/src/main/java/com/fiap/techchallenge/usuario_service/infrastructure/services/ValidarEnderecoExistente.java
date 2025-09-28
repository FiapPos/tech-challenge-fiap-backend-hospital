package com.fiap.techchallenge.usuario_service.infrastructure.services;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Endereco;
import com.fiap.techchallenge.usuario_service.core.exceptions.BadRequestException;
import com.fiap.techchallenge.usuario_service.core.gateways.EnderecoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidarEnderecoExistente {

    private final EnderecoRepository enderecoRepository;

    public Endereco execute(Long id) {
        return enderecoRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("endereco.nao.encontrado"));
    }

}
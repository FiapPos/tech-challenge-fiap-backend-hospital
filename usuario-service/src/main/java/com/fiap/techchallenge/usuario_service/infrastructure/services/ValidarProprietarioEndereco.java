package com.fiap.techchallenge.usuario_service.infrastructure.services;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Endereco;
import com.fiap.techchallenge.usuario_service.core.exceptions.BadRequestException;
import com.fiap.techchallenge.usuario_service.core.gateways.EnderecoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidarProprietarioEndereco {

    private final EnderecoRepository enderecoRepository;

    public void execute(Long enderecoId, Long usuarioId) {
        Endereco endereco = enderecoRepository.findById(enderecoId)
                .orElseThrow(() -> new BadRequestException("endereco.nao.encontrado"));

        if (!endereco.getUsuario().getId().equals(usuarioId)) {
            throw new BadRequestException("endereco.nao.pertence.ao.usuario");
        }
    }
}
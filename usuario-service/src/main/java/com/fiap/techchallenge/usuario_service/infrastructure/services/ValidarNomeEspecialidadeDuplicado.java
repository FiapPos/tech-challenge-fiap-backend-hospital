package com.fiap.techchallenge.usuario_service.infrastructure.services;

import com.fiap.techchallenge.usuario_service.core.exceptions.BadRequestException;
import com.fiap.techchallenge.usuario_service.core.gateways.EspecialidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidarNomeEspecialidadeDuplicado {

    private final EspecialidadeRepository especialidadeRepository;

    public void execute(String nome) {
        if (especialidadeRepository.existsByNomeIgnoreCase(nome)) {
            throw new BadRequestException("nome.especialidade.duplicado");
        }
    }
}
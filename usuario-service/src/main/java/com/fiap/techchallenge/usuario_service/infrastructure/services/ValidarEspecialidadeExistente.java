package com.fiap.techchallenge.usuario_service.infrastructure.services;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Especialidade;
import com.fiap.techchallenge.usuario_service.core.exceptions.BadRequestException;
import com.fiap.techchallenge.usuario_service.core.gateways.EspecialidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidarEspecialidadeExistente {

    private final EspecialidadeRepository especialidadeRepository;

    public Especialidade execute(Long id) {
        return especialidadeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("especialidade.nao.encontrado"));
    }
}
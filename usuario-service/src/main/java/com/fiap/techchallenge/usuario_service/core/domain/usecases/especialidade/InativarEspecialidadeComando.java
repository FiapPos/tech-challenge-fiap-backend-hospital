package com.fiap.techchallenge.usuario_service.core.domain.usecases.especialidade;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Especialidade;
import com.fiap.techchallenge.usuario_service.core.gateways.EspecialidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.fiap.techchallenge.usuario_service.infrastructure.services.ValidarEspecialidadeExistente;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class InativarEspecialidadeComando {

    private final EspecialidadeRepository repository;
    private final ValidarEspecialidadeExistente validarEspecialidadeExistente;

    public Especialidade execute(Long id) {
        Especialidade existente = validarEspecialidadeExistente.execute(id);

        if (!existente.isAtivo()) {
            return existente;
        }

        existente.setAtivo(false);
        existente.setDataAtualizacao(LocalDateTime.now());

        return repository.save(existente);
    }
}

package com.fiap.techchallenge.usuario_service.core.gateways;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Especialidade;

import java.util.List;
import java.util.Optional;

public interface EspecialidadeRepository {
    Especialidade save(Especialidade especialidade);

    Optional<Especialidade> findById(Long id);

    Optional<Especialidade> findByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCase(String nome);

    List<Especialidade> findAll();
}

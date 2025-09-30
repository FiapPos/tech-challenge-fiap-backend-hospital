package com.fiap.techchallenge.usuario_service.infrastructure.data.repositories;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Especialidade;
import com.fiap.techchallenge.usuario_service.core.gateways.EspecialidadeRepository;
import com.fiap.techchallenge.usuario_service.infrastructure.data.entities.EspecialidadeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EspecialidadeRepositoryImpl implements EspecialidadeRepository {

    private final JpaEspecialidadeRepository jpaRepository;

    @Override
    public Especialidade save(Especialidade especialidade) {
        EspecialidadeEntity entity = EspecialidadeEntity.fromDomain(especialidade);
        return jpaRepository.save(entity).toDomain();
    }

    @Override
    public Optional<Especialidade> findById(Long id) {
        return jpaRepository.findById(id).map(EspecialidadeEntity::toDomain);
    }

    @Override
    public Optional<Especialidade> findByNomeIgnoreCase(String nome) {
        return jpaRepository.findByNomeIgnoreCase(nome).map(EspecialidadeEntity::toDomain);
    }

    @Override
    public boolean existsByNomeIgnoreCase(String nome) {
        return jpaRepository.existsByNomeIgnoreCase(nome);
    }

    @Override
    public List<Especialidade> findAll() {
        return jpaRepository.findAll().stream().map(EspecialidadeEntity::toDomain).toList();
    }
}

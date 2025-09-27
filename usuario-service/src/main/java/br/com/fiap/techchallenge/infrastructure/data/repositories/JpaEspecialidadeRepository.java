package br.com.fiap.techchallenge.infrastructure.data.repositories;

import br.com.fiap.techchallenge.infrastructure.data.entities.EspecialidadeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaEspecialidadeRepository extends JpaRepository<EspecialidadeEntity, Long> {
    boolean existsByNomeIgnoreCase(String nome);

    Optional<EspecialidadeEntity> findByNomeIgnoreCase(String nome);
}

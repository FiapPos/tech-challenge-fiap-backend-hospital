package br.com.fiap.techchallenge.infrastructure.data.repositories;

import br.com.fiap.techchallenge.infrastructure.data.entities.PerfilEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilUsuarioJpaRepository extends JpaRepository<PerfilEntity, Long> {
}
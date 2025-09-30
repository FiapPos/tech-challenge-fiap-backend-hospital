package com.fiap.techchallenge.usuario_service.infrastructure.data.repositories;

import com.fiap.techchallenge.usuario_service.infrastructure.data.entities.EnderecoEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnderecoJpaRepository extends JpaRepository<EnderecoEntity, Long> {

    @Query("SELECT e FROM EnderecoEntity e JOIN FETCH e.usuario WHERE e.usuario.id = :usuarioId")
    List<EnderecoEntity> findByUsuarioId(@Param("usuarioId") Long usuarioId, Sort sort);

    boolean existsByUsuarioIdAndCep(Long usuarioId, String cep);
}
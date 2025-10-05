package com.fiap.techchallenge.usuario_service.infrastructure.data.repositories;

import com.fiap.techchallenge.usuario_service.core.enums.Perfil;
import com.fiap.techchallenge.usuario_service.infrastructure.data.entities.UsuarioEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, Long> {

    boolean existsByEmail(String email);

    boolean existsByLogin(String login);

    boolean existsByCpf(String cpf);

    List<UsuarioEntity> findByAtivo(boolean ativo, Sort sort);

    @EntityGraph(attributePaths = { "perfil", "especialidades" })
    Optional<UsuarioEntity> findByLogin(String login);

    List<UsuarioEntity> findByEspecialidadesNome(String nome);

    Optional<UsuarioEntity> findByIdAndEspecialidadesNome(Long id, String nome);

    Optional<UsuarioEntity> findByIdAndPerfil_NomePerfil(Long usuarioId, Perfil perfil);

    @Query("""
            SELECT u
            FROM UsuarioEntity u
            JOIN u.especialidades e
            WHERE u.id = :usuarioId
            AND u.perfil.id = :#{#perfil.codigo}
            AND e.id = :especialidadeId
            """)
    Optional<UsuarioEntity> findByIdAndPerfil_NomePerfilAndEspecialidadeId(@Param("usuarioId") Long usuarioId,
            @Param("perfil") Perfil perfil, @Param("especialidadeId") Long especialidadeId);
}
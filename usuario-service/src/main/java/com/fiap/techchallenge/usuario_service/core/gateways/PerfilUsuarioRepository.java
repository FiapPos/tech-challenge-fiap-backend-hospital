package com.fiap.techchallenge.usuario_service.core.gateways;

import com.fiap.techchallenge.usuario_service.core.domain.entities.PerfilDoUsuario;

import java.util.List;
import java.util.Optional;

public interface PerfilUsuarioRepository {
    PerfilDoUsuario save(PerfilDoUsuario perfilUsuario);

    Optional<PerfilDoUsuario> findById(Long id);

    List<PerfilDoUsuario> findAll();

    void deleteById(Long id);

    boolean existsById(Long id);

    void deleteAll();
}
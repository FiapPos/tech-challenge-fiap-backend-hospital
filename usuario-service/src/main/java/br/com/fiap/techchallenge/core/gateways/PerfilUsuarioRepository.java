package br.com.fiap.techchallenge.core.gateways;

import br.com.fiap.techchallenge.core.domain.entities.PerfilDoUsuario;

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
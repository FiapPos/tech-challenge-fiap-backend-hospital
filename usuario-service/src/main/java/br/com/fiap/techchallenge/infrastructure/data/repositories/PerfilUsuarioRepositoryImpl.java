package br.com.fiap.techchallenge.infrastructure.data.repositories;

import br.com.fiap.techchallenge.core.domain.entities.PerfilDoUsuario;
import br.com.fiap.techchallenge.core.gateways.PerfilUsuarioRepository;
import br.com.fiap.techchallenge.infrastructure.data.entities.PerfilEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PerfilUsuarioRepositoryImpl implements PerfilUsuarioRepository {

    private final PerfilUsuarioJpaRepository jpaRepository;

    public PerfilUsuarioRepositoryImpl(PerfilUsuarioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public PerfilDoUsuario save(PerfilDoUsuario perfilUsuario) {
        PerfilEntity entity = PerfilEntity.fromDomain(perfilUsuario);
        PerfilEntity savedEntity = jpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<PerfilDoUsuario> findById(Long id) {
        return jpaRepository.findById(id)
                .map(PerfilEntity::toDomain);
    }

    @Override
    public List<PerfilDoUsuario> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(PerfilEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
}
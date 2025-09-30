package com.fiap.techchallenge.usuario_service.infrastructure.data.repositories;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Endereco;
import com.fiap.techchallenge.usuario_service.core.gateways.EnderecoRepository;
import com.fiap.techchallenge.usuario_service.infrastructure.data.entities.EnderecoEntity;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class EnderecoRepositoryImpl implements EnderecoRepository {

    private final EnderecoJpaRepository jpaRepository;

    public EnderecoRepositoryImpl(EnderecoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Endereco save(Endereco endereco) {
        EnderecoEntity entity = EnderecoEntity.fromDomain(endereco);
        EnderecoEntity savedEntity = jpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Endereco> findById(Long id) {
        return jpaRepository.findById(id)
                .map(EnderecoEntity::toDomain);
    }

    @Override
    public List<Endereco> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(EnderecoEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public List<Endereco> findByUsuarioId(Long usuarioId, Sort sort) {
        return jpaRepository.findByUsuarioId(usuarioId, sort)
                .stream()
                .map(EnderecoEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByUsuarioIdAndCep(Long usuarioId, String cep) {
        return jpaRepository.existsByUsuarioIdAndCep(usuarioId, cep);
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
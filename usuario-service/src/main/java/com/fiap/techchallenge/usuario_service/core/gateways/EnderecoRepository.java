package com.fiap.techchallenge.usuario_service.core.gateways;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Endereco;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface EnderecoRepository {

    Endereco save(Endereco endereco);

    Optional<Endereco> findById(Long id);

    List<Endereco> findAll();

    void deleteById(Long id);

    List<Endereco> findByUsuarioId(Long usuarioId, Sort sort);

    boolean existsByUsuarioIdAndCep(Long usuarioId, String cep);

    boolean existsById(Long id);

    void deleteAll();
}

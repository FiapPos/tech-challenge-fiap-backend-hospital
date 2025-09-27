package br.com.fiap.techchallenge.core.domain.usecases.especialidade;

import br.com.fiap.techchallenge.core.domain.entities.Especialidade;
import br.com.fiap.techchallenge.core.gateways.EspecialidadeRepository;
import br.com.fiap.techchallenge.core.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import br.com.fiap.techchallenge.infrastructure.services.ValidarEspecialidadeExistente;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class InativarEspecialidadeComando {

    private final EspecialidadeRepository repository;
    private final ValidarEspecialidadeExistente validarEspecialidadeExistente;

    public Especialidade execute(Long id) {
        Especialidade existente = validarEspecialidadeExistente.execute(id);

        if (!existente.isAtivo()) {
            return existente;
        }

        existente.setAtivo(false);
        existente.setDataAtualizacao(LocalDateTime.now());

        return repository.save(existente);
    }
}

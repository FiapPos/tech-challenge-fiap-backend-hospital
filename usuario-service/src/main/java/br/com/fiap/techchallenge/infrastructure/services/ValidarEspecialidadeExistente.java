package br.com.fiap.techchallenge.infrastructure.services;

import br.com.fiap.techchallenge.core.domain.entities.Especialidade;
import br.com.fiap.techchallenge.core.exceptions.BadRequestException;
import br.com.fiap.techchallenge.core.gateways.EspecialidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidarEspecialidadeExistente {

    private final EspecialidadeRepository especialidadeRepository;

    public Especialidade execute(Long id) {
        return especialidadeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("especialidade.nao.encontrado"));
    }
}
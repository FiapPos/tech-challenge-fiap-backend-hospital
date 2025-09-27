package br.com.fiap.techchallenge.infrastructure.services;

import br.com.fiap.techchallenge.core.exceptions.BadRequestException;
import br.com.fiap.techchallenge.core.gateways.EspecialidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidarNomeEspecialidadeDuplicado {

    private final EspecialidadeRepository especialidadeRepository;

    public void execute(String nome) {
        if (especialidadeRepository.existsByNomeIgnoreCase(nome)) {
            throw new BadRequestException("nome.especialidade.duplicado");
        }
    }
}
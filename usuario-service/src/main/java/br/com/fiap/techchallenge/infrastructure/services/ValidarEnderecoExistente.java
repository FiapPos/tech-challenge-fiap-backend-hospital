package br.com.fiap.techchallenge.infrastructure.services;

import br.com.fiap.techchallenge.core.domain.entities.Endereco;
import br.com.fiap.techchallenge.core.exceptions.BadRequestException;
import br.com.fiap.techchallenge.core.gateways.EnderecoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidarEnderecoExistente {

    private final EnderecoRepository enderecoRepository;

    public Endereco execute(Long id) {
        return enderecoRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("endereco.nao.encontrado"));
    }

}
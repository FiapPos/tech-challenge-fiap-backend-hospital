package br.com.fiap.techchallenge.core.utils;

import br.com.fiap.techchallenge.core.exceptions.BadRequestException;
import br.com.fiap.techchallenge.core.gateways.EnderecoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidarCepDoUsuario {

    private final EnderecoRepository enderecoRepository;

    public void validarCepDuplicado(Long usuarioId, String cep) {
        if (enderecoRepository.existsByUsuarioIdAndCep(usuarioId, cep)) {
            throw new BadRequestException("endereco.cep.duplicado");
        }
    }
}

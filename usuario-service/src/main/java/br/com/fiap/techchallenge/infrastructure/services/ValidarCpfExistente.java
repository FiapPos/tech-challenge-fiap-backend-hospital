package br.com.fiap.techchallenge.infrastructure.services;

import br.com.fiap.techchallenge.core.exceptions.BadRequestException;
import br.com.fiap.techchallenge.core.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidarCpfExistente {
    private final UsuarioRepository usuarioRepository;

    public void execute(String cpf) {
        if (usuarioRepository.existsByCpf(cpf)) {
            throw new BadRequestException("cpf.duplicado");
        }
    }
}

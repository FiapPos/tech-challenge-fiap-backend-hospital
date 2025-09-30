package com.fiap.techchallenge.usuario_service.core.domain.usecases.endereco;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Endereco;
import com.fiap.techchallenge.usuario_service.core.dtos.endereco.DeletarEnderecoComandoDto;
import com.fiap.techchallenge.usuario_service.core.gateways.EnderecoRepository;
import com.fiap.techchallenge.usuario_service.infrastructure.services.ValidarUsuarioExistente;
import com.fiap.techchallenge.usuario_service.infrastructure.services.ValidarEnderecoExistente;
import com.fiap.techchallenge.usuario_service.infrastructure.services.ValidarProprietarioEndereco;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeletarEnderecoComando {

    private final EnderecoRepository enderecoRepository;
    private final ValidarEnderecoExistente validarEnderecoExistente;
    private final ValidarUsuarioExistente validarUsuarioExistente;
    private final ValidarProprietarioEndereco validarProprietarioEndereco;

    public void execute(Long usuarioId, DeletarEnderecoComandoDto dto) {
        validarUsuarioExistente.execute(usuarioId);
        validarProprietarioEndereco.execute(dto.getEnderecoId(), usuarioId);
        Endereco endereco = validarEnderecoExistente.execute(dto.getEnderecoId());
        deletarEndereco(endereco);
    }

    private void deletarEndereco(Endereco endereco) {
        enderecoRepository.deleteById(endereco.getId());
    }
}
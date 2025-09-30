package br.com.fiap.techchallenge.core.domain.usecases.endereco;

import br.com.fiap.techchallenge.core.domain.entities.Endereco;
import br.com.fiap.techchallenge.core.dtos.endereco.DeletarEnderecoComandoDto;
import br.com.fiap.techchallenge.core.gateways.EnderecoRepository;
import br.com.fiap.techchallenge.infrastructure.services.ValidarUsuarioExistente;
import br.com.fiap.techchallenge.infrastructure.services.ValidarEnderecoExistente;
import br.com.fiap.techchallenge.infrastructure.services.ValidarProprietarioEndereco;
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
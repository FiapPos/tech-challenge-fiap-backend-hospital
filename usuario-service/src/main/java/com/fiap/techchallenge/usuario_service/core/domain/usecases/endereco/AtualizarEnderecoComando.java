package com.fiap.techchallenge.usuario_service.core.domain.usecases.endereco;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Endereco;
import com.fiap.techchallenge.usuario_service.core.dtos.endereco.AtualizarEnderecoComandoDto;
import com.fiap.techchallenge.usuario_service.core.gateways.EnderecoRepository;
import com.fiap.techchallenge.usuario_service.core.shared.CompartilhadoService;
import com.fiap.techchallenge.usuario_service.core.utils.ValidarCepDoUsuario;
import com.fiap.techchallenge.usuario_service.infrastructure.services.ValidarEnderecoExistente;
import com.fiap.techchallenge.usuario_service.infrastructure.services.ValidarProprietarioEndereco;
import com.fiap.techchallenge.usuario_service.core.utils.endereco.ValidarCamposEndereco;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AtualizarEnderecoComando {

    private final EnderecoRepository enderecoRepository;
    private final ValidarEnderecoExistente validarEnderecoExistente;
    private final CompartilhadoService sharedService;
    private final ValidarCepDoUsuario validarCepDoUsuario;
    private final ValidarProprietarioEndereco validarProprietarioEndereco;
    private final ValidarCamposEndereco validarCamposEndereco;

    public Endereco execute(Long id, AtualizarEnderecoComandoDto dto, Long usuarioId) {

        validarCamposEndereco.validar(dto.getRua(), dto.getCep(), dto.getNumero(), dto.getBairro(), dto.getCidade());
        validarProprietarioEndereco.execute(id, usuarioId);
        Endereco endereco = validarEnderecoExistente.execute(id);
        validarCepDoUsuario.validarCepDuplicado(usuarioId, dto.getCep());
        atualizarCampos(endereco, dto);
        return enderecoRepository.save(endereco);
    }

    private void atualizarCampos(Endereco endereco, AtualizarEnderecoComandoDto dto) {
        atualizarRua(endereco, dto.getRua());
        atualizarCep(endereco, dto.getCep());
        atualizarNumero(endereco, dto.getNumero());
        atualizarBairro(endereco, dto.getBairro());
        atualizarCidade(endereco, dto.getCidade());
        endereco.setDataAtualizacao(sharedService.getCurrentDateTime());
    }

    private void atualizarRua(Endereco endereco, String rua) {
        if (rua != null) {
            endereco.setRua(rua);
        }
    }

    private void atualizarCep(Endereco endereco, String cep) {
        if (cep != null) {
            endereco.setCep(cep);
        }
    }

    private void atualizarNumero(Endereco endereco, String numero) {
        if (numero != null) {
            endereco.setNumero(numero);
        }
    }

    private void atualizarBairro(Endereco endereco, String bairro) {
        if (bairro != null) {
            endereco.setBairro(bairro);
        }
    }

    private void atualizarCidade(Endereco endereco, String cidade) {
        if (cidade != null) {
            endereco.setCidade(cidade);
        }
    }

}
package br.com.fiap.techchallenge.usuario_service.core.domain.usecases.endereco;

import br.com.fiap.techchallenge.core.domain.entities.Endereco;
import br.com.fiap.techchallenge.core.dtos.endereco.AtualizarEnderecoComandoDto;
import br.com.fiap.techchallenge.core.domain.usecases.endereco.AtualizarEnderecoComando;
import br.com.fiap.techchallenge.core.gateways.EnderecoRepository;
import br.com.fiap.techchallenge.core.shared.CompartilhadoService;
import br.com.fiap.techchallenge.core.utils.ValidarCepDoUsuario;
import br.com.fiap.techchallenge.core.utils.endereco.ValidarCamposEndereco;
import br.com.fiap.techchallenge.infrastructure.services.ValidarEnderecoExistente;
import br.com.fiap.techchallenge.infrastructure.services.ValidarProprietarioEndereco;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class AtualizarEnderecoComandoTest {

    private EnderecoRepository enderecoRepository;
    private ValidarEnderecoExistente validarEnderecoExistente;
    private CompartilhadoService sharedService;
    private ValidarCepDoUsuario validarCepDoUsuario;
    private ValidarProprietarioEndereco validarProprietarioEndereco;
    private ValidarCamposEndereco validarCamposEndereco;

    private AtualizarEnderecoComando comando;

    @BeforeEach
    void setUp() {
        enderecoRepository = mock(EnderecoRepository.class);
        validarEnderecoExistente = mock(ValidarEnderecoExistente.class);
        sharedService = mock(CompartilhadoService.class);
        validarCepDoUsuario = mock(ValidarCepDoUsuario.class);
        validarProprietarioEndereco = mock(ValidarProprietarioEndereco.class);
        validarCamposEndereco = mock(ValidarCamposEndereco.class);
        comando = new AtualizarEnderecoComando(enderecoRepository, validarEnderecoExistente, sharedService,
                validarCepDoUsuario, validarProprietarioEndereco, validarCamposEndereco);
    }

    @Test
    void deveAtualizarCamposPermitosERegistrarAtualizacao() {
        Long enderecoId = 5L;
        Long usuarioId = 10L;

        AtualizarEnderecoComandoDto dto = new AtualizarEnderecoComandoDto();
        dto.setRua("Nova Rua");
        dto.setCep("99999-000");
        dto.setNumero("200");
        dto.setBairro("Bairro Novo");
        dto.setCidade("Cidade Nova");
        dto.setUsuarioId(usuarioId);

        Endereco existente = new Endereco();
        existente.setId(enderecoId);
        existente.setRua("Antiga Rua");
        existente.setCep("11111-000");
        existente.setNumero("10");
        existente.setBairro("Antigo");
        existente.setCidade("Velha");

        when(validarEnderecoExistente.execute(enderecoId)).thenReturn(existente);
        when(sharedService.getCurrentDateTime()).thenReturn(LocalDateTime.now());
        when(enderecoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Endereco salvo = comando.execute(enderecoId, dto, usuarioId);

        verify(validarCamposEndereco).validar(dto.getRua(), dto.getCep(), dto.getNumero(), dto.getBairro(),
                dto.getCidade());
        verify(validarProprietarioEndereco).execute(enderecoId, usuarioId);
        verify(validarEnderecoExistente).execute(enderecoId);
        verify(validarCepDoUsuario).validarCepDuplicado(usuarioId, dto.getCep());

        ArgumentCaptor<Endereco> captor = ArgumentCaptor.forClass(Endereco.class);
        verify(enderecoRepository).save(captor.capture());
        Endereco atualizado = captor.getValue();

        assertThat(salvo).isSameAs(atualizado);
        assertThat(atualizado.getRua()).isEqualTo("Nova Rua");
        assertThat(atualizado.getCep()).isEqualTo("99999-000");
        assertThat(atualizado.getNumero()).isEqualTo("200");
        assertThat(atualizado.getBairro()).isEqualTo("Bairro Novo");
        assertThat(atualizado.getCidade()).isEqualTo("Cidade Nova");
        assertThat(atualizado.getDataAtualizacao()).isNotNull();
    }
}

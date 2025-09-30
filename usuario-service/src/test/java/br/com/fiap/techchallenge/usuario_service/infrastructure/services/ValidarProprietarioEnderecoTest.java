package br.com.fiap.techchallenge.usuario_service.infrastructure.services;

import br.com.fiap.techchallenge.core.domain.entities.Endereco;
import br.com.fiap.techchallenge.core.domain.entities.Usuario;
import br.com.fiap.techchallenge.core.exceptions.BadRequestException;
import br.com.fiap.techchallenge.core.gateways.EnderecoRepository;
import br.com.fiap.techchallenge.infrastructure.services.ValidarProprietarioEndereco;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidarProprietarioEnderecoTest {

    @Mock
    private EnderecoRepository enderecoRepository;

    @InjectMocks
    private ValidarProprietarioEndereco validarProprietarioEndereco;

    private Usuario usuario;
    private Endereco endereco;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");

        endereco = new Endereco();
        endereco.setId(1L);
        endereco.setRua("Rua das Flores");
        endereco.setCep("12345-678");
        endereco.setNumero("123");
        endereco.setUsuario(usuario);
    }

    @Test
    void deveValidarProprietarioComSucesso() {
        when(enderecoRepository.findById(1L)).thenReturn(Optional.of(endereco));

        assertDoesNotThrow(() -> validarProprietarioEndereco.execute(1L, 1L));

        verify(enderecoRepository).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoEnderecoNaoEncontrado() {
        when(enderecoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> validarProprietarioEndereco.execute(1L, 1L));

        verify(enderecoRepository).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoEnderecoNaoPertenceAoUsuario() {
        when(enderecoRepository.findById(1L)).thenReturn(Optional.of(endereco));

        assertThrows(BadRequestException.class, () -> validarProprietarioEndereco.execute(1L, 2L));

        verify(enderecoRepository).findById(1L);
    }

    @Test
    void deveValidarProprietarioComUsuarioDiferente() {
        Usuario outroUsuario = new Usuario();
        outroUsuario.setId(2L);
        endereco.setUsuario(outroUsuario);

        when(enderecoRepository.findById(1L)).thenReturn(Optional.of(endereco));

        assertDoesNotThrow(() -> validarProprietarioEndereco.execute(1L, 2L));

        verify(enderecoRepository).findById(1L);
    }
}
package br.com.fiap.techchallenge.usuario_service.infrastructure.services;

import br.com.fiap.techchallenge.core.domain.entities.Endereco;
import br.com.fiap.techchallenge.core.exceptions.BadRequestException;
import br.com.fiap.techchallenge.core.gateways.EnderecoRepository;
import br.com.fiap.techchallenge.infrastructure.services.ValidarEnderecoExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ValidarEnderecoExistenteTest {

    @Mock
    private EnderecoRepository enderecoRepository;
    @InjectMocks
    private ValidarEnderecoExistente validarEnderecoExistente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarEnderecoQuandoEncontrado() {
        Endereco endereco = new Endereco();
        when(enderecoRepository.findById(1L)).thenReturn(Optional.of(endereco));
        assertEquals(endereco, validarEnderecoExistente.execute(1L));
    }

    @Test
    void deveLancarExcecaoQuandoEnderecoNaoEncontrado() {
        when(enderecoRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(BadRequestException.class, () -> validarEnderecoExistente.execute(2L));
    }

}
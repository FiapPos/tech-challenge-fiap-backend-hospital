package com.fiap.techchallenge.usuario_service.infrastructure.services;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Endereco;
import com.fiap.techchallenge.usuario_service.core.exceptions.BadRequestException;
import com.fiap.techchallenge.usuario_service.core.gateways.EnderecoRepository;
import com.fiap.techchallenge.usuario_service.infrastructure.services.ValidarEnderecoExistente;
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
package com.fiap.techchallenge.usuario_service.infrastructure.services;

import com.fiap.techchallenge.usuario_service.core.exceptions.BadRequestException;
import com.fiap.techchallenge.usuario_service.core.gateways.UsuarioRepository;
import com.fiap.techchallenge.usuario_service.infrastructure.services.ValidarEmailExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidarEmailExistenteTest {
    @Mock
    private UsuarioRepository usuarioRepository;
    @InjectMocks
    private ValidarEmailExistente validarEmailExistente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void naoDeveLancarExcecaoQuandoEmailNaoDuplicado() {
        when(usuarioRepository.existsByEmail("teste@exemplo.com")).thenReturn(false);
        assertDoesNotThrow(() -> validarEmailExistente.execute("teste@exemplo.com"));
    }

    @Test
    void deveLancarExcecaoQuandoEmailDuplicado() {
        when(usuarioRepository.existsByEmail("teste@exemplo.com")).thenReturn(true);
        assertThrows(BadRequestException.class, () -> validarEmailExistente.execute("teste@exemplo.com"));
    }
}
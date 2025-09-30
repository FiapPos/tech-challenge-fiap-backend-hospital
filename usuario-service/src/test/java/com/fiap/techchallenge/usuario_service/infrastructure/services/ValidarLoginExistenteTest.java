package com.fiap.techchallenge.usuario_service.infrastructure.services;

import com.fiap.techchallenge.usuario_service.core.exceptions.BadRequestException;
import com.fiap.techchallenge.usuario_service.core.gateways.UsuarioRepository;
import com.fiap.techchallenge.usuario_service.infrastructure.services.ValidarLoginExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ValidarLoginExistenteTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ValidarLoginExistente validarLoginExistente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveLancarExcecaoQuandoLoginDuplicado() {
        String login = "user.test";
        when(usuarioRepository.existsByLogin(login)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> validarLoginExistente.execute(login));
        verify(usuarioRepository).existsByLogin(login);
    }

    @Test
    void devePermitirQuandoLoginDisponivel() {
        String login = "novo.user";
        when(usuarioRepository.existsByLogin(login)).thenReturn(false);

        assertDoesNotThrow(() -> validarLoginExistente.execute(login));
        verify(usuarioRepository).existsByLogin(login);
    }
}

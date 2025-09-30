package com.fiap.techchallenge.usuario_service.infrastructure.services;

import com.fiap.techchallenge.usuario_service.core.exceptions.BadRequestException;
import com.fiap.techchallenge.usuario_service.core.gateways.UsuarioRepository;
import com.fiap.techchallenge.usuario_service.infrastructure.services.ValidarCpfExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ValidarCpfExistenteTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ValidarCpfExistente validarCpfExistente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveLancarExcecaoQuandoCpfJaExiste() {
        String cpf = "12345678901";
        when(usuarioRepository.existsByCpf(cpf)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> validarCpfExistente.execute(cpf));
        verify(usuarioRepository).existsByCpf(cpf);
    }

    @Test
    void devePermitirQuandoCpfNaoExiste() {
        String cpf = "10987654321";
        when(usuarioRepository.existsByCpf(cpf)).thenReturn(false);

        assertDoesNotThrow(() -> validarCpfExistente.execute(cpf));
        verify(usuarioRepository).existsByCpf(cpf);
    }
}

package com.fiap.techchallenge.usuario_service.infrastructure.services;

import com.fiap.techchallenge.usuario_service.core.exceptions.BadRequestException;
import com.fiap.techchallenge.usuario_service.core.gateways.EspecialidadeRepository;
import com.fiap.techchallenge.usuario_service.infrastructure.services.ValidarNomeEspecialidadeDuplicado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ValidarNomeEspecialidadeDuplicadoTest {

    @Mock
    private EspecialidadeRepository especialidadeRepository;

    @InjectMocks
    private ValidarNomeEspecialidadeDuplicado validarNomeEspecialidadeDuplicado;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveLancarExcecaoQuandoNomeDuplicado() {
        String nome = "Cardiologia";
        when(especialidadeRepository.existsByNomeIgnoreCase(nome)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> validarNomeEspecialidadeDuplicado.execute(nome));
        verify(especialidadeRepository).existsByNomeIgnoreCase(nome);
    }

    @Test
    void devePermitirQuandoNomeNaoDuplicado() {
        String nome = "Dermatologia";
        when(especialidadeRepository.existsByNomeIgnoreCase(nome)).thenReturn(false);

        assertDoesNotThrow(() -> validarNomeEspecialidadeDuplicado.execute(nome));
        verify(especialidadeRepository).existsByNomeIgnoreCase(nome);
    }
}

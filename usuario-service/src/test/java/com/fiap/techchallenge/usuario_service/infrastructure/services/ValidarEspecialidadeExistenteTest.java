package com.fiap.techchallenge.usuario_service.infrastructure.services;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Especialidade;
import com.fiap.techchallenge.usuario_service.core.exceptions.BadRequestException;
import com.fiap.techchallenge.usuario_service.core.gateways.EspecialidadeRepository;
import com.fiap.techchallenge.usuario_service.infrastructure.services.ValidarEspecialidadeExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ValidarEspecialidadeExistenteTest {

    @Mock
    private EspecialidadeRepository especialidadeRepository;

    @InjectMocks
    private ValidarEspecialidadeExistente validarEspecialidadeExistente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarEspecialidadeQuandoEncontrada() {
        Long id = 10L;
        Especialidade esp = new Especialidade();
        esp.setId(id);
        when(especialidadeRepository.findById(id)).thenReturn(Optional.of(esp));

        Especialidade resultado = validarEspecialidadeExistente.execute(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        verify(especialidadeRepository).findById(id);
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrada() {
        Long id = 99L;
        when(especialidadeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> validarEspecialidadeExistente.execute(id));
        verify(especialidadeRepository).findById(id);
    }
}

package com.fiap.techchallenge.usuario_service.core.domain.usecases.especialidade;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Especialidade;
import com.fiap.techchallenge.usuario_service.core.gateways.EspecialidadeRepository;
import com.fiap.techchallenge.usuario_service.core.domain.usecases.especialidade.InativarEspecialidadeComando;
import com.fiap.techchallenge.usuario_service.infrastructure.services.ValidarEspecialidadeExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class InativarEspecialidadeComandoTest {

    private EspecialidadeRepository repository;
    private ValidarEspecialidadeExistente validarExistente;
    private InativarEspecialidadeComando comando;

    @BeforeEach
    void setUp() {
        repository = mock(EspecialidadeRepository.class);
        validarExistente = mock(ValidarEspecialidadeExistente.class);
        comando = new InativarEspecialidadeComando(repository, validarExistente);
    }

    @Test
    void deveInativarQuandoAtivo() {
        Long id = 4L;
        Especialidade existente = new Especialidade();
        existente.setId(id);
        existente.setAtivo(true);

        when(validarExistente.execute(id)).thenReturn(existente);
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Especialidade salvo = comando.execute(id);

        verify(validarExistente).execute(id);
        verify(repository).save(any(Especialidade.class));
        assertThat(salvo.isAtivo()).isFalse();
        assertThat(salvo.getDataAtualizacao()).isNotNull();
    }

    @Test
    void naoAlteraQuandoJaInativo() {
        Long id = 4L;
        Especialidade existente = new Especialidade();
        existente.setId(id);
        existente.setAtivo(false);

        when(validarExistente.execute(id)).thenReturn(existente);

        Especialidade retorno = comando.execute(id);

        verify(validarExistente).execute(id);
        verifyNoInteractions(repository);
        assertThat(retorno.isAtivo()).isFalse();
    }
}

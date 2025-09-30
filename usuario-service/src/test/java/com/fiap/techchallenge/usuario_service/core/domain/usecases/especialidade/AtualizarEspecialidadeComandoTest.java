package com.fiap.techchallenge.usuario_service.core.domain.usecases.especialidade;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Especialidade;
import com.fiap.techchallenge.usuario_service.core.dtos.especialidade.AtualizarEspecialidadeCommandDto;
import com.fiap.techchallenge.usuario_service.core.exceptions.BusinessException;
import com.fiap.techchallenge.usuario_service.core.gateways.EspecialidadeRepository;
import com.fiap.techchallenge.usuario_service.core.domain.usecases.especialidade.AtualizarEspecialidadeComando;
import com.fiap.techchallenge.usuario_service.infrastructure.services.ValidarEspecialidadeExistente;
import com.fiap.techchallenge.usuario_service.infrastructure.services.ValidarNomeEspecialidadeDuplicado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AtualizarEspecialidadeComandoTest {

    private EspecialidadeRepository repository;
    private ValidarEspecialidadeExistente validarExistente;
    private ValidarNomeEspecialidadeDuplicado validarNomeDuplicado;
    private AtualizarEspecialidadeComando comando;

    @BeforeEach
    void setUp() {
        repository = mock(EspecialidadeRepository.class);
        validarExistente = mock(ValidarEspecialidadeExistente.class);
        validarNomeDuplicado = mock(ValidarNomeEspecialidadeDuplicado.class);
        comando = new AtualizarEspecialidadeComando(repository, validarExistente, validarNomeDuplicado);
    }

    @Test
    void deveLancarErroSeNenhumCampoInformado() {
        AtualizarEspecialidadeCommandDto dto = new AtualizarEspecialidadeCommandDto(null, null);
        assertThrows(BusinessException.class, () -> comando.execute(1L, dto));
        verifyNoInteractions(repository);
        verifyNoInteractions(validarExistente);
    }

    @Test
    void deveAtualizarNomeEDescricao() {
        Long id = 9L;
        Especialidade existente = new Especialidade();
        existente.setId(id);
        existente.setNome("Antigo");
        existente.setDescricao("D");

        when(validarExistente.execute(id)).thenReturn(existente);
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        AtualizarEspecialidadeCommandDto dto = new AtualizarEspecialidadeCommandDto("  Novo  ", "Nova descricao");
        Especialidade salvo = comando.execute(id, dto);

        verify(validarExistente).execute(id);
        verify(validarNomeDuplicado).execute("Novo");
        verify(repository).save(any(Especialidade.class));

        assertThat(salvo.getNome()).isEqualTo("Novo");
        assertThat(salvo.getDescricao()).isEqualTo("Nova descricao");
        assertThat(salvo.getDataAtualizacao()).isNotNull();
    }
}

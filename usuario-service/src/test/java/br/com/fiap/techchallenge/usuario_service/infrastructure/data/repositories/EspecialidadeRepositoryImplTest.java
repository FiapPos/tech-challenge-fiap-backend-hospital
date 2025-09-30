package br.com.fiap.techchallenge.usuario_service.infrastructure.data.repositories;

import br.com.fiap.techchallenge.core.domain.entities.Especialidade;
import br.com.fiap.techchallenge.infrastructure.data.entities.EspecialidadeEntity;
import br.com.fiap.techchallenge.infrastructure.data.repositories.EspecialidadeRepositoryImpl;
import br.com.fiap.techchallenge.infrastructure.data.repositories.JpaEspecialidadeRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EspecialidadeRepositoryImplTest {

    @Test
    void save_deveConverterDomainParaEntityERetornarDomain() {
        JpaEspecialidadeRepository jpa = mock(JpaEspecialidadeRepository.class);
        EspecialidadeRepositoryImpl repo = new EspecialidadeRepositoryImpl(jpa);

        LocalDateTime agora = LocalDateTime.now();
        Especialidade especialidade = Especialidade.builder()
                .id(1L).nome("Cardio").descricao("desc").ativo(true)
                .dataCriacao(agora).dataAtualizacao(agora).build();

        EspecialidadeEntity salvo = EspecialidadeEntity.fromDomain(especialidade);
        when(jpa.save(any(EspecialidadeEntity.class))).thenReturn(salvo);

        Especialidade out = repo.save(especialidade);
        assertEquals("Cardio", out.getNome());
        verify(jpa).save(any(EspecialidadeEntity.class));
    }

    @Test
    void findById_findByNome_existsByNome_findAll_deveDelegarEConverter() {
        JpaEspecialidadeRepository jpa = mock(JpaEspecialidadeRepository.class);
        EspecialidadeRepositoryImpl repo = new EspecialidadeRepositoryImpl(jpa);

        EspecialidadeEntity e = EspecialidadeEntity.builder().id(2L).nome("Dermato").descricao("d").ativo(true)
                .dataCriacao(LocalDateTime.now()).dataAtualizacao(LocalDateTime.now()).build();

        when(jpa.findById(2L)).thenReturn(Optional.of(e));
        when(jpa.findByNomeIgnoreCase("Dermato")).thenReturn(Optional.of(e));
        when(jpa.existsByNomeIgnoreCase("Dermato")).thenReturn(true);
        when(jpa.findAll()).thenReturn(List.of(e));

        assertTrue(repo.findById(2L).isPresent());
        assertTrue(repo.findByNomeIgnoreCase("Dermato").isPresent());
        assertTrue(repo.existsByNomeIgnoreCase("Dermato"));
        assertEquals(1, repo.findAll().size());
    }
}

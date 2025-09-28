package com.fiap.techchallenge.usuario_service.infrastructure.data.repositories;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Endereco;
import com.fiap.techchallenge.usuario_service.infrastructure.data.entities.EnderecoEntity;
import com.fiap.techchallenge.usuario_service.infrastructure.data.repositories.EnderecoJpaRepository;
import com.fiap.techchallenge.usuario_service.infrastructure.data.repositories.EnderecoRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EnderecoRepositoryImplTest {

    @Test
    void save_deveConverterDomainParaEntityERetornarDomain() {
        EnderecoJpaRepository jpa = mock(EnderecoJpaRepository.class);
        EnderecoRepositoryImpl repo = new EnderecoRepositoryImpl(jpa);

        Endereco endereco = new Endereco();
        endereco.setRua("Rua X");
        endereco.setCep("12345-000");
        endereco.setNumero("10");
        endereco.setBairro("Centro");
        endereco.setCidade("SP");
        endereco.setUsuarioId(1L);
        LocalDateTime agora = LocalDateTime.now();
        endereco.setDataCriacao(agora);
        endereco.setDataAtualizacao(agora);

        EnderecoEntity salvo = EnderecoEntity.fromDomain(endereco);
        salvo.setId(99L);

        when(jpa.save(any(EnderecoEntity.class))).thenReturn(salvo);

        Endereco out = repo.save(endereco);

        assertEquals(99L, out.getId());
        assertEquals("Rua X", out.getRua());
        assertEquals("SP", out.getCidade());
        verify(jpa, times(1)).save(any(EnderecoEntity.class));
    }

    @Test
    void findByUsuarioId_deveDelegarParaJpaEConverter() {
        EnderecoJpaRepository jpa = mock(EnderecoJpaRepository.class);
        EnderecoRepositoryImpl repo = new EnderecoRepositoryImpl(jpa);
        Sort sort = Sort.by("id");

        EnderecoEntity e1 = new EnderecoEntity();
        e1.setId(1L);
        e1.setRua("A");
        e1.setUsuarioId(7L);
        EnderecoEntity e2 = new EnderecoEntity();
        e2.setId(2L);
        e2.setRua("B");
        e2.setUsuarioId(7L);
        when(jpa.findByUsuarioId(7L, sort)).thenReturn(List.of(e1, e2));

        List<Endereco> lista = repo.findByUsuarioId(7L, sort);

        assertEquals(2, lista.size());
        assertEquals(1L, lista.get(0).getId());
        verify(jpa).findByUsuarioId(7L, sort);
    }

    @Test
    void existsByUsuarioIdAndCep_deveDelegar() {
        EnderecoJpaRepository jpa = mock(EnderecoJpaRepository.class);
        EnderecoRepositoryImpl repo = new EnderecoRepositoryImpl(jpa);
        when(jpa.existsByUsuarioIdAndCep(5L, "00000-000")).thenReturn(true);

        assertTrue(repo.existsByUsuarioIdAndCep(5L, "00000-000"));
        verify(jpa).existsByUsuarioIdAndCep(5L, "00000-000");
    }

    @Test
    void findById_findAll_deleteById_existsById_deleteAll_deveFuncionar() {
        EnderecoJpaRepository jpa = mock(EnderecoJpaRepository.class);
        EnderecoRepositoryImpl repo = new EnderecoRepositoryImpl(jpa);

        EnderecoEntity e = new EnderecoEntity();
        e.setId(3L);
        e.setRua("C");
        when(jpa.findById(3L)).thenReturn(Optional.of(e));
        when(jpa.findAll()).thenReturn(List.of(e));
        when(jpa.existsById(3L)).thenReturn(true);

        assertTrue(repo.findById(3L).isPresent());
        assertEquals(1, repo.findAll().size());
        assertTrue(repo.existsById(3L));

        repo.deleteById(3L);
        verify(jpa).deleteById(3L);

        repo.deleteAll();
        verify(jpa).deleteAll();
    }
}

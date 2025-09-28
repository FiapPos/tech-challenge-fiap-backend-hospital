package com.fiap.techchallenge.usuario_service.infrastructure.data.repositories;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Especialidade;
import com.fiap.techchallenge.usuario_service.core.domain.entities.Usuario;
import com.fiap.techchallenge.usuario_service.infrastructure.data.entities.EspecialidadeEntity;
import com.fiap.techchallenge.usuario_service.infrastructure.data.entities.UsuarioEntity;
import com.fiap.techchallenge.usuario_service.infrastructure.data.repositories.JpaEspecialidadeRepository;
import com.fiap.techchallenge.usuario_service.infrastructure.data.repositories.UsuarioJpaRepository;
import com.fiap.techchallenge.usuario_service.infrastructure.data.repositories.UsuarioRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UsuarioRepositoryImplTest {

    @Test
    void save_deveSincronizarEspecialidadesEConverter() {
        UsuarioJpaRepository jpa = mock(UsuarioJpaRepository.class);
        JpaEspecialidadeRepository jpaEsp = mock(JpaEspecialidadeRepository.class);
        UsuarioRepositoryImpl repo = new UsuarioRepositoryImpl(jpa, jpaEsp);

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Ana");
        usuario.setEmail("ana@example.com");
        usuario.setLogin("ana");
        usuario.setSenha("s");
        usuario.setAtivo(true);
        usuario.setDataNascimento(LocalDate.of(1991, 1, 1));
        usuario.setDataCriacao(LocalDateTime.now());
        usuario.setDataAtualizacao(LocalDateTime.now());
        usuario.setEspecialidades(
                List.of(new Especialidade(10L, "Cardio", "d", true, LocalDateTime.now(), LocalDateTime.now())));

        EspecialidadeEntity espEntity = EspecialidadeEntity.builder().id(10L).nome("Cardio").descricao("d").ativo(true)
                .dataCriacao(LocalDateTime.now()).dataAtualizacao(LocalDateTime.now()).build();
        when(jpaEsp.findAllById(List.of(10L))).thenReturn(List.of(espEntity));

        UsuarioEntity salvo = new UsuarioEntity();
        salvo.setId(1L);
        salvo.setNome("Ana");
        when(jpa.save(any(UsuarioEntity.class))).thenReturn(salvo);

        Usuario out = repo.save(usuario);

        assertEquals(1L, out.getId());
        verify(jpaEsp).findAllById(List.of(10L));
        verify(jpa).save(any(UsuarioEntity.class));
    }

    @Test
    void consultas_delegamEConvertem() {
        UsuarioJpaRepository jpa = mock(UsuarioJpaRepository.class);
        JpaEspecialidadeRepository jpaEsp = mock(JpaEspecialidadeRepository.class);
        UsuarioRepositoryImpl repo = new UsuarioRepositoryImpl(jpa, jpaEsp);

        UsuarioEntity u = new UsuarioEntity();
        u.setId(2L);
        u.setNome("Bia");
        when(jpa.findById(2L)).thenReturn(Optional.of(u));
        when(jpa.findAll()).thenReturn(List.of(u));
        when(jpa.findByAtivo(true, Sort.by(Sort.Direction.DESC, "id"))).thenReturn(List.of(u));
        when(jpa.findByLogin("bia")).thenReturn(Optional.of(u));
        when(jpa.findByEspecialidadesNome("Cardio")).thenReturn(List.of(u));
        when(jpa.findByIdAndEspecialidadesNome(2L, "Cardio")).thenReturn(Optional.of(u));

        assertTrue(repo.findById(2L).isPresent());
        assertEquals(1, repo.findAll().size());
        assertEquals(1, repo.findByAtivo(true).size());
        assertTrue(repo.findByLogin("bia").isPresent());
        assertEquals(1, repo.findByEspecialidadesNome("Cardio").size());
        assertTrue(repo.findByIdAndEspecialidadesNome(2L, "Cardio").isPresent());

        repo.deleteById(2L);
        verify(jpa).deleteById(2L);

        repo.deleteAll();
        verify(jpa).deleteAll();
    }

    @Test
    void exists_delegamParaJpa() {
        UsuarioJpaRepository jpa = mock(UsuarioJpaRepository.class);
        JpaEspecialidadeRepository jpaEsp = mock(JpaEspecialidadeRepository.class);
        UsuarioRepositoryImpl repo = new UsuarioRepositoryImpl(jpa, jpaEsp);

        when(jpa.existsByEmail("x@x.com")).thenReturn(true);
        when(jpa.existsByLogin("login")).thenReturn(false);
        when(jpa.existsByCpf("123")).thenReturn(true);

        assertTrue(repo.existsByEmail("x@x.com"));
        assertFalse(repo.existsByLogin("login"));
        assertTrue(repo.existsByCpf("123"));
    }
}

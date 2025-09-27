package br.com.fiap.techchallenge.usuario_service.infrastructure.data.repositories;

import br.com.fiap.techchallenge.core.domain.entities.PerfilDoUsuario;
import br.com.fiap.techchallenge.core.enums.Perfil;
import br.com.fiap.techchallenge.infrastructure.data.entities.PerfilEntity;
import br.com.fiap.techchallenge.infrastructure.data.repositories.PerfilUsuarioJpaRepository;
import br.com.fiap.techchallenge.infrastructure.data.repositories.PerfilUsuarioRepositoryImpl;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PerfilUsuarioRepositoryImplTest {

    @Test
    void save_deveConverterDomainParaEntityERetornarDomain() {
        PerfilUsuarioJpaRepository jpa = mock(PerfilUsuarioJpaRepository.class);
        PerfilUsuarioRepositoryImpl repo = new PerfilUsuarioRepositoryImpl(jpa);

        PerfilDoUsuario perfil = new PerfilDoUsuario(1L, Perfil.MEDICO, null);
        PerfilEntity salvo = PerfilEntity.fromDomain(perfil);
        when(jpa.save(any(PerfilEntity.class))).thenReturn(salvo);

        PerfilDoUsuario out = repo.save(perfil);
        assertEquals(Perfil.MEDICO, out.getPerfil());
        verify(jpa).save(any(PerfilEntity.class));
    }

    @Test
    void findById_findAll_deleteById_existsById_deleteAll_deveDelegarEConverter() {
        PerfilUsuarioJpaRepository jpa = mock(PerfilUsuarioJpaRepository.class);
        PerfilUsuarioRepositoryImpl repo = new PerfilUsuarioRepositoryImpl(jpa);

        PerfilEntity e = new PerfilEntity();
        e.setId(2L);
        e.setNomePerfil(Perfil.PACIENTE);

        when(jpa.findById(2L)).thenReturn(Optional.of(e));
        when(jpa.findAll()).thenReturn(List.of(e));
        when(jpa.existsById(2L)).thenReturn(true);

        assertTrue(repo.findById(2L).isPresent());
        assertEquals(1, repo.findAll().size());
        assertTrue(repo.existsById(2L));

        repo.deleteById(2L);
        verify(jpa).deleteById(2L);

        repo.deleteAll();
        verify(jpa).deleteAll();
    }
}

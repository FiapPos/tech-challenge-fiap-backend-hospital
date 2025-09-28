package com.fiap.techchallenge.usuario_service.infrastructure.data.entities;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Especialidade;
import com.fiap.techchallenge.usuario_service.infrastructure.data.entities.EspecialidadeEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EspecialidadeEntityTest {

    @Test
    void fromDomain_deveMapearTodosOsCampos() {
        LocalDateTime agora = LocalDateTime.now();
        Especialidade domain = Especialidade.builder()
                .id(5L)
                .nome("Cardiologia")
                .descricao("Cuida do coração")
                .ativo(true)
                .dataCriacao(agora)
                .dataAtualizacao(agora)
                .build();

        EspecialidadeEntity entity = EspecialidadeEntity.fromDomain(domain);

        assertEquals(5L, entity.getId());
        assertEquals("Cardiologia", entity.getNome());
        assertEquals("Cuida do coração", entity.getDescricao());
        assertTrue(entity.isAtivo());
        assertEquals(agora, entity.getDataCriacao());
        assertEquals(agora, entity.getDataAtualizacao());
    }

    @Test
    void toDomain_deveMapearTodosOsCampos() {
        LocalDateTime agora = LocalDateTime.now();
        EspecialidadeEntity entity = EspecialidadeEntity.builder()
                .id(6L)
                .nome("Dermatologia")
                .descricao("Cuida da pele")
                .ativo(false)
                .dataCriacao(agora)
                .dataAtualizacao(agora)
                .build();

        Especialidade domain = entity.toDomain();

        assertEquals(6L, domain.getId());
        assertEquals("Dermatologia", domain.getNome());
        assertEquals("Cuida da pele", domain.getDescricao());
        assertFalse(domain.isAtivo());
        assertEquals(agora, domain.getDataCriacao());
        assertEquals(agora, domain.getDataAtualizacao());
    }
}

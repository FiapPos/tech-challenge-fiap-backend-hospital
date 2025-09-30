package br.com.fiap.techchallenge.usuario_service.core.domain.entities;

import br.com.fiap.techchallenge.core.domain.entities.Especialidade;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EspecialidadeTest {

    @Test
    void builderDevePopularCamposCorretamente() {
        LocalDateTime agora = LocalDateTime.now();
        Especialidade e = Especialidade.builder()
                .id(5L).nome("Cardio").descricao("desc")
                .ativo(true).dataCriacao(agora).dataAtualizacao(agora)
                .build();

        assertEquals(5L, e.getId());
        assertEquals("Cardio", e.getNome());
        assertEquals("desc", e.getDescricao());
        assertTrue(e.isAtivo());
        assertEquals(agora, e.getDataCriacao());
        assertEquals(agora, e.getDataAtualizacao());
    }
}

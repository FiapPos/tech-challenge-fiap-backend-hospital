package com.fiap.techchallenge.usuario_service.core.domain.entities;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Endereco;
import com.fiap.techchallenge.usuario_service.core.domain.entities.Usuario;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EnderecoTest {

    @Test
    void gettersSettersEConstrutoresDevemFuncionar() {
        Usuario u = new Usuario();
        LocalDateTime agora = LocalDateTime.now();

        Endereco e = new Endereco();
        e.setId(1L);
        e.setRua("Rua A");
        e.setCep("12345-678");
        e.setNumero("100");
        e.setBairro("Centro");
        e.setCidade("SP");
        e.setUsuarioId(9L);
        e.setUsuario(u);
        e.setDataCriacao(agora);
        e.setDataAtualizacao(agora);

        assertEquals(1L, e.getId());
        assertEquals("Rua A", e.getRua());
        assertEquals("12345-678", e.getCep());
        assertEquals("100", e.getNumero());
        assertEquals("Centro", e.getBairro());
        assertEquals("SP", e.getCidade());
        assertEquals(9L, e.getUsuarioId());
        assertSame(u, e.getUsuario());
        assertEquals(agora, e.getDataCriacao());
        assertEquals(agora, e.getDataAtualizacao());

        Endereco outro = new Endereco(2L, "Rua B", "00000-000", "200", "Bairro B", "RJ", 10L, u, agora, agora);
        assertEquals(2L, outro.getId());
        assertEquals("Rua B", outro.getRua());
    }
}

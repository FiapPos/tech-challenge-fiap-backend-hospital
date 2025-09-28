package com.fiap.techchallenge.usuario_service.infrastructure.data.entities;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Endereco;
import com.fiap.techchallenge.usuario_service.core.domain.entities.Usuario;
import com.fiap.techchallenge.usuario_service.infrastructure.data.entities.EnderecoEntity;
import com.fiap.techchallenge.usuario_service.infrastructure.data.entities.UsuarioEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EnderecoEntityTest {

    @Test
    void toDomain_deveMapearTodosOsCampos_incluindoBairroCidadeUsuarioId() {
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setId(10L);
        usuarioEntity.setNome("João Silva");

        EnderecoEntity entity = new EnderecoEntity();
        entity.setId(1L);
        entity.setRua("Rua A");
        entity.setCep("12345-678");
        entity.setNumero("100");
        entity.setBairro("Centro");
        entity.setCidade("São Paulo");
        entity.setUsuarioId(10L);
        entity.setUsuario(usuarioEntity);
        LocalDateTime agora = LocalDateTime.now();
        entity.setDataCriacao(agora);
        entity.setDataAtualizacao(agora);

        Endereco domain = entity.toDomain();

        assertEquals(1L, domain.getId());
        assertEquals("Rua A", domain.getRua());
        assertEquals("12345-678", domain.getCep());
        assertEquals("100", domain.getNumero());
        assertEquals("Centro", domain.getBairro());
        assertEquals("São Paulo", domain.getCidade());
        assertEquals(10L, domain.getUsuarioId());
        assertNotNull(domain.getUsuario());
        assertEquals(10L, domain.getUsuario().getId());
        assertEquals(agora, domain.getDataCriacao());
        assertEquals(agora, domain.getDataAtualizacao());
    }

    @Test
    void toDomain_deveFuncionarQuandoUsuarioForNull() {
        EnderecoEntity entity = new EnderecoEntity();
        entity.setId(2L);
        entity.setRua("Rua B");
        entity.setCep("00000-000");
        entity.setNumero("200");
        entity.setBairro("Bairro B");
        entity.setCidade("Cidade B");
        entity.setUsuarioId(20L);

        Endereco domain = entity.toDomain();

        assertEquals(2L, domain.getId());
        assertEquals("Rua B", domain.getRua());
        assertEquals("00000-000", domain.getCep());
        assertEquals("200", domain.getNumero());
        assertEquals("Bairro B", domain.getBairro());
        assertEquals("Cidade B", domain.getCidade());
        assertEquals(20L, domain.getUsuarioId());
        assertNull(domain.getUsuario());
    }

    @Test
    void fromDomain_deveMapearTodosOsCampos() {
        Usuario usuario = new Usuario();
        usuario.setId(11L);

        Endereco domain = new Endereco();
        domain.setId(3L);
        domain.setRua("Rua C");
        domain.setCep("11111-111");
        domain.setNumero("300");
        domain.setBairro("Bairro C");
        domain.setCidade("Cidade C");
        domain.setUsuarioId(11L);
        LocalDateTime agora = LocalDateTime.now();
        domain.setDataCriacao(agora);
        domain.setDataAtualizacao(agora);
        domain.setUsuario(usuario);

        EnderecoEntity entity = EnderecoEntity.fromDomain(domain);

        assertEquals(3L, entity.getId());
        assertEquals("Rua C", entity.getRua());
        assertEquals("11111-111", entity.getCep());
        assertEquals("300", entity.getNumero());
        assertEquals("Bairro C", entity.getBairro());
        assertEquals("Cidade C", entity.getCidade());
        assertEquals(11L, entity.getUsuarioId());
        assertEquals(agora, entity.getDataCriacao());
        assertEquals(agora, entity.getDataAtualizacao());
    }
}

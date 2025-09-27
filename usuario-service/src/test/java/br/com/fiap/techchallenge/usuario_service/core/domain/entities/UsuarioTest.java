package br.com.fiap.techchallenge.usuario_service.core.domain.entities;

import br.com.fiap.techchallenge.core.domain.entities.PerfilDoUsuario;
import br.com.fiap.techchallenge.core.domain.entities.Usuario;
import br.com.fiap.techchallenge.core.enums.Perfil;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void trocaSenhaDeveAtualizarDataAtualizacao() {
        Usuario u = new Usuario();
        u.setSenha("old");
        LocalDateTime antes = u.getDataAtualizacao();

        u.trocaSenha("new");

        assertEquals("new", u.getSenha());
        assertNotNull(u.getDataAtualizacao());
        if (antes != null) {
            assertTrue(u.getDataAtualizacao().isAfter(antes) || u.getDataAtualizacao().isEqual(antes));
        }
    }

    @Test
    void getPerfisDeveRetornarSetComPerfilQuandoPresente() {
        Usuario u = new Usuario();
        u.setPerfil(new PerfilDoUsuario(1L, Perfil.MEDICO, u));

        var perfis = u.getPerfis();
        assertEquals(1, perfis.size());
        assertTrue(perfis.stream().anyMatch(p -> p.getPerfil() == Perfil.MEDICO));
    }

    @Test
    void getPerfisDeveRetornarSetVazioQuandoPerfilNulo() {
        Usuario u = new Usuario();
        u.setPerfil(null);

        var perfis = u.getPerfis();
        assertNotNull(perfis);
        assertTrue(perfis.isEmpty());
    }

    @Test
    void isAtivoDeveRefletirCampoAtivo() {
        Usuario u = new Usuario();
        u.setAtivo(Boolean.TRUE);
        assertTrue(u.isAtivo());
        u.setAtivo(Boolean.FALSE);
        assertFalse(u.isAtivo());
    }
}

package com.fiap.techchallenge.usuario_service.core.domain.usecases.usuario;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Usuario;
import com.fiap.techchallenge.usuario_service.core.domain.usecases.usuario.AssociarPerfilUsuarioComando;
import com.fiap.techchallenge.usuario_service.core.enums.Perfil;
import com.fiap.techchallenge.usuario_service.core.utils.usuario.perfilUsuario.VerificarPerfilUsuarioExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class AssociarPerfilUsuarioComandoTest {

    private VerificarPerfilUsuarioExistente verificarPerfilUsuarioExistente;
    private AssociarPerfilUsuarioComando comando;

    @BeforeEach
    void setUp() {
        verificarPerfilUsuarioExistente = mock(VerificarPerfilUsuarioExistente.class);
        comando = new AssociarPerfilUsuarioComando(verificarPerfilUsuarioExistente);
    }

    @Test
    void deveAssociarPerfilAoUsuario() {
        Usuario usuario = new Usuario();
        Perfil perfil = Perfil.ADMIN;

        comando.execute(usuario, perfil);

        verify(verificarPerfilUsuarioExistente).execute(usuario, perfil);
        assertThat(usuario.getPerfil()).isNotNull();
        assertThat(usuario.getPerfil().getPerfil()).isEqualTo(perfil);
        assertThat(usuario.getPerfil().getUsuario()).isEqualTo(usuario);
    }
}

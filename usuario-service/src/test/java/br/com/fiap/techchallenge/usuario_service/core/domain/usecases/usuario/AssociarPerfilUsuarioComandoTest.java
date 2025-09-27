package br.com.fiap.techchallenge.usuario_service.core.domain.usecases.usuario;

import br.com.fiap.techchallenge.core.domain.entities.PerfilDoUsuario;
import br.com.fiap.techchallenge.core.domain.entities.Usuario;
import br.com.fiap.techchallenge.core.domain.usecases.usuario.AssociarPerfilUsuarioComando;
import br.com.fiap.techchallenge.core.enums.Perfil;
import br.com.fiap.techchallenge.core.utils.usuario.perfilUsuario.VerificarPerfilUsuarioExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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

package br.com.fiap.techchallenge.usuario_service.core.queries.usuario;

import br.com.fiap.techchallenge.core.domain.entities.PerfilDoUsuario;
import br.com.fiap.techchallenge.core.enums.Perfil;
import br.com.fiap.techchallenge.core.domain.entities.Usuario;
import br.com.fiap.techchallenge.core.exceptions.NotFoundException;
import br.com.fiap.techchallenge.core.gateways.UsuarioRepository;
import br.com.fiap.techchallenge.core.queries.perfil.ListarPorPerfilUsuario;
import br.com.fiap.techchallenge.core.queries.perfil.PerfilUsuarioResultItem;
import br.com.fiap.techchallenge.core.queries.usuario.ListarUsuarioPorLoginQuery;
import br.com.fiap.techchallenge.core.queries.resultadoItem.usuario.ListarUsuariosResultadoItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para {@link ListarUsuarioPorLoginQuery}.
 */
class ListarUsuarioPorLoginQueryTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ListarPorPerfilUsuario listarPorPerfilUsuario;

    @InjectMocks
    private ListarUsuarioPorLoginQuery listarUsuarioPorLoginQuery;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve retornar usuario quando login existir")
    void deveRetornarUsuarioQuandoLoginExistir() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setId(10L);
        usuario.setNome("Fulano Teste");
        usuario.setEmail("fulano@teste.com");
        usuario.setCpf("12345678901");
        usuario.setTelefone("11999999999");
        usuario.setLogin("fulano");
        usuario.setDataNascimento(LocalDate.of(1990, 1, 1));
        usuario.setDataCriacao(LocalDateTime.now().minusDays(1));
        usuario.setDataAtualizacao(LocalDateTime.now());
    PerfilDoUsuario perfilDoUsuario = new PerfilDoUsuario(1L, Perfil.ADMIN, usuario);
    usuario.setPerfil(perfilDoUsuario);

        when(usuarioRepository.findByLogin("fulano")).thenReturn(Optional.of(usuario));
    PerfilUsuarioResultItem perfilItem = PerfilUsuarioResultItem.builder()
        .perfil(usuario.getPerfil().getPerfil())
        .codigo(usuario.getPerfil().getPerfil().getCodigo())
        .build();
    when(listarPorPerfilUsuario.execute(any()))
        .thenReturn(List.of(perfilItem));

        // Act
        ListarUsuariosResultadoItem resultado = listarUsuarioPorLoginQuery.execute("fulano");

        // Assert
        assertNotNull(resultado);
        assertEquals(usuario.getId(), resultado.getId());
        assertEquals(usuario.getNome(), resultado.getNome());
        assertEquals(usuario.getEmail(), resultado.getEmail());
        assertEquals(usuario.getLogin(), resultado.getLogin());
        assertNotNull(resultado.getPerfil());
        assertEquals(1, resultado.getPerfil().size());
        verify(usuarioRepository).findByLogin("fulano");
    verify(listarPorPerfilUsuario).execute(any());
    }

    @Test
    @DisplayName("Deve lançar NotFoundException quando login não existir")
    void deveLancarNotFoundQuandoLoginNaoExistir() {
        when(usuarioRepository.findByLogin("inexistente")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> listarUsuarioPorLoginQuery.execute("inexistente"));

        verify(usuarioRepository).findByLogin("inexistente");
        verifyNoInteractions(listarPorPerfilUsuario);
    }
}

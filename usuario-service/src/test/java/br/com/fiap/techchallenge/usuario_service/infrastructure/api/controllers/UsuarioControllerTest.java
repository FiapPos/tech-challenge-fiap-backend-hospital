package br.com.fiap.techchallenge.usuario_service.infrastructure.api.controllers;

import br.com.fiap.techchallenge.infrastructure.api.controllers.UsuarioController;
import br.com.fiap.techchallenge.core.domain.usecases.usuario.CriarUsuarioComando;
import br.com.fiap.techchallenge.core.domain.usecases.usuario.AtualizarUsuarioComando;
import br.com.fiap.techchallenge.core.domain.usecases.usuario.DesativarUsuarioComando;
import br.com.fiap.techchallenge.core.dtos.usuario.AtualizarUsuarioComandoDto;
import br.com.fiap.techchallenge.core.dtos.usuario.CriarUsuarioComandoDto;
import br.com.fiap.techchallenge.core.domain.entities.Usuario;
import br.com.fiap.techchallenge.core.queries.usuario.ListarUsuariosQuery;
import br.com.fiap.techchallenge.core.queries.params.ListarUsuariosParams;
import br.com.fiap.techchallenge.core.queries.resultadoItem.usuario.ListarUsuariosResultadoItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioControllerTest {
    @Mock
    private CriarUsuarioComando criarUsuarioComando;
    @Mock
    private ListarUsuariosQuery listarUsuariosQuery;
    @Mock
    private AtualizarUsuarioComando atualizarUsuarioComando;
    @Mock
    private DesativarUsuarioComando desativarUsuarioComando;
    @InjectMocks
    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarUsuarioComSucesso() {
        CriarUsuarioComandoDto usuarioDto = new CriarUsuarioComandoDto();
        usuarioDto.setNome("Usuário Teste");
        usuarioDto.setEmail("teste@exemplo.com");
        usuarioDto.setLogin("loginTeste");

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        when(criarUsuarioComando.execute(usuarioDto)).thenReturn(usuario);

        ResponseEntity<Void> response = usuarioController.criarUsuario(usuarioDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(criarUsuarioComando).execute(usuarioDto);
    }

    @Test
    void deveListarUsuariosComSucesso() {
        ListarUsuariosParams params = new ListarUsuariosParams(true);
        ListarUsuariosResultadoItem usuarioItem = new ListarUsuariosResultadoItem();
        usuarioItem.setId(1L);
        usuarioItem.setNome("Usuário Teste");
        List<ListarUsuariosResultadoItem> resultado = List.of(usuarioItem);

        when(listarUsuariosQuery.execute(params)).thenReturn(resultado);

        ResponseEntity<List<ListarUsuariosResultadoItem>> response = usuarioController.listarUsuarios(params);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(resultado, response.getBody());
        verify(listarUsuariosQuery).execute(params);
    }

    @Test
    void deveRetornarNoContentQuandoListaVazia() {
        ListarUsuariosParams params = new ListarUsuariosParams(true);
        when(listarUsuariosQuery.execute(params)).thenReturn(List.of());

        ResponseEntity<List<ListarUsuariosResultadoItem>> response = usuarioController.listarUsuarios(params);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(listarUsuariosQuery).execute(params);
    }

    @Test
    void deveAtualizarUsuarioComSucesso() {
        Long id = 1L;
        Usuario usuario = new Usuario();
        AtualizarUsuarioComandoDto usuarioDto = new AtualizarUsuarioComandoDto();
        usuarioDto.setNome("Novo Nome");

        when(atualizarUsuarioComando.execute(id, usuarioDto)).thenReturn(usuario);

        ResponseEntity<Void> response = usuarioController.atualizarUsuario(id, usuarioDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(atualizarUsuarioComando).execute(id, usuarioDto);
    }

    @Test
    void deveDesativarUsuarioComSucesso() {
        Long id = 1L;
        Usuario usuario = new Usuario();

        when(desativarUsuarioComando.execute(id)).thenReturn(usuario);

        ResponseEntity<Void> response = usuarioController.desativarUsuario(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(desativarUsuarioComando).execute(id);
    }
}

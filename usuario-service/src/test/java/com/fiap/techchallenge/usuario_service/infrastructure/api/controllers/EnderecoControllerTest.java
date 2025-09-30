package com.fiap.techchallenge.usuario_service.infrastructure.api.controllers;

import com.fiap.techchallenge.usuario_service.infrastructure.api.controllers.EnderecoController;
import com.fiap.techchallenge.usuario_service.core.domain.entities.Endereco;
import com.fiap.techchallenge.usuario_service.core.domain.entities.Usuario;
import com.fiap.techchallenge.usuario_service.core.domain.usecases.endereco.CriarEnderecoCommand;
import com.fiap.techchallenge.usuario_service.core.domain.usecases.endereco.DeletarEnderecoComando;
import com.fiap.techchallenge.usuario_service.core.dtos.endereco.CriarEnderecoComandoDto;
import com.fiap.techchallenge.usuario_service.core.dtos.endereco.DeletarEnderecoComandoDto;
import com.fiap.techchallenge.usuario_service.core.gateways.EnderecoRepository;
import com.fiap.techchallenge.usuario_service.core.queries.endereco.ListarEnderecoPorIdUsuario;
import com.fiap.techchallenge.usuario_service.core.queries.resultadoItem.endereco.ListarEnderecoPorIdUsuarioResultadoItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class EnderecoControllerTest {
    @Mock
    private CriarEnderecoCommand criarEnderecoCommand;
    @Mock
    private DeletarEnderecoComando deletarEnderecoComando;
    @Mock
    private ListarEnderecoPorIdUsuario listarEnderecoPorIdUsuario;
    @Mock
    private EnderecoRepository enderecoRepository;
    @InjectMocks
    private EnderecoController enderecoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarEnderecoComSucesso() {
        CriarEnderecoComandoDto dto = new CriarEnderecoComandoDto();
        dto.setRua("Rua Teste");
        dto.setCep("12345-678");
        dto.setNumero("100");
        dto.setUsuarioId(1L);

        Endereco endereco = new Endereco();
        endereco.setId(1L);
        when(criarEnderecoCommand.execute(dto.getUsuarioId(), dto)).thenReturn(endereco);

        ResponseEntity<Void> response = enderecoController.criar(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(criarEnderecoCommand).execute(dto.getUsuarioId(), dto);
    }

    @Test
    void deveDeletarEnderecoComSucesso() {
        DeletarEnderecoComandoDto dto = new DeletarEnderecoComandoDto();
        dto.setEnderecoId(1L);
        dto.setUsuarioId(1L);

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        Endereco endereco = new Endereco();
        endereco.setId(dto.getEnderecoId());
        endereco.setUsuario(usuario);
        when(enderecoRepository.findById(dto.getEnderecoId())).thenReturn(Optional.of(endereco));

        doNothing().when(deletarEnderecoComando).execute(dto.getUsuarioId(), dto);

        ResponseEntity<Void> response = enderecoController.deletar(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(deletarEnderecoComando).execute(dto.getUsuarioId(), dto);
        verify(enderecoRepository).findById(dto.getEnderecoId());
    }

    @Test
    void deveListarEnderecosPorUsuarioComSucesso() {
        Long usuarioId = 1L;
        ListarEnderecoPorIdUsuarioResultadoItem item = ListarEnderecoPorIdUsuarioResultadoItem.builder()
                .id(1L)
                .rua("Rua Teste")
                .build();
        List<ListarEnderecoPorIdUsuarioResultadoItem> resultado = List.of(item);

        when(listarEnderecoPorIdUsuario.execute(usuarioId)).thenReturn(resultado);

        ResponseEntity<List<ListarEnderecoPorIdUsuarioResultadoItem>> response = enderecoController
                .listarPorUsuario(usuarioId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(resultado, response.getBody());
        verify(listarEnderecoPorIdUsuario).execute(usuarioId);
    }

    @Test
    void deveRetornarNoContentQuandoListaVazia() {
        Long usuarioId = 1L;

        when(listarEnderecoPorIdUsuario.execute(usuarioId)).thenReturn(List.of());

        ResponseEntity<List<ListarEnderecoPorIdUsuarioResultadoItem>> response = enderecoController
                .listarPorUsuario(usuarioId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(listarEnderecoPorIdUsuario).execute(usuarioId);
    }

}

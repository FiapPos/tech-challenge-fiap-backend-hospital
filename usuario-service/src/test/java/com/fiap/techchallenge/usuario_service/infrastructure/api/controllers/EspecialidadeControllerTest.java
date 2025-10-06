package com.fiap.techchallenge.usuario_service.infrastructure.api.controllers;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Especialidade;
import com.fiap.techchallenge.usuario_service.core.domain.usecases.especialidade.AtualizarEspecialidadeComando;
import com.fiap.techchallenge.usuario_service.core.domain.usecases.especialidade.CriarEspecialidadeComando;
import com.fiap.techchallenge.usuario_service.core.domain.usecases.especialidade.InativarEspecialidadeComando;
import com.fiap.techchallenge.usuario_service.core.dtos.especialidade.AtualizarEspecialidadeCommandDto;
import com.fiap.techchallenge.usuario_service.core.dtos.especialidade.CriarEspecialidadeCommandDto;
import com.fiap.techchallenge.usuario_service.core.dtos.especialidade.EspecialidadeResponse;
import com.fiap.techchallenge.usuario_service.core.queries.especialidade.ListarEspecialidadesQuery;
import com.fiap.techchallenge.usuario_service.core.queries.resultadoItem.especialidade.ListarEspecialidadePorResultadoItem;
import com.fiap.techchallenge.usuario_service.infrastructure.api.controllers.EspecialidadeController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EspecialidadeControllerTest {

    @Mock
    private CriarEspecialidadeComando criarEspecialidadeComando;
    @Mock
    private ListarEspecialidadesQuery listarEspecialidadesQuery;
    @Mock
    private AtualizarEspecialidadeComando atualizarEspecialidadeComando;
    @Mock
    private InativarEspecialidadeComando inativarEspecialidadeComando;

    @InjectMocks
    private EspecialidadeController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarEspecialidadeComSucesso() {
        String nome = "Cardiologia";
        String descricao = "Coração";
        CriarEspecialidadeCommandDto dto = new CriarEspecialidadeCommandDto(nome, descricao);

        Especialidade criada = new Especialidade();
        criada.setId(10L);
        criada.setNome(nome);
        criada.setDescricao(descricao);
        criada.setAtivo(true);
        criada.setDataCriacao(LocalDateTime.now());

        when(criarEspecialidadeComando.execute(dto)).thenReturn(criada);

        ResponseEntity<EspecialidadeResponse> resp = controller.criar(dto);

        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertEquals(URI.create("/api/especialidades/" + criada.getId()), resp.getHeaders().getLocation());
        assertNotNull(resp.getBody());
        assertEquals(criada.getId(), resp.getBody().id());
        assertEquals(nome, resp.getBody().nome());
        verify(criarEspecialidadeComando).execute(dto);
    }

    @Test
    void deveListarEspecialidades() {
        ListarEspecialidadePorResultadoItem item = ListarEspecialidadePorResultadoItem.builder()
                .id(1L)
                .nome("Dermatologia")
                .descricao("Pele")
                .ativo(true)
                .dataCriacao(LocalDateTime.now())
                .build();
        when(listarEspecialidadesQuery.execute()).thenReturn(List.of(item));

        ResponseEntity<List<ListarEspecialidadePorResultadoItem>> resp = controller.listar();

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertEquals(1, resp.getBody().size());
        assertEquals(item, resp.getBody().get(0));
        verify(listarEspecialidadesQuery).execute();
    }

    @Test
    void deveRetornarOkComListaVaziaQuandoNaoHaEspecialidades() {
        when(listarEspecialidadesQuery.execute()).thenReturn(List.of());

        ResponseEntity<List<ListarEspecialidadePorResultadoItem>> resp = controller.listar();

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertTrue(resp.getBody().isEmpty());
        verify(listarEspecialidadesQuery).execute();
    }

    @Test
    void deveAtualizarEspecialidade() {
        Long id = 5L;
        String nome = "Ortopedia";
        String descricao = "Ossos";
        AtualizarEspecialidadeCommandDto dto = new AtualizarEspecialidadeCommandDto(nome, descricao);

        Especialidade atualizada = new Especialidade();
        atualizada.setId(id);
        atualizada.setNome(nome);
        atualizada.setDescricao(descricao);
        atualizada.setAtivo(true);
        atualizada.setDataAtualizacao(LocalDateTime.now());

        when(atualizarEspecialidadeComando.execute(id, dto)).thenReturn(atualizada);

        ResponseEntity<EspecialidadeResponse> resp = controller.atualizar(id, dto);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertEquals(id, resp.getBody().id());
        assertEquals(nome, resp.getBody().nome());
        verify(atualizarEspecialidadeComando).execute(id, dto);
    }

    @Test
    void devePropagarErroAoAtualizar() {
        Long id = 7L;
        AtualizarEspecialidadeCommandDto dto = new AtualizarEspecialidadeCommandDto("X", "Y");
        when(atualizarEspecialidadeComando.execute(id, dto))
                .thenThrow(new RuntimeException("erro.atualizar"));

        assertThrows(RuntimeException.class, () -> controller.atualizar(id, dto));
        verify(atualizarEspecialidadeComando).execute(id, dto);
    }

    @Test
    void deveInativarEspecialidade() {
        Long id = 8L;
        Especialidade inativada = new Especialidade();
        inativada.setId(id);
        inativada.setAtivo(false);

        when(inativarEspecialidadeComando.execute(id)).thenReturn(inativada);

        ResponseEntity<EspecialidadeResponse> resp = controller.inativar(id);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertEquals(id, resp.getBody().id());
        assertFalse(resp.getBody().ativo());
        verify(inativarEspecialidadeComando).execute(id);
    }

    @Test
    void devePropagarErroAoInativar() {
        Long id = 99L;
        when(inativarEspecialidadeComando.execute(id))
                .thenThrow(new RuntimeException("erro.inativar"));

        assertThrows(RuntimeException.class, () -> controller.inativar(id));
        verify(inativarEspecialidadeComando).execute(id);
    }

    @Test
    void devePropagarErroAoCriar() {
        CriarEspecialidadeCommandDto dto = new CriarEspecialidadeCommandDto("A", "B");
        when(criarEspecialidadeComando.execute(dto))
                .thenThrow(new RuntimeException("erro.criar"));

        assertThrows(RuntimeException.class, () -> controller.criar(dto));
        verify(criarEspecialidadeComando).execute(dto);
    }
}

package br.com.fiap.techchallenge.usuario_service.infrastructure.api.controllers;

import br.com.fiap.techchallenge.core.domain.usecases.especialidadesDoMedico.AssociarEspecialidadeAoMedico;
import br.com.fiap.techchallenge.core.domain.usecases.especialidadesDoMedico.DesassociarEspecialidadeMedico;
import br.com.fiap.techchallenge.core.queries.especialidade.ListarEspecialidadePorIdUsuario;
import br.com.fiap.techchallenge.core.queries.resultadoItem.especialidade.ListarEspecialidadePorResultadoItem;
import br.com.fiap.techchallenge.infrastructure.api.controllers.EspecialidadesMedicoController;
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

public class EspecialidadesMedicoControllerTest {

    @Mock
    private AssociarEspecialidadeAoMedico associarEspecialidadeAoMedico;
    @Mock
    private DesassociarEspecialidadeMedico desassociarEspecialidadeMedico;
    @Mock
    private ListarEspecialidadePorIdUsuario listarEspecialidadePorIdUsuario;

    @InjectMocks
    private EspecialidadesMedicoController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveAssociarEspecialidade() {
        Long medicoId = 3L;
        Long especialidadeId = 7L;

        // execute é void; apenas verificamos a chamada
        doNothing().when(associarEspecialidadeAoMedico).execute(eq(medicoId), any());

        ResponseEntity<Void> resp = controller.associarEspecialidade(medicoId, especialidadeId);

        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertEquals(URI.create(String.format("/medicos/%d/especialidades/%d", medicoId, especialidadeId)),
                resp.getHeaders().getLocation());
        verify(associarEspecialidadeAoMedico).execute(eq(medicoId), any());
    }

    @Test
    void deveListarEspecialidadesDoMedico() {
        Long medicoId = 2L;
        ListarEspecialidadePorResultadoItem item = ListarEspecialidadePorResultadoItem.builder()
                .id(1L)
                .nome("Cardiologia")
                .descricao("Coração")
                .ativo(true)
                .dataCriacao(LocalDateTime.now())
                .build();
        when(listarEspecialidadePorIdUsuario.execute(medicoId)).thenReturn(List.of(item));

        ResponseEntity<List<ListarEspecialidadePorResultadoItem>> resp = controller.listarEspecialidades(medicoId);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertEquals(1, resp.getBody().size());
        assertEquals(item, resp.getBody().get(0));
        verify(listarEspecialidadePorIdUsuario).execute(medicoId);
    }

    @Test
    void deveDesassociarEspecialidade() {
        Long medicoId = 5L;
        Long especialidadeId = 11L;

        doNothing().when(desassociarEspecialidadeMedico).execute(medicoId, especialidadeId);

        ResponseEntity<Void> resp = controller.desassociarEspecialidade(medicoId, especialidadeId);

        assertEquals(HttpStatus.NO_CONTENT, resp.getStatusCode());
        verify(desassociarEspecialidadeMedico).execute(medicoId, especialidadeId);
    }
}

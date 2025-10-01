package com.fiap.techchallenge.appointment_service.core.controller;

import com.fiap.techchallenge.appointment_service.core.client.OrchestratorClient;
import com.fiap.techchallenge.appointment_service.core.client.OrchestratorException;
import com.fiap.techchallenge.appointment_service.core.dto.CriarUsuarioComandoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsuarioProxyControllerTest {

    @Mock
    private OrchestratorClient orchestratorClient;

    private UsuarioController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new UsuarioController(orchestratorClient);
    }

    @Test
    void criarUsuario_success_returnsBodyAndStatus() {
        CriarUsuarioComandoDto dto = new CriarUsuarioComandoDto();
        dto.setLogin("maria");

        ResponseEntity<Object> resp = ResponseEntity.status(HttpStatus.CREATED).body("ok");
        when(orchestratorClient.createUsuarioWithHeaders(any(), any())).thenReturn(resp);

        jakarta.servlet.http.HttpServletRequest req = mock(jakarta.servlet.http.HttpServletRequest.class);
        ResponseEntity<Object> result = controller.criarUsuario(req, dto);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("ok", result.getBody());
    }

    @Test
    void criarUsuario_orchestratorError_propagatesStatusAndBody() {
        CriarUsuarioComandoDto dto = new CriarUsuarioComandoDto();
        dto.setLogin("maria");

        OrchestratorException oe = new OrchestratorException(HttpStatus.BAD_REQUEST, "{\"error\":\"bad\"}");
        when(orchestratorClient.createUsuarioWithHeaders(any(), any())).thenThrow(oe);

        jakarta.servlet.http.HttpServletRequest req = mock(jakarta.servlet.http.HttpServletRequest.class);
        ResponseEntity<Object> result = controller.criarUsuario(req, dto);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertTrue(result.getBody().toString().contains("bad"));
    }

}

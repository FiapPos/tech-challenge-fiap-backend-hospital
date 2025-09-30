package com.fiap.techchallenge.usuario_service.core.domain.usecases.usuario;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Usuario;
import com.fiap.techchallenge.usuario_service.core.domain.usecases.usuario.DesativarUsuarioComando;
import com.fiap.techchallenge.usuario_service.core.gateways.UsuarioRepository;
import com.fiap.techchallenge.usuario_service.core.shared.CompartilhadoService;
import com.fiap.techchallenge.usuario_service.infrastructure.services.ValidarUsuarioExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DesativarUsuarioComandoTest {

    private UsuarioRepository usuarioRepository;
    private ValidarUsuarioExistente validarUsuarioExistente;
    private CompartilhadoService sharedService;

    private DesativarUsuarioComando comando;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        validarUsuarioExistente = mock(ValidarUsuarioExistente.class);
        sharedService = mock(CompartilhadoService.class);
        comando = new DesativarUsuarioComando(usuarioRepository, validarUsuarioExistente, sharedService);
    }

    @Test
    void deveDesativarERegistrarData() {
        Long id = 2L;
        Usuario usuario = new Usuario();
        usuario.setId(id);
        when(validarUsuarioExistente.execute(id)).thenReturn(usuario);
        when(sharedService.getCurrentDateTime()).thenReturn(LocalDateTime.now());
        when(usuarioRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Usuario salvo = comando.execute(id);

        verify(validarUsuarioExistente).execute(id);
        verify(usuarioRepository).save(usuario);
        assertThat(salvo.isAtivo()).isFalse();
        assertThat(salvo.getDataDesativacao()).isNotNull();
    }
}

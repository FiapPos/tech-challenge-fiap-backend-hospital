package com.fiap.techchallenge.usuario_service.core.domain.usecases.especialidadesDoMedico;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Especialidade;
import com.fiap.techchallenge.usuario_service.core.domain.entities.Usuario;
import com.fiap.techchallenge.usuario_service.core.domain.usecases.especialidadesDoMedico.DesassociarEspecialidadeMedico;
import com.fiap.techchallenge.usuario_service.core.exceptions.BusinessException;
import com.fiap.techchallenge.usuario_service.core.gateways.UsuarioRepository;
import com.fiap.techchallenge.usuario_service.core.utils.ValidarPerfilMedico;
import com.fiap.techchallenge.usuario_service.infrastructure.services.ValidarUsuarioExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class DesassociarEspecialidadeMedicoTest {

    private UsuarioRepository usuarioRepository;
    private ValidarUsuarioExistente validarUsuarioExistente;
    private ValidarPerfilMedico validarPerfilMedico;

    private DesassociarEspecialidadeMedico comando;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        validarUsuarioExistente = mock(ValidarUsuarioExistente.class);
        validarPerfilMedico = mock(ValidarPerfilMedico.class);
        comando = new DesassociarEspecialidadeMedico(usuarioRepository, null, validarUsuarioExistente,
                validarPerfilMedico);
    }

    @Test
    void deveRemoverQuandoAssociada() {
        Long usuarioId = 1L;
        Long especialidadeId = 2L;

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        Especialidade esp = new Especialidade();
        esp.setId(especialidadeId);
        usuario.setEspecialidades(new ArrayList<>(List.of(esp)));

        when(validarUsuarioExistente.execute(usuarioId)).thenReturn(usuario);

        comando.execute(usuarioId, especialidadeId);

        verify(validarUsuarioExistente).execute(usuarioId);
        verify(validarPerfilMedico).execute(usuario);
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void deveLancarQuandoNaoAssociada() {
        Long usuarioId = 1L;
        Long especialidadeId = 2L;

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setEspecialidades(new ArrayList<>());

        when(validarUsuarioExistente.execute(usuarioId)).thenReturn(usuario);

        assertThrows(BusinessException.class, () -> comando.execute(usuarioId, especialidadeId));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void deveValidarIdObrigatorio() {
        Long usuarioId = 1L;
        assertThrows(BusinessException.class, () -> comando.execute(usuarioId, null));
        verifyNoInteractions(usuarioRepository);
    }
}

package com.fiap.techchallenge.usuario_service.core.domain.usecases.especialidadesDoMedico;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Especialidade;
import com.fiap.techchallenge.usuario_service.core.domain.entities.Usuario;
import com.fiap.techchallenge.usuario_service.core.dtos.especialidadesDoMedico.AssociarEspecialidadeMedicoDto;
import com.fiap.techchallenge.usuario_service.core.exceptions.BusinessException;
import com.fiap.techchallenge.usuario_service.core.gateways.EspecialidadeRepository;
import com.fiap.techchallenge.usuario_service.core.gateways.UsuarioRepository;
import com.fiap.techchallenge.usuario_service.core.utils.ValidarPerfilMedico;
import com.fiap.techchallenge.usuario_service.core.domain.usecases.especialidadesDoMedico.AssociarEspecialidadeAoMedico;
import com.fiap.techchallenge.usuario_service.infrastructure.services.ValidarUsuarioExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AssociarEspecialidadeAoMedicoTest {

    private UsuarioRepository usuarioRepository;
    private EspecialidadeRepository especialidadeRepository;
    private ValidarUsuarioExistente validarUsuarioExistente;
    private ValidarPerfilMedico validarPerfilMedico;

    private AssociarEspecialidadeAoMedico comando;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        especialidadeRepository = mock(EspecialidadeRepository.class);
        validarUsuarioExistente = mock(ValidarUsuarioExistente.class);
        validarPerfilMedico = mock(ValidarPerfilMedico.class);
        comando = new AssociarEspecialidadeAoMedico(usuarioRepository, especialidadeRepository, validarUsuarioExistente,
                validarPerfilMedico);
    }

    @Test
    void deveAssociarQuandoValidoEAbaixoDoLimite() {
        Long usuarioId = 1L;
        Long especialidadeId = 2L;
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setEspecialidades(new ArrayList<>());

        AssociarEspecialidadeMedicoDto dto = new AssociarEspecialidadeMedicoDto();
        dto.setEspecialidadeId(especialidadeId);

        when(validarUsuarioExistente.execute(usuarioId)).thenReturn(usuario);
        when(especialidadeRepository.findById(especialidadeId)).thenReturn(Optional.of(new Especialidade()));

        comando.execute(usuarioId, dto);

        verify(validarUsuarioExistente).execute(usuarioId);
        verify(validarPerfilMedico).execute(usuario);
        verify(especialidadeRepository).findById(especialidadeId);
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void deveLancarSeEspecialidadeJaAssociada() {
        Long usuarioId = 1L;
        Long especialidadeId = 2L;
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        Especialidade esp = new Especialidade();
        esp.setId(especialidadeId);
        usuario.setEspecialidades(new ArrayList<>(List.of(esp)));

        AssociarEspecialidadeMedicoDto dto = new AssociarEspecialidadeMedicoDto();
        dto.setEspecialidadeId(especialidadeId);

        when(validarUsuarioExistente.execute(usuarioId)).thenReturn(usuario);
        when(especialidadeRepository.findById(especialidadeId)).thenReturn(Optional.of(new Especialidade()));

        assertThrows(BusinessException.class, () -> comando.execute(usuarioId, dto));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void deveLancarSeExcederLimiteDeTres() {
        Long usuarioId = 1L;
        Long especialidadeId = 99L;
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        Especialidade e1 = new Especialidade();
        e1.setId(1L);
        Especialidade e2 = new Especialidade();
        e2.setId(2L);
        Especialidade e3 = new Especialidade();
        e3.setId(3L);
        usuario.setEspecialidades(new ArrayList<>(List.of(e1, e2, e3)));

        AssociarEspecialidadeMedicoDto dto = new AssociarEspecialidadeMedicoDto();
        dto.setEspecialidadeId(especialidadeId);

        when(validarUsuarioExistente.execute(usuarioId)).thenReturn(usuario);
        when(especialidadeRepository.findById(especialidadeId)).thenReturn(Optional.of(new Especialidade()));

        assertThrows(BusinessException.class, () -> comando.execute(usuarioId, dto));
        verify(usuarioRepository, never()).save(any());
    }
}

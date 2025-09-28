package com.fiap.techchallenge.usuario_service.core.domain.usecases.usuario;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Usuario;
import com.fiap.techchallenge.usuario_service.core.dtos.usuario.AtualizarUsuarioComandoDto;
import com.fiap.techchallenge.usuario_service.core.domain.usecases.usuario.AtualizarUsuarioComando;
import com.fiap.techchallenge.usuario_service.core.exceptions.BadRequestException;
import com.fiap.techchallenge.usuario_service.core.gateways.UsuarioRepository;
import com.fiap.techchallenge.usuario_service.core.shared.CompartilhadoService;
import com.fiap.techchallenge.usuario_service.infrastructure.services.ValidarEmailExistente;
import com.fiap.techchallenge.usuario_service.infrastructure.services.ValidarLoginExistente;
import com.fiap.techchallenge.usuario_service.infrastructure.services.ValidarUsuarioExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AtualizarUsuarioComandoTest {

    private UsuarioRepository usuarioRepository;
    private ValidarEmailExistente validarEmailExistente;
    private ValidarLoginExistente validarLoginExistente;
    private ValidarUsuarioExistente validarUsuarioExistente;
    private CompartilhadoService sharedService;
    private PasswordEncoder passwordEncoder;

    private AtualizarUsuarioComando comando;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        validarEmailExistente = mock(ValidarEmailExistente.class);
        validarLoginExistente = mock(ValidarLoginExistente.class);
        validarUsuarioExistente = mock(ValidarUsuarioExistente.class);
        sharedService = mock(CompartilhadoService.class);
        passwordEncoder = mock(PasswordEncoder.class);
        comando = new AtualizarUsuarioComando(usuarioRepository, validarEmailExistente, validarLoginExistente,
                validarUsuarioExistente, sharedService, passwordEncoder);
    }

    @Test
    void deveLancarErroQuandoNenhumCampoInformado() {
        AtualizarUsuarioComandoDto dto = new AtualizarUsuarioComandoDto();
        assertThrows(BadRequestException.class, () -> comando.execute(1L, dto));
        verifyNoInteractions(usuarioRepository);
    }

    @Test
    void deveAtualizarCamposEValidarUnicos() {
        Long id = 5L;
        AtualizarUsuarioComandoDto dto = new AtualizarUsuarioComandoDto();
        dto.setNome("Novo Nome");
        dto.setEmail("novo@ex.com");
        dto.setSenha("nova");
        dto.setLogin("novoLogin");
        dto.setTelefone("123");
        dto.setCpf("321");
        dto.setDataNascimento(LocalDate.of(1990, 1, 1));

        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setEmail("antigo@ex.com");
        usuario.setLogin("antigo");

        when(validarUsuarioExistente.execute(id)).thenReturn(usuario);
        when(sharedService.getCurrentDateTime()).thenReturn(LocalDateTime.now());
        when(passwordEncoder.encode("nova")).thenReturn("hash");
        when(usuarioRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Usuario salvo = comando.execute(id, dto);

        verify(validarEmailExistente).execute("novo@ex.com");
        verify(validarLoginExistente).execute("novoLogin");

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        Usuario atualizado = captor.getValue();

        assertThat(salvo).isSameAs(atualizado);
        assertThat(atualizado.getNome()).isEqualTo("Novo Nome");
        assertThat(atualizado.getEmail()).isEqualTo("novo@ex.com");
        assertThat(atualizado.getLogin()).isEqualTo("novoLogin");
        assertThat(atualizado.getSenha()).isEqualTo("hash");
        assertThat(atualizado.getTelefone()).isEqualTo("123");
        assertThat(atualizado.getCpf()).isEqualTo("321");
        assertThat(atualizado.getDataNascimento()).isEqualTo(LocalDate.of(1990, 1, 1));
        assertThat(atualizado.getDataAtualizacao()).isNotNull();
    }

    @Test
    void naoValidaUnicosQuandoIguais() {
        Long id = 5L;
        AtualizarUsuarioComandoDto dto = new AtualizarUsuarioComandoDto();
        dto.setEmail("mesmo@ex.com");
        dto.setLogin("mesmo");

        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setEmail("mesmo@ex.com");
        usuario.setLogin("mesmo");

        when(validarUsuarioExistente.execute(id)).thenReturn(usuario);
        dto.setNome("x"); // para passar validacao de pelo menos um campo
        when(usuarioRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        comando.execute(id, dto);

        verify(validarEmailExistente, never()).execute(any());
        verify(validarLoginExistente, never()).execute(any());
    }
}

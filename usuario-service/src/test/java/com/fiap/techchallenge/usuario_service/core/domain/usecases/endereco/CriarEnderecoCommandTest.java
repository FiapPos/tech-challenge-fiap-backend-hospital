package com.fiap.techchallenge.usuario_service.core.domain.usecases.endereco;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Endereco;
import com.fiap.techchallenge.usuario_service.core.domain.entities.Usuario;
import com.fiap.techchallenge.usuario_service.core.dtos.endereco.CriarEnderecoComandoDto;
import com.fiap.techchallenge.usuario_service.core.gateways.EnderecoRepository;
import com.fiap.techchallenge.usuario_service.core.shared.CompartilhadoService;
import com.fiap.techchallenge.usuario_service.core.utils.ValidarCepDoUsuario;
import com.fiap.techchallenge.usuario_service.core.domain.usecases.endereco.CriarEnderecoCommand;
import com.fiap.techchallenge.usuario_service.infrastructure.services.ValidarUsuarioExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class CriarEnderecoCommandTest {

    private EnderecoRepository enderecoRepository;
    private CompartilhadoService sharedService;
    private ValidarUsuarioExistente validarUsuarioExistente;
    private ValidarCepDoUsuario validarCepDoUsuario;

    private CriarEnderecoCommand command;

    @BeforeEach
    void setUp() {
        enderecoRepository = mock(EnderecoRepository.class);
        sharedService = mock(CompartilhadoService.class);
        validarUsuarioExistente = mock(ValidarUsuarioExistente.class);
        validarCepDoUsuario = mock(ValidarCepDoUsuario.class);
        command = new CriarEnderecoCommand(enderecoRepository, sharedService, validarUsuarioExistente,
                validarCepDoUsuario);
    }

    @Test
    void deveCriarEnderecoComCamposCorretosEValidacoes() {
        Long usuarioId = 10L;
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        CriarEnderecoComandoDto dto = new CriarEnderecoComandoDto();
        dto.setRua("Rua A");
        dto.setCep("12345-000");
        dto.setNumero("100");
        dto.setBairro("Centro");
        dto.setCidade("SP");
        dto.setUsuarioId(usuarioId);

        when(validarUsuarioExistente.execute(usuarioId)).thenReturn(usuario);
        LocalDateTime agora = LocalDateTime.now();
        when(sharedService.getCurrentDateTime()).thenReturn(agora);

        ArgumentCaptor<Endereco> captor = ArgumentCaptor.forClass(Endereco.class);
        when(enderecoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Endereco salvo = command.execute(usuarioId, dto);

        verify(validarUsuarioExistente).execute(usuarioId);
        verify(validarCepDoUsuario).validarCepDuplicado(usuarioId, dto.getCep());
        verify(sharedService).getCurrentDateTime();
        verify(enderecoRepository).save(captor.capture());

        Endereco enviado = captor.getValue();
        assertThat(salvo).isSameAs(enviado);
        assertThat(enviado.getRua()).isEqualTo("Rua A");
        assertThat(enviado.getCep()).isEqualTo("12345-000");
        assertThat(enviado.getNumero()).isEqualTo("100");
        assertThat(enviado.getBairro()).isEqualTo("Centro");
        assertThat(enviado.getCidade()).isEqualTo("SP");
        assertThat(enviado.getUsuario()).isEqualTo(usuario);
        assertThat(enviado.getUsuarioId()).isEqualTo(usuarioId);
        assertThat(enviado.getDataCriacao()).isEqualTo(agora);
    }
}

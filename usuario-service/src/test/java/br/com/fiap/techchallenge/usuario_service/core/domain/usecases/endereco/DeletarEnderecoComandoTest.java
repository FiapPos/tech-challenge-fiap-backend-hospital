package br.com.fiap.techchallenge.usuario_service.core.domain.usecases.endereco;

import br.com.fiap.techchallenge.core.domain.entities.Endereco;
import br.com.fiap.techchallenge.core.dtos.endereco.DeletarEnderecoComandoDto;
import br.com.fiap.techchallenge.core.domain.usecases.endereco.DeletarEnderecoComando;
import br.com.fiap.techchallenge.core.gateways.EnderecoRepository;
import br.com.fiap.techchallenge.infrastructure.services.ValidarEnderecoExistente;
import br.com.fiap.techchallenge.infrastructure.services.ValidarProprietarioEndereco;
import br.com.fiap.techchallenge.infrastructure.services.ValidarUsuarioExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class DeletarEnderecoComandoTest {

    private EnderecoRepository enderecoRepository;
    private ValidarEnderecoExistente validarEnderecoExistente;
    private ValidarUsuarioExistente validarUsuarioExistente;
    private ValidarProprietarioEndereco validarProprietarioEndereco;

    private DeletarEnderecoComando comando;

    @BeforeEach
    void setUp() {
        enderecoRepository = mock(EnderecoRepository.class);
        validarEnderecoExistente = mock(ValidarEnderecoExistente.class);
        validarUsuarioExistente = mock(ValidarUsuarioExistente.class);
        validarProprietarioEndereco = mock(ValidarProprietarioEndereco.class);
        comando = new DeletarEnderecoComando(enderecoRepository, validarEnderecoExistente, validarUsuarioExistente,
                validarProprietarioEndereco);
    }

    @Test
    void deveValidarEDeletarEndereco() {
        Long usuarioId = 3L;
        Long enderecoId = 7L;
        DeletarEnderecoComandoDto dto = new DeletarEnderecoComandoDto();
        dto.setUsuarioId(usuarioId);
        dto.setEnderecoId(enderecoId);

        Endereco endereco = new Endereco();
        endereco.setId(enderecoId);

        when(validarEnderecoExistente.execute(enderecoId)).thenReturn(endereco);

        comando.execute(usuarioId, dto);

        verify(validarUsuarioExistente).execute(usuarioId);
        verify(validarProprietarioEndereco).execute(enderecoId, usuarioId);
        verify(validarEnderecoExistente).execute(enderecoId);
        verify(enderecoRepository).deleteById(enderecoId);
    }
}

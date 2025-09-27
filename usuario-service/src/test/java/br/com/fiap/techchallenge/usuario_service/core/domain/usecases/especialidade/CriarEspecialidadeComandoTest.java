package br.com.fiap.techchallenge.usuario_service.core.domain.usecases.especialidade;

import br.com.fiap.techchallenge.core.domain.entities.Especialidade;
import br.com.fiap.techchallenge.core.dtos.especialidade.CriarEspecialidadeCommandDto;
import br.com.fiap.techchallenge.core.exceptions.BusinessException;
import br.com.fiap.techchallenge.core.gateways.EspecialidadeRepository;
import br.com.fiap.techchallenge.core.domain.usecases.especialidade.CriarEspecialidadeComando;
import br.com.fiap.techchallenge.infrastructure.services.ValidarNomeEspecialidadeDuplicado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CriarEspecialidadeComandoTest {

    private EspecialidadeRepository repository;
    private ValidarNomeEspecialidadeDuplicado validarNomeDuplicado;
    private CriarEspecialidadeComando comando;

    @BeforeEach
    void setUp() {
        repository = mock(EspecialidadeRepository.class);
        validarNomeDuplicado = mock(ValidarNomeEspecialidadeDuplicado.class);
        comando = new CriarEspecialidadeComando(repository, validarNomeDuplicado);
    }

    @Test
    void deveLancarErroQuandoNomeVazio() {
        CriarEspecialidadeCommandDto dto = new CriarEspecialidadeCommandDto("", "desc");
        assertThrows(BusinessException.class, () -> comando.execute(dto));
        verifyNoInteractions(repository);
        verifyNoInteractions(validarNomeDuplicado);
    }

    @Test
    void deveCriarEspecialidadeComSucesso() {
        CriarEspecialidadeCommandDto dto = new CriarEspecialidadeCommandDto("Cardiologia", "Cuidar do coração");
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Especialidade esp = comando.execute(dto);

        verify(validarNomeDuplicado).execute("Cardiologia");
        verify(repository).save(any(Especialidade.class));
        assertThat(esp.getNome()).isEqualTo("Cardiologia");
        assertThat(esp.getDescricao()).isEqualTo("Cuidar do coração");
        assertThat(esp.isAtivo()).isTrue();
        assertThat(esp.getDataCriacao()).isNotNull();
        assertThat(esp.getDataAtualizacao()).isNotNull();
    }
}

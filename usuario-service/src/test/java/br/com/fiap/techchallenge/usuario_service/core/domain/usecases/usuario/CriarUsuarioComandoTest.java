package br.com.fiap.techchallenge.usuario_service.core.domain.usecases.usuario;

import br.com.fiap.techchallenge.core.domain.entities.Especialidade;
import br.com.fiap.techchallenge.core.domain.entities.Usuario;
import br.com.fiap.techchallenge.core.dtos.usuario.CriarUsuarioComandoDto;
import br.com.fiap.techchallenge.core.domain.usecases.usuario.CriarUsuarioComando;
import br.com.fiap.techchallenge.core.enums.Perfil;
import br.com.fiap.techchallenge.core.gateways.EspecialidadeRepository;
import br.com.fiap.techchallenge.core.gateways.UsuarioRepository;
import br.com.fiap.techchallenge.core.utils.usuario.CriarUsuarioBase;
import br.com.fiap.techchallenge.infrastructure.services.ValidarCpfExistente;
import br.com.fiap.techchallenge.infrastructure.services.ValidarEmailExistente;
import br.com.fiap.techchallenge.infrastructure.services.ValidarLoginExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CriarUsuarioComandoTest {

    private UsuarioRepository usuarioRepository;
    private EspecialidadeRepository especialidadeRepository;
    private ValidarEmailExistente validarEmailExistente;
    private ValidarCpfExistente validarCpfExistente;
    private ValidarLoginExistente validarLoginExistente;
    private CriarUsuarioBase criarUsuarioBase;

    private CriarUsuarioComando comando;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        especialidadeRepository = mock(EspecialidadeRepository.class);
        validarEmailExistente = mock(ValidarEmailExistente.class);
        validarCpfExistente = mock(ValidarCpfExistente.class);
        validarLoginExistente = mock(ValidarLoginExistente.class);
        criarUsuarioBase = mock(CriarUsuarioBase.class);
        comando = new CriarUsuarioComando(usuarioRepository, especialidadeRepository, validarEmailExistente,
                validarCpfExistente, validarLoginExistente, criarUsuarioBase);
    }

    @Test
    void deveCriarUsuarioEAssociarEspecialidadesQuandoMedico() {
        CriarUsuarioComandoDto dto = new CriarUsuarioComandoDto();
        dto.setNome("Ana");
        dto.setCpf("123");
        dto.setDataNascimento(LocalDate.of(2000, 1, 1));
        dto.setEmail("ana@ex.com");
        dto.setSenha("s");
        dto.setLogin("ana");
        dto.setPerfilId(Perfil.MEDICO.getCodigo());
        dto.setTelefone("999");
        dto.setEspecialidadeIds(List.of(1L, 1L, 2L)); // contem duplicado

        Usuario base = new Usuario();
        when(criarUsuarioBase.execute(dto)).thenReturn(base);
        var esp1 = new Especialidade();
        esp1.setId(1L);
        var esp2 = new Especialidade();
        esp2.setId(2L);
        when(especialidadeRepository.findById(1L)).thenReturn(Optional.of(esp1));
        when(especialidadeRepository.findById(2L)).thenReturn(Optional.of(esp2));
        when(usuarioRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Usuario salvo = comando.execute(dto);

        verify(validarEmailExistente).execute("ana@ex.com");
        verify(validarCpfExistente).execute("123");
        verify(validarLoginExistente).execute("ana");
        assertThat(salvo.getPerfilId()).isEqualTo(Perfil.MEDICO.getCodigo());
        assertThat(salvo.getEspecialidades()).extracting(Especialidade::getId).containsExactlyInAnyOrder(1L, 2L);
        verify(usuarioRepository).save(base);
    }

    @Test
    void naoAssociaEspecialidadesQuandoNaoMedicoOuListaVazia() {
        CriarUsuarioComandoDto dto = new CriarUsuarioComandoDto();
        dto.setPerfilId(Perfil.ADMIN.getCodigo());

        Usuario base = new Usuario();
        when(criarUsuarioBase.execute(dto)).thenReturn(base);
        when(usuarioRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Usuario salvo = comando.execute(dto);
        assertThat(salvo.getEspecialidades()).isNull();
        verifyNoInteractions(especialidadeRepository);
    }
}

package br.com.fiap.techchallenge.usuario_service.core.dtos;

import br.com.fiap.techchallenge.core.dtos.usuario.CriarUsuarioComandoDto;
import br.com.fiap.techchallenge.core.dtos.usuario.AtualizarUsuarioComandoDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class UsuarioDtosTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void criarUsuarioComandoDto_ValidaObrigatoriosEFormatoEmail() {
        var dto = new CriarUsuarioComandoDto();
        dto.setEmail("invalido");
        Set<ConstraintViolation<CriarUsuarioComandoDto>> violations = validator.validate(dto);
        assertThat(violations).extracting(v -> v.getPropertyPath().toString())
                .contains("nome", "cpf", "dataNascimento", "email", "senha", "login", "perfilId", "telefone");
    }

    @Test
    void criarUsuarioComandoDto_ValidoNaoGeraViolacoes() {
        var dto = new CriarUsuarioComandoDto();
        dto.setNome("Ana");
        dto.setCpf("123");
        dto.setDataNascimento(LocalDate.of(2000, 1, 1));
        dto.setEmail("ana@ex.com");
        dto.setSenha("s");
        dto.setLogin("ana");
        dto.setPerfilId(1L);
        dto.setTelefone("999");
        Set<ConstraintViolation<CriarUsuarioComandoDto>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    @Test
    void atualizarUsuarioComandoDto_EmailFormatoValidado() {
        var dto = new AtualizarUsuarioComandoDto();
        dto.setEmail("email-invalido");
        Set<ConstraintViolation<AtualizarUsuarioComandoDto>> violations = validator.validate(dto);
        assertThat(violations).extracting(v -> v.getPropertyPath().toString()).contains("email");
    }
}

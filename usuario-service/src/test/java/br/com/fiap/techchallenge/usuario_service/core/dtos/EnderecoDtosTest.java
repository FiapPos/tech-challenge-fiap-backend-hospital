package br.com.fiap.techchallenge.usuario_service.core.dtos;

import br.com.fiap.techchallenge.core.dtos.endereco.CriarEnderecoComandoDto;
import br.com.fiap.techchallenge.core.dtos.endereco.AtualizarEnderecoComandoDto;
import br.com.fiap.techchallenge.core.dtos.endereco.DeletarEnderecoComandoDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class EnderecoDtosTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void criarEnderecoComandoDto_DeveValidarCamposObrigatorios() {
        var dto = new CriarEnderecoComandoDto();
        Set<ConstraintViolation<CriarEnderecoComandoDto>> violations = validator.validate(dto);
        assertThat(violations).extracting(ConstraintViolation::getPropertyPath).extracting(Object::toString)
                .contains("rua", "cep", "numero", "bairro", "cidade", "usuarioId");
    }

    @Test
    void atualizarEnderecoComandoDto_PermiteCamposNulosMasExigeUsuarioId() {
        var dto = new AtualizarEnderecoComandoDto();
        Set<ConstraintViolation<AtualizarEnderecoComandoDto>> violations = validator.validate(dto);
        assertThat(violations).extracting(ConstraintViolation::getPropertyPath).extracting(Object::toString)
                .contains("usuarioId");
    }

    @Test
    void deletarEnderecoComandoDto_SemObrigatoriosNoBean() {
        var dto = new DeletarEnderecoComandoDto();
        Set<ConstraintViolation<DeletarEnderecoComandoDto>> violations = validator.validate(dto);
        // Sem anotacoes Bean Validation nesta classe
        assertThat(violations).isEmpty();
    }
}

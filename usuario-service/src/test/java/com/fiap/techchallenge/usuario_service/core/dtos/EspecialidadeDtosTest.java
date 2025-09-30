package com.fiap.techchallenge.usuario_service.core.dtos;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Especialidade;
import com.fiap.techchallenge.usuario_service.core.dtos.especialidade.AtualizarEspecialidadeCommandDto;
import com.fiap.techchallenge.usuario_service.core.dtos.especialidade.CriarEspecialidadeCommandDto;
import com.fiap.techchallenge.usuario_service.core.dtos.especialidade.EspecialidadeResponse;
import com.fiap.techchallenge.usuario_service.core.dtos.especialidadesDoMedico.AssociarEspecialidadeMedicoDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class EspecialidadeDtosTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void criarEspecialidadeCommandDto_ExigeNome() {
        var dto = new CriarEspecialidadeCommandDto(null, null);
        Set<ConstraintViolation<CriarEspecialidadeCommandDto>> violations = validator.validate(dto);
        assertThat(violations)
                .isNotEmpty()
                .anySatisfy(v -> {
                    assertThat(v.getPropertyPath().toString()).isEqualTo("novoNome");
                });
    }

    @Test
    void atualizarEspecialidadeCommandDto_SemObrigatorio() {
        var dto = new AtualizarEspecialidadeCommandDto(null, null);
        Set<ConstraintViolation<AtualizarEspecialidadeCommandDto>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    @Test
    void associarEspecialidadeMedicoDto_ExigeId() {
        var dto = new AssociarEspecialidadeMedicoDto();
        Set<ConstraintViolation<AssociarEspecialidadeMedicoDto>> violations = validator.validate(dto);
        assertThat(violations).extracting(v -> v.getPropertyPath().toString()).contains("especialidadeId");
    }

    @Test
    void especialidadeResponse_FromDomainMapeiaCampos() {
        var e = new Especialidade();
        e.setId(10L);
        e.setNome("Cardio");
        e.setDescricao("desc");
        e.setAtivo(true);
        e.setDataCriacao(LocalDateTime.now().minusDays(1));
        e.setDataAtualizacao(LocalDateTime.now());

        var resp = EspecialidadeResponse.fromDomain(e);
        assertThat(resp.id()).isEqualTo(10L);
        assertThat(resp.nome()).isEqualTo("Cardio");
        assertThat(resp.descricao()).isEqualTo("desc");
        assertThat(resp.ativo()).isTrue();
        assertThat(resp.dataCriacao()).isEqualTo(e.getDataCriacao());
        assertThat(resp.dataAtualizacao()).isEqualTo(e.getDataAtualizacao());
    }
}

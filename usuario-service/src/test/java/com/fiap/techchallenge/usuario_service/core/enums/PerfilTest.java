package com.fiap.techchallenge.usuario_service.core.enums;

import com.fiap.techchallenge.usuario_service.core.enums.Perfil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PerfilTest {

    @Test
    void getCodigoDeveRetornarCodigoCorreto() {
        assertThat(Perfil.ADMIN.getCodigo()).isEqualTo(0L);
        assertThat(Perfil.MEDICO.getCodigo()).isEqualTo(1L);
        assertThat(Perfil.PACIENTE.getCodigo()).isEqualTo(2L);
        assertThat(Perfil.ENFERMEIRO.getCodigo()).isEqualTo(3L);
    }

    @Test
    void fromCodigoDeveRetornarEnumCorreto() {
        assertThat(Perfil.fromCodigo(0L)).isEqualTo(Perfil.ADMIN);
        assertThat(Perfil.fromCodigo(1L)).isEqualTo(Perfil.MEDICO);
        assertThat(Perfil.fromCodigo(2L)).isEqualTo(Perfil.PACIENTE);
        assertThat(Perfil.fromCodigo(3L)).isEqualTo(Perfil.ENFERMEIRO);
    }

    @Test
    void fromCodigoDeveLancarQuandoInvalidoOuNulo() {
        assertThrows(IllegalArgumentException.class, () -> Perfil.fromCodigo(999L));
        // Implementação lança IllegalArgumentException também para null
        assertThrows(IllegalArgumentException.class, () -> Perfil.fromCodigo(null));
    }
}

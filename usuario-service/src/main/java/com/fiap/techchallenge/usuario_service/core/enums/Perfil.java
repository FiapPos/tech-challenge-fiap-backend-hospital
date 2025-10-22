package com.fiap.techchallenge.usuario_service.core.enums;

import java.util.Arrays;

/**
 * Enum que representa os perfis de usuário do sistema educacional.
 * Os valores devem corresponder aos dados inseridos na tabela 'perfil' pela
 * migração V2.
 */
public enum Perfil {
    ADMIN(0L),
    PROFESSOR(1L),
    ESTUDANTE(2L),
    COORDENADOR(3L);

    private final Long codigo;

    Perfil(Long codigo) {
        this.codigo = codigo;
    }

    public Long getCodigo() {
        return codigo;
    }

    public static Perfil fromCodigo(Long codigo) {
        return Arrays.stream(values())
                .filter(perfil -> perfil.getCodigo().equals(codigo))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Perfil não encontrado"));
    }
}

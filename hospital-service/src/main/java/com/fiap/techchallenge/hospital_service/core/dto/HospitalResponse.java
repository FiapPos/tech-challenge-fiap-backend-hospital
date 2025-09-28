package com.fiap.techchallenge.hospital_service.core.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HospitalResponse {

    private Long id;
    private String nome;
    private String endereco;
    private String telefone;
    private String email;
    private String especialidades;
    private Boolean ativo;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}

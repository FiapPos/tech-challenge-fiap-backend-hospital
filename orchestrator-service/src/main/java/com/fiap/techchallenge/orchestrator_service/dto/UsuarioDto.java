package com.fiap.techchallenge.orchestrator_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDto {
    private Long id;
    private String login;
    private String senha; // hashed password
    private String nome;
    private String email;
    private String cpf;
    private boolean ativo;
    private LocalDate dataNascimento;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private List<PerfilDto> perfil;
}

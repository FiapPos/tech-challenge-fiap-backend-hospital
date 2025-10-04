package com.fiap.techchallenge.appointment_service.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {

    private Long id;
    private String login;
    private String senha;
    private String nome;
    private String email;
    private String cpf;
    private boolean ativo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private PerfilResponse perfil;
}
package com.fiap.techchallenge.usuario_service.core.dtos.usuario;

import jakarta.validation.constraints.Email;
import java.time.LocalDate;
import lombok.Data;

@Data
public class AtualizarUsuarioComandoDto {
    private String nome;

    @Email(message = "Email deve ser válido")
    private String email;

    private String senha;

    private String login;

    private String telefone;

    private String cpf;

    private LocalDate dataNascimento;

    // Campos de prioridade para redirecionamento de consultas
    private Boolean idoso;
    private Boolean gestante;
    private Boolean pcd; // Pessoa com Deficiência
}
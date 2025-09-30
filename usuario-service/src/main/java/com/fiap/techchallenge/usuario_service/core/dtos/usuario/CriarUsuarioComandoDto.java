package com.fiap.techchallenge.usuario_service.core.dtos.usuario;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

import java.time.LocalDate;

@Getter
@Setter
public class CriarUsuarioComandoDto {

    @NotBlank(message = "{nome.obrigatorio}")
    private String nome;

    @NotBlank(message = "{cpf.obrigatorio}")
    private String cpf;

    @NotNull(message = "{data.nascimento.obrigatoria}")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;

    @Email(message = "{email.invalido}")
    @NotBlank(message = "{email.obrigatorio}")
    private String email;

    @NotBlank(message = "{senha.obrigatoria}")
    private String senha;

    @NotBlank(message = "{login.obrigatorio}")
    private String login;

    @NotNull(message = "{perfil.obrigatorio}")
    private Long perfilId;

    @NotBlank(message = "{telefone.obrigatorio}")
    private String telefone;

    private List<Long> especialidadeIds;
}

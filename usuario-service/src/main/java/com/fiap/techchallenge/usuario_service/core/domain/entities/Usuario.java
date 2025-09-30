package com.fiap.techchallenge.usuario_service.core.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    private Long id;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private String telefone;
    private String email;
    private String senha;
    private String login;
    private Boolean ativo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private LocalDateTime dataDesativacao;
    private Long perfilId;
    private PerfilDoUsuario perfil;
    private List<Especialidade> especialidades;

    public void trocaSenha(String novaSenha) {
        setSenha(novaSenha);
        this.dataAtualizacao = LocalDateTime.now();
    }

    public boolean isAtivo() {
        return ativo;
    }

    public Set<PerfilDoUsuario> getPerfis() {
        Set<PerfilDoUsuario> perfis = new HashSet<>();
        if (this.perfil != null) {
            perfis.add(this.perfil);
        }
        return perfis;
    }
}

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

    // Campos de prioridade para redirecionamento de consultas
    private Boolean idoso;
    private Boolean gestante;
    private Boolean pcd; // Pessoa com Deficiência

    /**
     * Calcula o peso de prioridade do usuário.
     * Quanto maior o peso, maior a prioridade para receber consultas canceladas.
     * Prioridade: PCD (3) > Idoso (2) > Gestante (1)
     */
    public int calcularPesoPrioridade() {
        int peso = 0;
        if (Boolean.TRUE.equals(pcd)) peso += 3;
        if (Boolean.TRUE.equals(idoso)) peso += 2;
        if (Boolean.TRUE.equals(gestante)) peso += 1;
        return peso;
    }

    /**
     * Verifica se o usuário possui algum critério de prioridade
     */
    public boolean isPrioritario() {
        return Boolean.TRUE.equals(idoso) ||
               Boolean.TRUE.equals(gestante) ||
               Boolean.TRUE.equals(pcd);
    }

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

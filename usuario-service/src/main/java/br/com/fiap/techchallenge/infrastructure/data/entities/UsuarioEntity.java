package br.com.fiap.techchallenge.infrastructure.data.entities;

// TODO: Reativar imports após resolver problemas de compilação
import br.com.fiap.techchallenge.core.domain.entities.Usuario;
import br.com.fiap.techchallenge.core.domain.entities.PerfilDoUsuario;
import br.com.fiap.techchallenge.core.domain.entities.Especialidade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "cpf", nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "senha", nullable = false)
    private String senha;

    @Column(name = "login", nullable = false, unique = true, length = 50)
    private String login;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(name = "data_desativacao")
    private LocalDateTime dataDesativacao;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EnderecoEntity> enderecos = new ArrayList<>();

    @Column(name = "perfil_id")
    private Long perfilId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perfil_id", insertable = false, updatable = false)
    @Setter(AccessLevel.NONE) // torna o campo efetivamente somente leitura na entidade
    private PerfilEntity perfil;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "usuarios_especialidades", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "especialidade_id"))
    private List<EspecialidadeEntity> especialidades = new ArrayList<>();

    public Usuario toDomain() {
        PerfilDoUsuario perfilConvertido = this.perfil != null ? this.perfil.toDomain() : null;

        // Convert EspecialidadeEntity list to Especialidade list
        List<Especialidade> especialidadesList = new ArrayList<>();
        if (this.especialidades != null) {
            for (EspecialidadeEntity entity : this.especialidades) {
                especialidadesList.add(entity.toDomain());
            }
        }

        return new Usuario(
                this.id,
                this.nome,
                this.cpf,
                this.dataNascimento,
                this.telefone,
                this.email,
                this.senha,
                this.login,
                this.ativo,
                this.dataCriacao,
                this.dataAtualizacao,
                this.dataDesativacao,
                this.perfilId,
                perfilConvertido,
                especialidadesList);
    }

    public static UsuarioEntity fromDomain(Usuario usuario) {
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(usuario.getId());
        entity.setNome(usuario.getNome());
        entity.setCpf(usuario.getCpf());
        entity.setDataNascimento(usuario.getDataNascimento());
        entity.setTelefone(usuario.getTelefone());
        entity.setEmail(usuario.getEmail());
        entity.setSenha(usuario.getSenha());
        entity.setLogin(usuario.getLogin());
        entity.setAtivo(usuario.getAtivo());
        entity.setDataCriacao(usuario.getDataCriacao());
        entity.setDataAtualizacao(usuario.getDataAtualizacao());
        entity.setDataDesativacao(usuario.getDataDesativacao());

        if (usuario.getPerfilId() != null) {
            entity.setPerfilId(usuario.getPerfilId());
        }

        return entity;
    }
}

package br.com.fiap.techchallenge.infrastructure.data.entities;

import br.com.fiap.techchallenge.core.domain.entities.Especialidade;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import br.com.fiap.techchallenge.infrastructure.data.entities.UsuarioEntity;

@Entity
@Table(name = "especialidade")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EspecialidadeEntity {
    @ManyToMany(mappedBy = "especialidades", fetch = FetchType.LAZY)
    @Builder.Default
    private List<UsuarioEntity> usuarios = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 120)
    private String nome;

    @Column(length = 500)
    private String descricao;

    @Column(nullable = false)
    private boolean ativo;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    @Column(nullable = false)
    private LocalDateTime dataAtualizacao;

    public static EspecialidadeEntity fromDomain(Especialidade e) {
        return EspecialidadeEntity.builder()
                .id(e.getId())
                .nome(e.getNome())
                .descricao(e.getDescricao())
                .ativo(e.isAtivo())
                .dataCriacao(e.getDataCriacao())
                .dataAtualizacao(e.getDataAtualizacao())
                .build();
    }

    public Especialidade toDomain() {
        return Especialidade.builder()
                .id(this.id)
                .nome(this.nome)
                .descricao(this.descricao)
                .ativo(this.ativo)
                .dataCriacao(this.dataCriacao)
                .dataAtualizacao(this.dataAtualizacao)
                .build();
    }
}

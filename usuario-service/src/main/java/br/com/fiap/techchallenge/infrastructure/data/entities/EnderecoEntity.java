package br.com.fiap.techchallenge.infrastructure.data.entities;

import br.com.fiap.techchallenge.core.domain.entities.Endereco;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "endereco")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rua", nullable = false, length = 255)
    private String rua;

    @Column(name = "cep", nullable = false, length = 10)
    private String cep;

    @Column(name = "numero", length = 20)
    private String numero;

    @Column(name = "bairro", length = 100)
    private String bairro;

    @Column(name = "cidade", length = 100)
    private String cidade;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", insertable = false, updatable = false)
    private UsuarioEntity usuario;

    @CreationTimestamp
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    public Endereco toDomain() {
        Endereco endereco = new Endereco();
        endereco.setId(this.id);
        endereco.setRua(this.rua);
        endereco.setCep(this.cep);
        endereco.setNumero(this.numero);
        endereco.setBairro(this.bairro);
        endereco.setCidade(this.cidade);
        endereco.setUsuarioId(this.usuarioId);
        endereco.setDataCriacao(this.dataCriacao);
        endereco.setDataAtualizacao(this.dataAtualizacao);

        // Converter usuario se existir
        if (this.usuario != null) {
            endereco.setUsuario(this.usuario.toDomain());
        }

        return endereco;
    }

    public static EnderecoEntity fromDomain(Endereco endereco) {
        EnderecoEntity entity = new EnderecoEntity();
        entity.setId(endereco.getId());
        entity.setRua(endereco.getRua());
        entity.setCep(endereco.getCep());
        entity.setNumero(endereco.getNumero());
        entity.setBairro(endereco.getBairro());
        entity.setCidade(endereco.getCidade());
        entity.setUsuarioId(endereco.getUsuarioId());
        entity.setDataCriacao(endereco.getDataCriacao());
        entity.setDataAtualizacao(endereco.getDataAtualizacao());
        return entity;
    }
}

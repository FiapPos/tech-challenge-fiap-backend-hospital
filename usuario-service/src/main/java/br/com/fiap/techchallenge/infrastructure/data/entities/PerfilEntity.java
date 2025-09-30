
package br.com.fiap.techchallenge.infrastructure.data.entities;

import br.com.fiap.techchallenge.core.enums.Perfil;
import br.com.fiap.techchallenge.core.domain.entities.PerfilDoUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "perfil")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerfilEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "nome_perfil", nullable = false, length = 100)
    private Perfil nomePerfil;

    // Método para converter para a entidade de domínio
    public PerfilDoUsuario toDomain() {
        return new PerfilDoUsuario(this.id, this.nomePerfil, null);
    }

    // Método estático para criar a partir da entidade de domínio
    public static PerfilEntity fromDomain(PerfilDoUsuario perfilDoUsuario) {
        PerfilEntity entity = new PerfilEntity();
        entity.setId(perfilDoUsuario.getId());
        entity.setNomePerfil(perfilDoUsuario.getPerfil());
        return entity;
    }
}

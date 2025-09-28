
package com.fiap.techchallenge.usuario_service.infrastructure.data.entities;

import com.fiap.techchallenge.usuario_service.core.enums.Perfil;
import com.fiap.techchallenge.usuario_service.core.domain.entities.PerfilDoUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

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

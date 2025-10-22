package com.fiap.techchallenge.usuario_service.core.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import com.fiap.techchallenge.usuario_service.core.enums.Perfil;

@Data
@EqualsAndHashCode(exclude = "usuario")
@ToString(exclude = "usuario")
@NoArgsConstructor
@AllArgsConstructor
public class PerfilDoUsuario {

    private Long id;
    private Perfil perfil;
    private Usuario usuario;

    public boolean isAdmin() {
        return Perfil.ADMIN.equals(this.perfil);
    }

    public boolean isProfessor() {
        return Perfil.PROFESSOR.equals(this.perfil);
    }

    public boolean isEstudante() {
        return Perfil.ESTUDANTE.equals(this.perfil);
    }

    public boolean isCoordenador() {
        return Perfil.COORDENADOR.equals(this.perfil);
    }
}

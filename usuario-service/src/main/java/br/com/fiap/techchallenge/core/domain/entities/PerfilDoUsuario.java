package br.com.fiap.techchallenge.core.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import br.com.fiap.techchallenge.core.enums.Perfil;

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

    public boolean isMedico() {
        return Perfil.MEDICO.equals(this.perfil);
    }

    public boolean isPaciente() {
        return Perfil.PACIENTE.equals(this.perfil);
    }

    public boolean isEnfermeiro() {
        return Perfil.ENFERMEIRO.equals(this.perfil);
    }
}

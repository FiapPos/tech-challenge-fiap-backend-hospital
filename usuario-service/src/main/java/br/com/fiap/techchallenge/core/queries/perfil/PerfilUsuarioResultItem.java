package br.com.fiap.techchallenge.core.queries.perfil;

import br.com.fiap.techchallenge.core.enums.Perfil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerfilUsuarioResultItem {
    private Perfil perfil;
    private long codigo;
}

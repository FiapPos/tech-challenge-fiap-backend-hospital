package br.com.fiap.techchallenge.core.domain.usecases.usuario;

import br.com.fiap.techchallenge.core.domain.entities.Usuario;
import br.com.fiap.techchallenge.core.domain.entities.PerfilDoUsuario;
import br.com.fiap.techchallenge.core.enums.Perfil;
import br.com.fiap.techchallenge.core.utils.usuario.perfilUsuario.VerificarPerfilUsuarioExistente;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssociarPerfilUsuarioComando {
    private final VerificarPerfilUsuarioExistente verificarPerfilExistente;

    public void execute(Usuario usuario, Perfil perfil) {
        verificarPerfilExistente.execute(usuario, perfil);
        associarPerfilAoUsuario(usuario, perfil);
    }

    private void associarPerfilAoUsuario(Usuario usuario, Perfil perfil) {
        PerfilDoUsuario perfilUsuario = new PerfilDoUsuario();
        perfilUsuario.setUsuario(usuario);
        perfilUsuario.setPerfil(perfil);
        // O domínio mantém um único PerfilDoUsuario em Usuario#perfil; setamos
        // diretamente
        usuario.setPerfil(perfilUsuario);
    }
}

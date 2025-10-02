package com.fiap.techchallenge.usuario_service.core.domain.usecases.login;

import com.fiap.techchallenge.usuario_service.core.dtos.login.CredenciaisUsuarioDto;
import com.fiap.techchallenge.usuario_service.core.dtos.login.DetalhesUsuarioDto;
import com.fiap.techchallenge.usuario_service.core.domain.entities.Usuario;
import com.fiap.techchallenge.usuario_service.core.enums.Perfil;
import com.fiap.techchallenge.usuario_service.core.exceptions.BadRequestException;
import com.fiap.techchallenge.usuario_service.core.exceptions.CredenciaisInvalidasException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AutenticaLoginComando {

    private final AuthenticationManager authenticationManager;

    public AutenticaLoginComando(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public Usuario login(CredenciaisUsuarioDto credentials) throws Exception {
        try {
            Authentication authentication = autenticarCredenciais(credentials);
            Usuario usuario = extrairUsuario(authentication);
            // If perfil was provided in the request, validate it. Otherwise accept the
            // user's existing profiles.
            if (credentials.perfil() != null) {
                validarPerfilUsuario(usuario, credentials.perfil());
            }
            return usuario;
        } catch (Exception e) {
            tratarExcecao(e);
            return null;
        }
    }

    private Authentication autenticarCredenciais(CredenciaisUsuarioDto credentials) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                credentials.login(), credentials.senha());
        return authenticationManager.authenticate(token);
    }

    private Usuario extrairUsuario(Authentication authentication) {
        DetalhesUsuarioDto detalhes = (DetalhesUsuarioDto) authentication.getPrincipal();
        return detalhes.usuario();
    }

    private void validarPerfilUsuario(Usuario usuario, Perfil perfilSolicitado) {
        boolean temPerfil = usuario.getPerfis().stream()
                .anyMatch(pd -> pd.getPerfil() == perfilSolicitado);

        if (!temPerfil) {
            throw new CredenciaisInvalidasException();
        }
    }

    private void tratarExcecao(Exception e) throws Exception {
        if (e instanceof BadRequestException) {
            throw e;
        }
        throw new CredenciaisInvalidasException();
    }
}
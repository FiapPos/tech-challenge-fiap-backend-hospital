package com.fiap.techchallenge.usuario_service.core.domain.usecases.login;

import com.fiap.techchallenge.usuario_service.core.dtos.login.DetalhesUsuarioDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EncontraUsuarioPorLoginComando implements UserDetailsService {

    private final AutenticaUsuarioComando userAuthenticationService;

    public EncontraUsuarioPorLoginComando(AutenticaUsuarioComando userAuthenticationService) {
        this.userAuthenticationService = userAuthenticationService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new DetalhesUsuarioDto(userAuthenticationService.getByLogin(username));
    }
}
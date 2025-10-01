package com.fiap.techchallenge.appointment_service.core.service;

import com.fiap.techchallenge.appointment_service.core.client.OrchestratorClient;
import com.fiap.techchallenge.appointment_service.core.dto.UserDetailsImpl;
import com.fiap.techchallenge.appointment_service.core.dto.UsuarioResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final OrchestratorClient orchestratorClient;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        log.debug("Buscando usuário '{}' através do orchestrator-service", login);
        UsuarioResponse usuario = orchestratorClient.findUsuarioByLogin(login);

        if (usuario == null) {
            log.warn("Usuário '{}' não encontrado no usuario-service", login);
            throw new UsernameNotFoundException("Usuário não encontrado: " + login);
        }

        log.debug("Usuário '{}' encontrado com perfil: {}", login,
                usuario.getPerfil() != null ? usuario.getPerfil().getTipo() : "NENHUM");
        return new UserDetailsImpl(usuario);
    }
}
package com.fiap.techchallenge.usuario_service.core.domain.usecases.login;

import com.fiap.techchallenge.usuario_service.core.dtos.login.DetalhesUsuarioDto;
import com.fiap.techchallenge.usuario_service.core.domain.entities.Usuario;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class FiltroAutenticacaoJwt extends OncePerRequestFilter {

    private static final String AUTHENTICATION_HEADER = "Bearer ";
    private static final Logger logger = LoggerFactory.getLogger(FiltroAutenticacaoJwt.class);

    private final AutenticaJwtComando jwtService;
    private final AutenticaUsuarioComando userAuthenticationService;

    public FiltroAutenticacaoJwt(AutenticaJwtComando jwtService, AutenticaUsuarioComando userAuthenticationService) {
        this.jwtService = jwtService;
        this.userAuthenticationService = userAuthenticationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith(AUTHENTICATION_HEADER)) {
            String token = authorizationHeader.substring(AUTHENTICATION_HEADER.length());
            if (!token.isEmpty())
                tryToAuthenticate(token);
        }

        filterChain.doFilter(request, response);
    }

    private void tryToAuthenticate(String token) {
        try {
            String login = jwtService.getLogin(token);
            Usuario user = userAuthenticationService.getByLogin(login);
            DetalhesUsuarioDto userDetails = new DetalhesUsuarioDto(user);

            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        } catch (Exception e) {
            logger.warn("Falha ao autenticar token JWT. Token: {}. Erro: {}", token, e.getMessage());
        }
    }
}
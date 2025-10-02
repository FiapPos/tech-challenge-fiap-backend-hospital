package com.fiap.techchallenge.usuario_service.core.domain.usecases.login;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Usuario;
import com.fiap.techchallenge.usuario_service.core.enums.Perfil;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;

import java.util.Date;

@Service
public class AutenticaJwtComando {

    @Value("${jwt.secret}")
    private String SECRET;

    @Value("${jwt.expiration-time}")
    private long EXPIRATION_TIME;

    public String createToken(Usuario user, Perfil perfil) {
        return JWT.create()
                .withSubject(user.getLogin())
                .withClaim("perfil", perfil.name())
                .withClaim("userId", user.getId())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SECRET));
    }

    public String getLogin(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET))
                .build()
                .verify(token)
                .getSubject();
    }
}
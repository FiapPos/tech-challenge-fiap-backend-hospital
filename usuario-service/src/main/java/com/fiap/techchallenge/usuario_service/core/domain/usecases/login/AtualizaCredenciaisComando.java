package com.fiap.techchallenge.usuario_service.core.domain.usecases.login;

import com.fiap.techchallenge.usuario_service.core.dtos.login.AtualizaCredenciaisComandoDto;
import com.fiap.techchallenge.usuario_service.infrastructure.services.UsuarioLogado;
import com.fiap.techchallenge.usuario_service.core.gateways.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class AtualizaCredenciaisComando {

    private final UsuarioLogado usuarioLogado;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;

    public AtualizaCredenciaisComando(UsuarioLogado usuarioLogado, PasswordEncoder passwordEncoder,
            UsuarioRepository usuarioRepository) {
        this.usuarioLogado = usuarioLogado;
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
    }

    public void execute(AtualizaCredenciaisComandoDto atualizaCredenciaisComandoDto) {
        Assert.notNull(atualizaCredenciaisComandoDto.senha(), "A senha não pode ser nula");
        Assert.notNull(atualizaCredenciaisComandoDto.confirmacaoSenha(), "A confirmação da senha não pode ser nula");
        Assert.notNull(usuarioLogado.getUsuarioId(), "A pessoa usuária precisa estar logada para trocar a senha");

        usuarioRepository
                .findById(usuarioLogado.getUsuarioId())
                .ifPresent(usuario -> {
                    usuario.trocaSenha(passwordEncoder.encode(atualizaCredenciaisComandoDto.senha()));
                    usuarioRepository.save(usuario);
                });
    }
}
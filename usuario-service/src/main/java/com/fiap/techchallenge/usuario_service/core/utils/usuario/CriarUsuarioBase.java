package com.fiap.techchallenge.usuario_service.core.utils.usuario;

import com.fiap.techchallenge.usuario_service.core.dtos.usuario.CriarUsuarioComandoDto;
import com.fiap.techchallenge.usuario_service.core.domain.entities.Usuario;
import com.fiap.techchallenge.usuario_service.core.shared.CompartilhadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CriarUsuarioBase {
    private final PasswordEncoder passwordEncoder;
    private final CompartilhadoService sharedService;

    public Usuario execute(CriarUsuarioComandoDto dto) {
        Usuario usuario = new Usuario();
        configurarDadosBasicos(usuario, dto);
        configurarDadosSeguranca(usuario, dto);

        return usuario;
    }

    private void configurarDadosBasicos(Usuario usuario, CriarUsuarioComandoDto dto) {
        usuario.setNome(dto.getNome());
        usuario.setCpf(dto.getCpf());
        usuario.setDataNascimento(dto.getDataNascimento());
        usuario.setTelefone(dto.getTelefone());
        usuario.setEmail(dto.getEmail());
        usuario.setLogin(dto.getLogin());

        // Configurar campos de prioridade
        usuario.setIdoso(dto.getIdoso() != null ? dto.getIdoso() : false);
        usuario.setGestante(dto.getGestante() != null ? dto.getGestante() : false);
        usuario.setPcd(dto.getPcd() != null ? dto.getPcd() : false);
    }

    private void configurarDadosSeguranca(Usuario usuario, CriarUsuarioComandoDto dto) {
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setDataCriacao(sharedService.getCurrentDateTime());
        usuario.setAtivo(true);
    }
}

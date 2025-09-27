package br.com.fiap.techchallenge.core.utils.usuario;

import br.com.fiap.techchallenge.core.dtos.usuario.CriarUsuarioComandoDto;
import br.com.fiap.techchallenge.core.domain.entities.Usuario;
import br.com.fiap.techchallenge.core.shared.CompartilhadoService;
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
    }

    private void configurarDadosSeguranca(Usuario usuario, CriarUsuarioComandoDto dto) {
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setDataCriacao(sharedService.getCurrentDateTime());
        usuario.setAtivo(true);
    }
}

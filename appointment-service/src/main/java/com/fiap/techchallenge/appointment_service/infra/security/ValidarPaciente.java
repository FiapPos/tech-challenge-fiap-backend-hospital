package com.fiap.techchallenge.appointment_service.infra.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ValidarPaciente {

    private final UsuarioLogado usuarioLogado;

    public void validar(Long idPacienteRequisicao) {
        boolean ehPaciente = usuarioLogado.ehPaciente();
        if (!ehPaciente)
            return;

        Long idUsuarioLogado = usuarioLogado.getIdUsuarioLogado();
        if (idUsuarioLogado == null || !idUsuarioLogado.equals(idPacienteRequisicao)) {
            log.warn("Acesso negado: usuário paciente idLogado={} tentou acessar histórico do paciente idRequisicao={}",
                    idUsuarioLogado, idPacienteRequisicao);
            throw new AccessDeniedException("Paciente só pode acessar seu próprio histórico");
        }
    }
}

package com.fiap.techchallenge.usuario_service.core.domain.usecases.usuario;

import com.fiap.techchallenge.usuario_service.infrastructure.services.GeraQrCodeService;
import com.fiap.techchallenge.usuario_service.infrastructure.services.ValidarUsuarioExistente;
import org.springframework.stereotype.Service;

@Service
public class GeraQrCodeComando {

    private final ValidarUsuarioExistente validarUsuarioExistente;
    private final GeraQrCodeService geraQrCodeService;

    public GeraQrCodeComando(ValidarUsuarioExistente validarUsuarioExistente, GeraQrCodeService geraQrCodeService) {
        this.validarUsuarioExistente = validarUsuarioExistente;
        this.geraQrCodeService = geraQrCodeService;
    }

    public byte[] geraQrCode(Long usuarioId) {
        validarUsuarioExistente.execute(usuarioId);
        return geraQrCodeService.geraPng(usuarioId);
    }
}

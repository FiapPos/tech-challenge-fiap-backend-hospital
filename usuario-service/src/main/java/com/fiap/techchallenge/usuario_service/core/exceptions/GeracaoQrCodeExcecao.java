package com.fiap.techchallenge.usuario_service.core.exceptions;

public class GeracaoQrCodeExcecao extends RuntimeException {
    public GeracaoQrCodeExcecao() {
        super("Houve um problema ao gerar o QR Code para o pagamento, tente novamente mais tarde.");
    }
}

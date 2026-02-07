package com.fiap.techchallenge.usuario_service.infrastructure.services;

import com.fiap.techchallenge.usuario_service.core.exceptions.GeracaoQrCodeExcecao;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class GeraQrCodeService {

    private static final String BOT_USERNAME = "sus_agendamento_bot";

    public byte[] geraPng(Long pacienteId) {
        try {
            String linkMagico = "https://t.me/" + BOT_USERNAME + "?start=" + pacienteId;
            return geraCodigoQrPng(linkMagico);
        } catch (Exception e) {
            throw new GeracaoQrCodeExcecao();
        }

    }

    private byte[] geraCodigoQrPng(String barcodeText) throws Exception {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = barcodeWriter
                .encode(barcodeText, BarcodeFormat.QR_CODE, 300, 300);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", out);
            return out.toByteArray();
        }
    }
}
package com.fiap.techchallenge.usuario_service.core.utils.endereco;

import com.fiap.techchallenge.usuario_service.core.exceptions.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class ValidarCamposEndereco {
    public void validar(String rua, String cep, String numero, String bairro, String cidade) {
        if (rua == null && cep == null && numero == null && bairro == null && cidade == null) {
            throw new BadRequestException("atualizar.endereco.nenhum.campo");
        }
    }
}

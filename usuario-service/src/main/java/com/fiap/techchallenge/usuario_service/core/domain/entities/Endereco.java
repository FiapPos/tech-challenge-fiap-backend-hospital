package com.fiap.techchallenge.usuario_service.core.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {

    private Long id;
    private String rua;
    private String cep;
    private String numero;
    private String bairro;
    private String cidade;
    private Long usuarioId;
    private Usuario usuario;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
}

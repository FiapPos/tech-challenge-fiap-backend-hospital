package br.com.fiap.techchallenge.core.dtos.endereco;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AtualizarEnderecoComandoDto {
    private String rua;
    private String cep;
    private String numero;
    private String bairro;
    private String cidade;
    @NotNull(message = "{usuario.id.obrigatorio}")
    private Long usuarioId;
}

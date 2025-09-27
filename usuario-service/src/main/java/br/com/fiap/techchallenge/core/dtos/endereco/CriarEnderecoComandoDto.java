package br.com.fiap.techchallenge.core.dtos.endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CriarEnderecoComandoDto {
    @NotBlank(message = "{rua.obrigatoria}")
    private String rua;

    @NotBlank(message = "{cep.obrigatorio}")
    private String cep;

    @NotBlank(message = "{numero.obrigatorio}")
    private String numero;

    @NotBlank(message = "{bairro.obrigatorio}")
    private String bairro;

    @NotBlank(message = "{cidade.obrigatoria}")
    private String cidade;

    @NotNull(message = "{usuario.id.obrigatorio}")
    private Long usuarioId;
}

package com.fiap.techchallenge.orchestrator_service.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HospitalDTO {

    private Long id;
    private String nome;
    private String endereco;
    private String telefone;
    private String email;
    private String especialidades;
    private Boolean ativo;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}

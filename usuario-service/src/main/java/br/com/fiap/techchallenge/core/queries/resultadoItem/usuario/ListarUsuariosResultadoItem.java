package br.com.fiap.techchallenge.core.queries.resultadoItem.usuario;

import br.com.fiap.techchallenge.core.queries.perfil.PerfilUsuarioResultItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListarUsuariosResultadoItem {
    private Long id;
    private String nome;
    private String email;
    private String login;
    private String telefone;
    private String cpf;
    private LocalDate dataNascimento;
    private List<PerfilUsuarioResultItem> perfil;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
}
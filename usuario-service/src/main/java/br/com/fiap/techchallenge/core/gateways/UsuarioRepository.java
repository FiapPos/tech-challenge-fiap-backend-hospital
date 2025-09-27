package br.com.fiap.techchallenge.core.gateways;

import br.com.fiap.techchallenge.core.domain.entities.Usuario;
import br.com.fiap.techchallenge.infrastructure.data.entities.UsuarioEntity;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    Usuario save(Usuario usuario);

    Optional<Usuario> findById(Long id);

    List<Usuario> findAll();

    void deleteById(Long id);

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    boolean existsByLogin(String login);

    List<Usuario> findByAtivo(boolean ativo);

    Optional<Usuario> findByLogin(String login);

    // Consulta todos os usuários que possuem uma especialidade específica
    List<Usuario> findByEspecialidadesNome(String nome);

    // Consulta um usuário específico que possua uma especialidade específica
    Optional<Usuario> findByIdAndEspecialidadesNome(Long usuarioId, String nome);

    void deleteAll();
}
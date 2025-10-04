package com.fiap.techchallenge.usuario_service.core.gateways;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Usuario;
import com.fiap.techchallenge.usuario_service.core.enums.Perfil;

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

<<<<<<< HEAD
    boolean existsById(Long id);
=======
    Optional<Usuario> findByIdAndPerfil(Long id, Perfil perfil);

    Optional<Usuario> findByIdAndPerfilAndEspecialidadeId(Long id, Perfil perfil, Long especialidadeId);
>>>>>>> origin/main
}
package com.fiap.techchallenge.usuario_service.infrastructure.data.repositories;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Usuario;
import com.fiap.techchallenge.usuario_service.core.enums.Perfil;
import com.fiap.techchallenge.usuario_service.core.gateways.UsuarioRepository;
import com.fiap.techchallenge.usuario_service.infrastructure.data.entities.UsuarioEntity;
import com.fiap.techchallenge.usuario_service.infrastructure.data.entities.EspecialidadeEntity;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UsuarioRepositoryImpl implements UsuarioRepository {

    private final UsuarioJpaRepository jpaRepository;
    private final JpaEspecialidadeRepository especialidadeRepository;

    public UsuarioRepositoryImpl(UsuarioJpaRepository jpaRepository,
            JpaEspecialidadeRepository especialidadeRepository) {
        this.jpaRepository = jpaRepository;
        this.especialidadeRepository = especialidadeRepository;
    }

    @Override
    public Usuario save(Usuario usuario) {
        UsuarioEntity entity = UsuarioEntity.fromDomain(usuario);

        // Sincroniza ManyToMany (usuarios_especialidades) a partir dos IDs vindos do
        // domínio
        if (usuario.getEspecialidades() != null) {
            var ids = usuario.getEspecialidades().stream()
                    .map(e -> e.getId())
                    .filter(id -> id != null)
                    .toList();
            List<EspecialidadeEntity> especialidades = ids.isEmpty()
                    ? java.util.Collections.emptyList()
                    : especialidadeRepository.findAllById(ids);
            entity.setEspecialidades(especialidades);
        }

        UsuarioEntity savedEntity = jpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return jpaRepository.findById(id)
                .map(UsuarioEntity::toDomain);
    }

    @Override
    public List<Usuario> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(UsuarioEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByLogin(String login) {
        return jpaRepository.existsByLogin(login);
    }

    @Override
    public boolean existsByCpf(String cpf) {
        return jpaRepository.existsByCpf(cpf);
    }

    @Override
    public List<Usuario> findByAtivo(boolean ativo) {
        return jpaRepository.findByAtivo(ativo, Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(UsuarioEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Usuario> findByLogin(String login) {
        return jpaRepository.findByLogin(login)
                .map(UsuarioEntity::toDomain);
    }

    @Override
    public List<Usuario> findByEspecialidadesNome(String nome) {
        return jpaRepository.findByEspecialidadesNome(nome)
                .stream()
                .map(UsuarioEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Usuario> findByIdAndEspecialidadesNome(Long usuarioId, String nome) {
        return jpaRepository.findByIdAndEspecialidadesNome(usuarioId, nome)
                .map(UsuarioEntity::toDomain);
    }

    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }

    @Override
<<<<<<< HEAD
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
=======
    public Optional<Usuario> findByIdAndPerfil(Long id, Perfil perfil) {
        return jpaRepository.findByIdAndPerfil_NomePerfil(id, perfil)
                .map(UsuarioEntity::toDomain);
    }

    @Override
    public Optional<Usuario> findByIdAndPerfilAndEspecialidadeId(Long id, Perfil perfil, Long especialidadeId) {
        return jpaRepository.findByIdAndPerfil_NomePerfilAndEspecialidadeId(id, perfil, especialidadeId)
                .map(UsuarioEntity::toDomain);
>>>>>>> origin/main
    }
}
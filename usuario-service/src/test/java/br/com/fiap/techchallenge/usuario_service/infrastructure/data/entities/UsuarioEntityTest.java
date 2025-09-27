package br.com.fiap.techchallenge.usuario_service.infrastructure.data.entities;

import br.com.fiap.techchallenge.core.domain.entities.Especialidade;
import br.com.fiap.techchallenge.core.domain.entities.PerfilDoUsuario;
import br.com.fiap.techchallenge.core.domain.entities.Usuario;
import br.com.fiap.techchallenge.core.enums.Perfil;
import br.com.fiap.techchallenge.infrastructure.data.entities.EspecialidadeEntity;
import br.com.fiap.techchallenge.infrastructure.data.entities.PerfilEntity;
import br.com.fiap.techchallenge.infrastructure.data.entities.UsuarioEntity;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioEntityTest {

    @Test
    void toDomain_deveMapearCamposBasicosPerfilEEspecialidades() throws Exception {
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(1L);
        entity.setNome("Maria");
        entity.setCpf("12345678901");
        entity.setDataNascimento(LocalDate.of(1990, 1, 1));
        entity.setTelefone("11999999999");
        entity.setEmail("maria@example.com");
        entity.setSenha("senha");
        entity.setLogin("maria");
        entity.setAtivo(true);
        LocalDateTime agora = LocalDateTime.now();
        entity.setDataCriacao(agora);
        entity.setDataAtualizacao(agora);
        entity.setDataDesativacao(null);
        entity.setPerfilId(2L);

        PerfilEntity perfilEntity = new PerfilEntity();
        perfilEntity.setId(2L);
        perfilEntity.setNomePerfil(Perfil.MEDICO);

        Field perfilField = UsuarioEntity.class.getDeclaredField("perfil");
        perfilField.setAccessible(true);
        perfilField.set(entity, perfilEntity);

        EspecialidadeEntity esp1 = EspecialidadeEntity.builder().id(10L).nome("Cardio").descricao("Desc").ativo(true)
                .dataCriacao(agora).dataAtualizacao(agora).build();
        EspecialidadeEntity esp2 = EspecialidadeEntity.builder().id(11L).nome("Dermato").descricao("Desc").ativo(false)
                .dataCriacao(agora).dataAtualizacao(agora).build();
        entity.setEspecialidades(List.of(esp1, esp2));

        Usuario domain = entity.toDomain();

        assertEquals(1L, domain.getId());
        assertEquals("Maria", domain.getNome());
        assertEquals("12345678901", domain.getCpf());
        assertEquals(LocalDate.of(1990, 1, 1), domain.getDataNascimento());
        assertEquals("11999999999", domain.getTelefone());
        assertEquals("maria@example.com", domain.getEmail());
        assertEquals("senha", domain.getSenha());
        assertEquals("maria", domain.getLogin());
        assertTrue(domain.getAtivo());
        assertEquals(agora, domain.getDataCriacao());
        assertEquals(agora, domain.getDataAtualizacao());
        assertNull(domain.getDataDesativacao());
        assertEquals(2L, domain.getPerfilId());
        assertNotNull(domain.getPerfil());
        assertEquals(Perfil.MEDICO, domain.getPerfil().getPerfil());
        assertNotNull(domain.getEspecialidades());
        assertEquals(2, domain.getEspecialidades().size());
        assertEquals("Cardio", domain.getEspecialidades().get(0).getNome());
        assertEquals("Dermato", domain.getEspecialidades().get(1).getNome());
    }

    @Test
    void fromDomain_deveMapearCamposBasicosEPerfilId() {
        Usuario domain = new Usuario();
        domain.setId(3L);
        domain.setNome("Pedro");
        domain.setCpf("98765432100");
        domain.setDataNascimento(LocalDate.of(1985, 5, 20));
        domain.setTelefone("11888888888");
        domain.setEmail("pedro@example.com");
        domain.setSenha("outra");
        domain.setLogin("pedro");
        domain.setAtivo(false);
        LocalDateTime agora = LocalDateTime.now();
        domain.setDataCriacao(agora);
        domain.setDataAtualizacao(agora);
        domain.setDataDesativacao(agora);
        domain.setPerfilId(4L);

        UsuarioEntity entity = UsuarioEntity.fromDomain(domain);

        assertEquals(3L, entity.getId());
        assertEquals("Pedro", entity.getNome());
        assertEquals("98765432100", entity.getCpf());
        assertEquals(LocalDate.of(1985, 5, 20), entity.getDataNascimento());
        assertEquals("11888888888", entity.getTelefone());
        assertEquals("pedro@example.com", entity.getEmail());
        assertEquals("outra", entity.getSenha());
        assertEquals("pedro", entity.getLogin());
        assertFalse(entity.getAtivo());
        assertEquals(agora, entity.getDataCriacao());
        assertEquals(agora, entity.getDataAtualizacao());
        assertEquals(agora, entity.getDataDesativacao());
        assertEquals(4L, entity.getPerfilId());
        assertNull(entity.getPerfil());
        assertNotNull(entity.getEspecialidades());
        assertTrue(entity.getEspecialidades().isEmpty());
    }
}

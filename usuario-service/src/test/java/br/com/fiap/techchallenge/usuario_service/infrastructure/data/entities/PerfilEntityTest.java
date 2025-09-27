package br.com.fiap.techchallenge.usuario_service.infrastructure.data.entities;

import br.com.fiap.techchallenge.core.domain.entities.PerfilDoUsuario;
import br.com.fiap.techchallenge.core.enums.Perfil;
import br.com.fiap.techchallenge.infrastructure.data.entities.PerfilEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PerfilEntityTest {

    @Test
    void toDomain_deveMapearIdENomePerfil() {
        PerfilEntity entity = new PerfilEntity();
        entity.setId(7L);
        entity.setNomePerfil(Perfil.MEDICO);

        PerfilDoUsuario domain = entity.toDomain();

        assertEquals(7L, domain.getId());
        assertEquals(Perfil.MEDICO, domain.getPerfil());
        assertNull(domain.getUsuario());
    }

    @Test
    void fromDomain_deveMapearIdEPerfil() {
        PerfilDoUsuario domain = new PerfilDoUsuario(8L, Perfil.PACIENTE, null);

        PerfilEntity entity = PerfilEntity.fromDomain(domain);

        assertEquals(8L, entity.getId());
        assertEquals(Perfil.PACIENTE, entity.getNomePerfil());
    }
}

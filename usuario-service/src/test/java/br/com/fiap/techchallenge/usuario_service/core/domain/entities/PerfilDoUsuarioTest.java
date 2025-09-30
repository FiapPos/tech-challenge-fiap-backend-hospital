package br.com.fiap.techchallenge.usuario_service.core.domain.entities;

import br.com.fiap.techchallenge.core.domain.entities.PerfilDoUsuario;
import br.com.fiap.techchallenge.core.enums.Perfil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PerfilDoUsuarioTest {

    @Test
    void deveIdentificarPerfisCorretamente() {
        PerfilDoUsuario admin = new PerfilDoUsuario(1L, Perfil.ADMIN, null);
        PerfilDoUsuario medico = new PerfilDoUsuario(2L, Perfil.MEDICO, null);
        PerfilDoUsuario paciente = new PerfilDoUsuario(3L, Perfil.PACIENTE, null);
        PerfilDoUsuario enfermeiro = new PerfilDoUsuario(4L, Perfil.ENFERMEIRO, null);

        assertTrue(admin.isAdmin());
        assertFalse(admin.isMedico());
        assertFalse(admin.isPaciente());
        assertFalse(admin.isEnfermeiro());

        assertTrue(medico.isMedico());
        assertTrue(paciente.isPaciente());
        assertTrue(enfermeiro.isEnfermeiro());
    }
}

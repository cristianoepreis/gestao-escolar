package com.ufbra.gestaoescolar.domain;

import static com.ufbra.gestaoescolar.domain.AdministradorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ufbra.gestaoescolar.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdministradorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Administrador.class);
        Administrador administrador1 = getAdministradorSample1();
        Administrador administrador2 = new Administrador();
        assertThat(administrador1).isNotEqualTo(administrador2);

        administrador2.setId(administrador1.getId());
        assertThat(administrador1).isEqualTo(administrador2);

        administrador2 = getAdministradorSample2();
        assertThat(administrador1).isNotEqualTo(administrador2);
    }
}

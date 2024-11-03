package com.ufbra.gestaoescolar.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ufbra.gestaoescolar.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdministradorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdministradorDTO.class);
        AdministradorDTO administradorDTO1 = new AdministradorDTO();
        administradorDTO1.setId(1L);
        AdministradorDTO administradorDTO2 = new AdministradorDTO();
        assertThat(administradorDTO1).isNotEqualTo(administradorDTO2);
        administradorDTO2.setId(administradorDTO1.getId());
        assertThat(administradorDTO1).isEqualTo(administradorDTO2);
        administradorDTO2.setId(2L);
        assertThat(administradorDTO1).isNotEqualTo(administradorDTO2);
        administradorDTO1.setId(null);
        assertThat(administradorDTO1).isNotEqualTo(administradorDTO2);
    }
}

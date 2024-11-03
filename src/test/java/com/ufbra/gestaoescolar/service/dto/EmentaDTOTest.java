package com.ufbra.gestaoescolar.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ufbra.gestaoescolar.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmentaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmentaDTO.class);
        EmentaDTO ementaDTO1 = new EmentaDTO();
        ementaDTO1.setId(1L);
        EmentaDTO ementaDTO2 = new EmentaDTO();
        assertThat(ementaDTO1).isNotEqualTo(ementaDTO2);
        ementaDTO2.setId(ementaDTO1.getId());
        assertThat(ementaDTO1).isEqualTo(ementaDTO2);
        ementaDTO2.setId(2L);
        assertThat(ementaDTO1).isNotEqualTo(ementaDTO2);
        ementaDTO1.setId(null);
        assertThat(ementaDTO1).isNotEqualTo(ementaDTO2);
    }
}

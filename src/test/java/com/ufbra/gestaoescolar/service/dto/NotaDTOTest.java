package com.ufbra.gestaoescolar.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ufbra.gestaoescolar.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NotaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotaDTO.class);
        NotaDTO notaDTO1 = new NotaDTO();
        notaDTO1.setId(1L);
        NotaDTO notaDTO2 = new NotaDTO();
        assertThat(notaDTO1).isNotEqualTo(notaDTO2);
        notaDTO2.setId(notaDTO1.getId());
        assertThat(notaDTO1).isEqualTo(notaDTO2);
        notaDTO2.setId(2L);
        assertThat(notaDTO1).isNotEqualTo(notaDTO2);
        notaDTO1.setId(null);
        assertThat(notaDTO1).isNotEqualTo(notaDTO2);
    }
}

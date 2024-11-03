package com.ufbra.gestaoescolar.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ufbra.gestaoescolar.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MatriculaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MatriculaDTO.class);
        MatriculaDTO matriculaDTO1 = new MatriculaDTO();
        matriculaDTO1.setId(1L);
        MatriculaDTO matriculaDTO2 = new MatriculaDTO();
        assertThat(matriculaDTO1).isNotEqualTo(matriculaDTO2);
        matriculaDTO2.setId(matriculaDTO1.getId());
        assertThat(matriculaDTO1).isEqualTo(matriculaDTO2);
        matriculaDTO2.setId(2L);
        assertThat(matriculaDTO1).isNotEqualTo(matriculaDTO2);
        matriculaDTO1.setId(null);
        assertThat(matriculaDTO1).isNotEqualTo(matriculaDTO2);
    }
}

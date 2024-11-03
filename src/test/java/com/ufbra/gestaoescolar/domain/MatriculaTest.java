package com.ufbra.gestaoescolar.domain;

import static com.ufbra.gestaoescolar.domain.AlunoTestSamples.*;
import static com.ufbra.gestaoescolar.domain.CursoTestSamples.*;
import static com.ufbra.gestaoescolar.domain.MatriculaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ufbra.gestaoescolar.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MatriculaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Matricula.class);
        Matricula matricula1 = getMatriculaSample1();
        Matricula matricula2 = new Matricula();
        assertThat(matricula1).isNotEqualTo(matricula2);

        matricula2.setId(matricula1.getId());
        assertThat(matricula1).isEqualTo(matricula2);

        matricula2 = getMatriculaSample2();
        assertThat(matricula1).isNotEqualTo(matricula2);
    }

    @Test
    void alunoTest() {
        Matricula matricula = getMatriculaRandomSampleGenerator();
        Aluno alunoBack = getAlunoRandomSampleGenerator();

        matricula.setAluno(alunoBack);
        assertThat(matricula.getAluno()).isEqualTo(alunoBack);

        matricula.aluno(null);
        assertThat(matricula.getAluno()).isNull();
    }

    @Test
    void cursoTest() {
        Matricula matricula = getMatriculaRandomSampleGenerator();
        Curso cursoBack = getCursoRandomSampleGenerator();

        matricula.setCurso(cursoBack);
        assertThat(matricula.getCurso()).isEqualTo(cursoBack);

        matricula.curso(null);
        assertThat(matricula.getCurso()).isNull();
    }
}

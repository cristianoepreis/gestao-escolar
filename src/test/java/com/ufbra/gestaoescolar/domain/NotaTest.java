package com.ufbra.gestaoescolar.domain;

import static com.ufbra.gestaoescolar.domain.AlunoTestSamples.*;
import static com.ufbra.gestaoescolar.domain.DisciplinaTestSamples.*;
import static com.ufbra.gestaoescolar.domain.NotaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ufbra.gestaoescolar.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NotaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Nota.class);
        Nota nota1 = getNotaSample1();
        Nota nota2 = new Nota();
        assertThat(nota1).isNotEqualTo(nota2);

        nota2.setId(nota1.getId());
        assertThat(nota1).isEqualTo(nota2);

        nota2 = getNotaSample2();
        assertThat(nota1).isNotEqualTo(nota2);
    }

    @Test
    void disciplinaTest() {
        Nota nota = getNotaRandomSampleGenerator();
        Disciplina disciplinaBack = getDisciplinaRandomSampleGenerator();

        nota.setDisciplina(disciplinaBack);
        assertThat(nota.getDisciplina()).isEqualTo(disciplinaBack);

        nota.disciplina(null);
        assertThat(nota.getDisciplina()).isNull();
    }

    @Test
    void alunoTest() {
        Nota nota = getNotaRandomSampleGenerator();
        Aluno alunoBack = getAlunoRandomSampleGenerator();

        nota.setAluno(alunoBack);
        assertThat(nota.getAluno()).isEqualTo(alunoBack);

        nota.aluno(null);
        assertThat(nota.getAluno()).isNull();
    }
}

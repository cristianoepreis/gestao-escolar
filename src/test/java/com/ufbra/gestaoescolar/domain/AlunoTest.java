package com.ufbra.gestaoescolar.domain;

import static com.ufbra.gestaoescolar.domain.AlunoTestSamples.*;
import static com.ufbra.gestaoescolar.domain.CursoTestSamples.*;
import static com.ufbra.gestaoescolar.domain.NotaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ufbra.gestaoescolar.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AlunoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Aluno.class);
        Aluno aluno1 = getAlunoSample1();
        Aluno aluno2 = new Aluno();
        assertThat(aluno1).isNotEqualTo(aluno2);

        aluno2.setId(aluno1.getId());
        assertThat(aluno1).isEqualTo(aluno2);

        aluno2 = getAlunoSample2();
        assertThat(aluno1).isNotEqualTo(aluno2);
    }

    @Test
    void notaTest() {
        Aluno aluno = getAlunoRandomSampleGenerator();
        Nota notaBack = getNotaRandomSampleGenerator();

        aluno.addNota(notaBack);
        assertThat(aluno.getNotas()).containsOnly(notaBack);
        assertThat(notaBack.getAluno()).isEqualTo(aluno);

        aluno.removeNota(notaBack);
        assertThat(aluno.getNotas()).doesNotContain(notaBack);
        assertThat(notaBack.getAluno()).isNull();

        aluno.notas(new HashSet<>(Set.of(notaBack)));
        assertThat(aluno.getNotas()).containsOnly(notaBack);
        assertThat(notaBack.getAluno()).isEqualTo(aluno);

        aluno.setNotas(new HashSet<>());
        assertThat(aluno.getNotas()).doesNotContain(notaBack);
        assertThat(notaBack.getAluno()).isNull();
    }

    @Test
    void cursoTest() {
        Aluno aluno = getAlunoRandomSampleGenerator();
        Curso cursoBack = getCursoRandomSampleGenerator();

        aluno.addCurso(cursoBack);
        assertThat(aluno.getCursos()).containsOnly(cursoBack);

        aluno.removeCurso(cursoBack);
        assertThat(aluno.getCursos()).doesNotContain(cursoBack);

        aluno.cursos(new HashSet<>(Set.of(cursoBack)));
        assertThat(aluno.getCursos()).containsOnly(cursoBack);

        aluno.setCursos(new HashSet<>());
        assertThat(aluno.getCursos()).doesNotContain(cursoBack);
    }
}

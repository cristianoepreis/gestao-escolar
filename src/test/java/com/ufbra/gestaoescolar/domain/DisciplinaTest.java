package com.ufbra.gestaoescolar.domain;

import static com.ufbra.gestaoescolar.domain.CursoTestSamples.*;
import static com.ufbra.gestaoescolar.domain.DisciplinaTestSamples.*;
import static com.ufbra.gestaoescolar.domain.EmentaTestSamples.*;
import static com.ufbra.gestaoescolar.domain.ProfessorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ufbra.gestaoescolar.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DisciplinaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Disciplina.class);
        Disciplina disciplina1 = getDisciplinaSample1();
        Disciplina disciplina2 = new Disciplina();
        assertThat(disciplina1).isNotEqualTo(disciplina2);

        disciplina2.setId(disciplina1.getId());
        assertThat(disciplina1).isEqualTo(disciplina2);

        disciplina2 = getDisciplinaSample2();
        assertThat(disciplina1).isNotEqualTo(disciplina2);
    }

    @Test
    void ementaTest() {
        Disciplina disciplina = getDisciplinaRandomSampleGenerator();
        Ementa ementaBack = getEmentaRandomSampleGenerator();

        disciplina.addEmenta(ementaBack);
        assertThat(disciplina.getEmentas()).containsOnly(ementaBack);
        assertThat(ementaBack.getDisciplina()).isEqualTo(disciplina);

        disciplina.removeEmenta(ementaBack);
        assertThat(disciplina.getEmentas()).doesNotContain(ementaBack);
        assertThat(ementaBack.getDisciplina()).isNull();

        disciplina.ementas(new HashSet<>(Set.of(ementaBack)));
        assertThat(disciplina.getEmentas()).containsOnly(ementaBack);
        assertThat(ementaBack.getDisciplina()).isEqualTo(disciplina);

        disciplina.setEmentas(new HashSet<>());
        assertThat(disciplina.getEmentas()).doesNotContain(ementaBack);
        assertThat(ementaBack.getDisciplina()).isNull();
    }

    @Test
    void cursoTest() {
        Disciplina disciplina = getDisciplinaRandomSampleGenerator();
        Curso cursoBack = getCursoRandomSampleGenerator();

        disciplina.setCurso(cursoBack);
        assertThat(disciplina.getCurso()).isEqualTo(cursoBack);

        disciplina.curso(null);
        assertThat(disciplina.getCurso()).isNull();
    }

    @Test
    void professorTest() {
        Disciplina disciplina = getDisciplinaRandomSampleGenerator();
        Professor professorBack = getProfessorRandomSampleGenerator();

        disciplina.addProfessor(professorBack);
        assertThat(disciplina.getProfessors()).containsOnly(professorBack);
        assertThat(professorBack.getDisciplinas()).containsOnly(disciplina);

        disciplina.removeProfessor(professorBack);
        assertThat(disciplina.getProfessors()).doesNotContain(professorBack);
        assertThat(professorBack.getDisciplinas()).doesNotContain(disciplina);

        disciplina.professors(new HashSet<>(Set.of(professorBack)));
        assertThat(disciplina.getProfessors()).containsOnly(professorBack);
        assertThat(professorBack.getDisciplinas()).containsOnly(disciplina);

        disciplina.setProfessors(new HashSet<>());
        assertThat(disciplina.getProfessors()).doesNotContain(professorBack);
        assertThat(professorBack.getDisciplinas()).doesNotContain(disciplina);
    }
}

package com.ufbra.gestaoescolar.domain;

import static com.ufbra.gestaoescolar.domain.CursoTestSamples.*;
import static com.ufbra.gestaoescolar.domain.DisciplinaTestSamples.*;
import static com.ufbra.gestaoescolar.domain.EmentaTestSamples.*;
import static com.ufbra.gestaoescolar.domain.ProfessorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ufbra.gestaoescolar.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmentaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ementa.class);
        Ementa ementa1 = getEmentaSample1();
        Ementa ementa2 = new Ementa();
        assertThat(ementa1).isNotEqualTo(ementa2);

        ementa2.setId(ementa1.getId());
        assertThat(ementa1).isEqualTo(ementa2);

        ementa2 = getEmentaSample2();
        assertThat(ementa1).isNotEqualTo(ementa2);
    }

    @Test
    void professorTest() {
        Ementa ementa = getEmentaRandomSampleGenerator();
        Professor professorBack = getProfessorRandomSampleGenerator();

        ementa.setProfessor(professorBack);
        assertThat(ementa.getProfessor()).isEqualTo(professorBack);

        ementa.professor(null);
        assertThat(ementa.getProfessor()).isNull();
    }

    @Test
    void cursoTest() {
        Ementa ementa = getEmentaRandomSampleGenerator();
        Curso cursoBack = getCursoRandomSampleGenerator();

        ementa.setCurso(cursoBack);
        assertThat(ementa.getCurso()).isEqualTo(cursoBack);

        ementa.curso(null);
        assertThat(ementa.getCurso()).isNull();
    }

    @Test
    void disciplinaTest() {
        Ementa ementa = getEmentaRandomSampleGenerator();
        Disciplina disciplinaBack = getDisciplinaRandomSampleGenerator();

        ementa.setDisciplina(disciplinaBack);
        assertThat(ementa.getDisciplina()).isEqualTo(disciplinaBack);

        ementa.disciplina(null);
        assertThat(ementa.getDisciplina()).isNull();
    }
}

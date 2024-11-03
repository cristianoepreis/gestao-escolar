package com.ufbra.gestaoescolar.domain;

import static com.ufbra.gestaoescolar.domain.DisciplinaTestSamples.*;
import static com.ufbra.gestaoescolar.domain.ProfessorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ufbra.gestaoescolar.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProfessorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Professor.class);
        Professor professor1 = getProfessorSample1();
        Professor professor2 = new Professor();
        assertThat(professor1).isNotEqualTo(professor2);

        professor2.setId(professor1.getId());
        assertThat(professor1).isEqualTo(professor2);

        professor2 = getProfessorSample2();
        assertThat(professor1).isNotEqualTo(professor2);
    }

    @Test
    void disciplinaTest() {
        Professor professor = getProfessorRandomSampleGenerator();
        Disciplina disciplinaBack = getDisciplinaRandomSampleGenerator();

        professor.addDisciplina(disciplinaBack);
        assertThat(professor.getDisciplinas()).containsOnly(disciplinaBack);

        professor.removeDisciplina(disciplinaBack);
        assertThat(professor.getDisciplinas()).doesNotContain(disciplinaBack);

        professor.disciplinas(new HashSet<>(Set.of(disciplinaBack)));
        assertThat(professor.getDisciplinas()).containsOnly(disciplinaBack);

        professor.setDisciplinas(new HashSet<>());
        assertThat(professor.getDisciplinas()).doesNotContain(disciplinaBack);
    }
}

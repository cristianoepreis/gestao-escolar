package com.ufbra.gestaoescolar.service.mapper;

import static com.ufbra.gestaoescolar.domain.ProfessorAsserts.*;
import static com.ufbra.gestaoescolar.domain.ProfessorTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProfessorMapperTest {

    private ProfessorMapper professorMapper;

    @BeforeEach
    void setUp() {
        professorMapper = new ProfessorMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProfessorSample1();
        var actual = professorMapper.toEntity(professorMapper.toDto(expected));
        assertProfessorAllPropertiesEquals(expected, actual);
    }
}

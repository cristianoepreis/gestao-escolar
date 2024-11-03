package com.ufbra.gestaoescolar.service.mapper;

import static com.ufbra.gestaoescolar.domain.CursoAsserts.*;
import static com.ufbra.gestaoescolar.domain.CursoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CursoMapperTest {

    private CursoMapper cursoMapper;

    @BeforeEach
    void setUp() {
        cursoMapper = new CursoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCursoSample1();
        var actual = cursoMapper.toEntity(cursoMapper.toDto(expected));
        assertCursoAllPropertiesEquals(expected, actual);
    }
}

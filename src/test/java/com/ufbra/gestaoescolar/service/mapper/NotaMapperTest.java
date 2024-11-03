package com.ufbra.gestaoescolar.service.mapper;

import static com.ufbra.gestaoescolar.domain.NotaAsserts.*;
import static com.ufbra.gestaoescolar.domain.NotaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NotaMapperTest {

    private NotaMapper notaMapper;

    @BeforeEach
    void setUp() {
        notaMapper = new NotaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNotaSample1();
        var actual = notaMapper.toEntity(notaMapper.toDto(expected));
        assertNotaAllPropertiesEquals(expected, actual);
    }
}

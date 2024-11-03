package com.ufbra.gestaoescolar.service.mapper;

import static com.ufbra.gestaoescolar.domain.EmentaAsserts.*;
import static com.ufbra.gestaoescolar.domain.EmentaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmentaMapperTest {

    private EmentaMapper ementaMapper;

    @BeforeEach
    void setUp() {
        ementaMapper = new EmentaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEmentaSample1();
        var actual = ementaMapper.toEntity(ementaMapper.toDto(expected));
        assertEmentaAllPropertiesEquals(expected, actual);
    }
}

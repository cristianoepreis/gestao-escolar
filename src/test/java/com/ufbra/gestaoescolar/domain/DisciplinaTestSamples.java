package com.ufbra.gestaoescolar.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DisciplinaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Disciplina getDisciplinaSample1() {
        return new Disciplina().id(1L).nome("nome1").codigo("codigo1").cargaHoraria(1);
    }

    public static Disciplina getDisciplinaSample2() {
        return new Disciplina().id(2L).nome("nome2").codigo("codigo2").cargaHoraria(2);
    }

    public static Disciplina getDisciplinaRandomSampleGenerator() {
        return new Disciplina()
            .id(longCount.incrementAndGet())
            .nome(UUID.randomUUID().toString())
            .codigo(UUID.randomUUID().toString())
            .cargaHoraria(intCount.incrementAndGet());
    }
}

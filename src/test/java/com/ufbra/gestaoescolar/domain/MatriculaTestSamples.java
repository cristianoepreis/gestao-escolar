package com.ufbra.gestaoescolar.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class MatriculaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Matricula getMatriculaSample1() {
        return new Matricula().id(1L);
    }

    public static Matricula getMatriculaSample2() {
        return new Matricula().id(2L);
    }

    public static Matricula getMatriculaRandomSampleGenerator() {
        return new Matricula().id(longCount.incrementAndGet());
    }
}

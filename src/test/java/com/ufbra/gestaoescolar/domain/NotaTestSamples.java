package com.ufbra.gestaoescolar.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class NotaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Nota getNotaSample1() {
        return new Nota().id(1L);
    }

    public static Nota getNotaSample2() {
        return new Nota().id(2L);
    }

    public static Nota getNotaRandomSampleGenerator() {
        return new Nota().id(longCount.incrementAndGet());
    }
}

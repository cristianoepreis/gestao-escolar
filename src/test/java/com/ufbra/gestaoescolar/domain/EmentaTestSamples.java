package com.ufbra.gestaoescolar.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class EmentaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Ementa getEmentaSample1() {
        return new Ementa().id(1L);
    }

    public static Ementa getEmentaSample2() {
        return new Ementa().id(2L);
    }

    public static Ementa getEmentaRandomSampleGenerator() {
        return new Ementa().id(longCount.incrementAndGet());
    }
}

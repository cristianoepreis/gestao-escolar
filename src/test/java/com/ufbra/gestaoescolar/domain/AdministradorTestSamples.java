package com.ufbra.gestaoescolar.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AdministradorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Administrador getAdministradorSample1() {
        return new Administrador().id(1L).nome("nome1").telefone("telefone1").email("email1");
    }

    public static Administrador getAdministradorSample2() {
        return new Administrador().id(2L).nome("nome2").telefone("telefone2").email("email2");
    }

    public static Administrador getAdministradorRandomSampleGenerator() {
        return new Administrador()
            .id(longCount.incrementAndGet())
            .nome(UUID.randomUUID().toString())
            .telefone(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString());
    }
}

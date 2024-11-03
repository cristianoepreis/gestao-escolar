package com.ufbra.gestaoescolar.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProfessorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Professor getProfessorSample1() {
        return new Professor().id(1L).nome("nome1").especializacao("especializacao1").telefone("telefone1").email("email1");
    }

    public static Professor getProfessorSample2() {
        return new Professor().id(2L).nome("nome2").especializacao("especializacao2").telefone("telefone2").email("email2");
    }

    public static Professor getProfessorRandomSampleGenerator() {
        return new Professor()
            .id(longCount.incrementAndGet())
            .nome(UUID.randomUUID().toString())
            .especializacao(UUID.randomUUID().toString())
            .telefone(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString());
    }
}

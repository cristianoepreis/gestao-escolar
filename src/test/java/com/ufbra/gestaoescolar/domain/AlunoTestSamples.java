package com.ufbra.gestaoescolar.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AlunoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Aluno getAlunoSample1() {
        return new Aluno().id(1L).nome("nome1").cpf("cpf1").endereco("endereco1").telefone("telefone1").email("email1");
    }

    public static Aluno getAlunoSample2() {
        return new Aluno().id(2L).nome("nome2").cpf("cpf2").endereco("endereco2").telefone("telefone2").email("email2");
    }

    public static Aluno getAlunoRandomSampleGenerator() {
        return new Aluno()
            .id(longCount.incrementAndGet())
            .nome(UUID.randomUUID().toString())
            .cpf(UUID.randomUUID().toString())
            .endereco(UUID.randomUUID().toString())
            .telefone(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString());
    }
}
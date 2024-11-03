package com.ufbra.gestaoescolar.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Nota.
 */
@Entity
@Table(name = "nota")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Nota implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "pontuacao", nullable = false)
    private Float pontuacao;

    @NotNull
    @Column(name = "data", nullable = false)
    private LocalDate data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "ementas", "curso", "professors" }, allowSetters = true)
    private Disciplina disciplina;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "notas", "cursos" }, allowSetters = true)
    private Aluno aluno;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Nota id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getPontuacao() {
        return this.pontuacao;
    }

    public Nota pontuacao(Float pontuacao) {
        this.setPontuacao(pontuacao);
        return this;
    }

    public void setPontuacao(Float pontuacao) {
        this.pontuacao = pontuacao;
    }

    public LocalDate getData() {
        return this.data;
    }

    public Nota data(LocalDate data) {
        this.setData(data);
        return this;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Disciplina getDisciplina() {
        return this.disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public Nota disciplina(Disciplina disciplina) {
        this.setDisciplina(disciplina);
        return this;
    }

    public Aluno getAluno() {
        return this.aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Nota aluno(Aluno aluno) {
        this.setAluno(aluno);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Nota)) {
            return false;
        }
        return getId() != null && getId().equals(((Nota) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Nota{" +
            "id=" + getId() +
            ", pontuacao=" + getPontuacao() +
            ", data='" + getData() + "'" +
            "}";
    }
}

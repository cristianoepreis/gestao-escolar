package com.ufbra.gestaoescolar.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ufbra.gestaoescolar.domain.enumeration.StatusMatricula;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Matricula.
 */
@Entity
@Table(name = "matricula")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Matricula implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "data_de_matricula", nullable = false)
    private LocalDate dataDeMatricula;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusMatricula status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "notas", "cursos" }, allowSetters = true)
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "disciplinas", "alunos" }, allowSetters = true)
    private Curso curso;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Matricula id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataDeMatricula() {
        return this.dataDeMatricula;
    }

    public Matricula dataDeMatricula(LocalDate dataDeMatricula) {
        this.setDataDeMatricula(dataDeMatricula);
        return this;
    }

    public void setDataDeMatricula(LocalDate dataDeMatricula) {
        this.dataDeMatricula = dataDeMatricula;
    }

    public StatusMatricula getStatus() {
        return this.status;
    }

    public Matricula status(StatusMatricula status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(StatusMatricula status) {
        this.status = status;
    }

    public Aluno getAluno() {
        return this.aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Matricula aluno(Aluno aluno) {
        this.setAluno(aluno);
        return this;
    }

    public Curso getCurso() {
        return this.curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Matricula curso(Curso curso) {
        this.setCurso(curso);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Matricula)) {
            return false;
        }
        return getId() != null && getId().equals(((Matricula) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Matricula{" +
            "id=" + getId() +
            ", dataDeMatricula='" + getDataDeMatricula() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}

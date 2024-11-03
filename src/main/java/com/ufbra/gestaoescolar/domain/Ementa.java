package com.ufbra.gestaoescolar.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A Ementa.
 */
@Entity
@Table(name = "ementa")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Ementa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "descricao")
    private String descricao;

    @Lob
    @Column(name = "bibliografia_basica")
    private String bibliografiaBasica;

    @Lob
    @Column(name = "bibliografia_complementar")
    private String bibliografiaComplementar;

    @Lob
    @Column(name = "pratica_laboratorial")
    private String praticaLaboratorial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "disciplinas" }, allowSetters = true)
    private Professor professor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "disciplinas", "alunos" }, allowSetters = true)
    private Curso curso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "ementas", "curso", "professors" }, allowSetters = true)
    private Disciplina disciplina;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ementa id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Ementa descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getBibliografiaBasica() {
        return this.bibliografiaBasica;
    }

    public Ementa bibliografiaBasica(String bibliografiaBasica) {
        this.setBibliografiaBasica(bibliografiaBasica);
        return this;
    }

    public void setBibliografiaBasica(String bibliografiaBasica) {
        this.bibliografiaBasica = bibliografiaBasica;
    }

    public String getBibliografiaComplementar() {
        return this.bibliografiaComplementar;
    }

    public Ementa bibliografiaComplementar(String bibliografiaComplementar) {
        this.setBibliografiaComplementar(bibliografiaComplementar);
        return this;
    }

    public void setBibliografiaComplementar(String bibliografiaComplementar) {
        this.bibliografiaComplementar = bibliografiaComplementar;
    }

    public String getPraticaLaboratorial() {
        return this.praticaLaboratorial;
    }

    public Ementa praticaLaboratorial(String praticaLaboratorial) {
        this.setPraticaLaboratorial(praticaLaboratorial);
        return this;
    }

    public void setPraticaLaboratorial(String praticaLaboratorial) {
        this.praticaLaboratorial = praticaLaboratorial;
    }

    public Professor getProfessor() {
        return this.professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public Ementa professor(Professor professor) {
        this.setProfessor(professor);
        return this;
    }

    public Curso getCurso() {
        return this.curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Ementa curso(Curso curso) {
        this.setCurso(curso);
        return this;
    }

    public Disciplina getDisciplina() {
        return this.disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public Ementa disciplina(Disciplina disciplina) {
        this.setDisciplina(disciplina);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ementa)) {
            return false;
        }
        return getId() != null && getId().equals(((Ementa) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ementa{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            ", bibliografiaBasica='" + getBibliografiaBasica() + "'" +
            ", bibliografiaComplementar='" + getBibliografiaComplementar() + "'" +
            ", praticaLaboratorial='" + getPraticaLaboratorial() + "'" +
            "}";
    }
}

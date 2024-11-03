package com.ufbra.gestaoescolar.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Disciplina.
 */
@Entity
@Table(name = "disciplina")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Disciplina implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotNull
    @Column(name = "codigo", nullable = false)
    private String codigo;

    @NotNull
    @Column(name = "carga_horaria", nullable = false)
    private Integer cargaHoraria;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "disciplina")
    @JsonIgnoreProperties(value = { "professor", "curso", "disciplina" }, allowSetters = true)
    private Set<Ementa> ementas = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "disciplinas", "alunos" }, allowSetters = true)
    private Curso curso;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "disciplinas")
    @JsonIgnoreProperties(value = { "disciplinas" }, allowSetters = true)
    private Set<Professor> professors = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Disciplina id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Disciplina nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public Disciplina codigo(String codigo) {
        this.setCodigo(codigo);
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Integer getCargaHoraria() {
        return this.cargaHoraria;
    }

    public Disciplina cargaHoraria(Integer cargaHoraria) {
        this.setCargaHoraria(cargaHoraria);
        return this;
    }

    public void setCargaHoraria(Integer cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public Set<Ementa> getEmentas() {
        return this.ementas;
    }

    public void setEmentas(Set<Ementa> ementas) {
        if (this.ementas != null) {
            this.ementas.forEach(i -> i.setDisciplina(null));
        }
        if (ementas != null) {
            ementas.forEach(i -> i.setDisciplina(this));
        }
        this.ementas = ementas;
    }

    public Disciplina ementas(Set<Ementa> ementas) {
        this.setEmentas(ementas);
        return this;
    }

    public Disciplina addEmenta(Ementa ementa) {
        this.ementas.add(ementa);
        ementa.setDisciplina(this);
        return this;
    }

    public Disciplina removeEmenta(Ementa ementa) {
        this.ementas.remove(ementa);
        ementa.setDisciplina(null);
        return this;
    }

    public Curso getCurso() {
        return this.curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Disciplina curso(Curso curso) {
        this.setCurso(curso);
        return this;
    }

    public Set<Professor> getProfessors() {
        return this.professors;
    }

    public void setProfessors(Set<Professor> professors) {
        if (this.professors != null) {
            this.professors.forEach(i -> i.removeDisciplina(this));
        }
        if (professors != null) {
            professors.forEach(i -> i.addDisciplina(this));
        }
        this.professors = professors;
    }

    public Disciplina professors(Set<Professor> professors) {
        this.setProfessors(professors);
        return this;
    }

    public Disciplina addProfessor(Professor professor) {
        this.professors.add(professor);
        professor.getDisciplinas().add(this);
        return this;
    }

    public Disciplina removeProfessor(Professor professor) {
        this.professors.remove(professor);
        professor.getDisciplinas().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Disciplina)) {
            return false;
        }
        return getId() != null && getId().equals(((Disciplina) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Disciplina{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", codigo='" + getCodigo() + "'" +
            ", cargaHoraria=" + getCargaHoraria() +
            "}";
    }
}

package com.ufbra.gestaoescolar.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Curso.
 */
@Entity
@Table(name = "curso")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Curso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    @NotNull
    @Column(name = "duracao", nullable = false)
    private Integer duracao;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "curso")
    @JsonIgnoreProperties(value = { "ementas", "curso", "professors" }, allowSetters = true)
    private Set<Disciplina> disciplinas = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "cursos")
    @JsonIgnoreProperties(value = { "notas", "cursos" }, allowSetters = true)
    private Set<Aluno> alunos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Curso id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Curso nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Curso descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getDuracao() {
        return this.duracao;
    }

    public Curso duracao(Integer duracao) {
        this.setDuracao(duracao);
        return this;
    }

    public void setDuracao(Integer duracao) {
        this.duracao = duracao;
    }

    public Set<Disciplina> getDisciplinas() {
        return this.disciplinas;
    }

    public void setDisciplinas(Set<Disciplina> disciplinas) {
        if (this.disciplinas != null) {
            this.disciplinas.forEach(i -> i.setCurso(null));
        }
        if (disciplinas != null) {
            disciplinas.forEach(i -> i.setCurso(this));
        }
        this.disciplinas = disciplinas;
    }

    public Curso disciplinas(Set<Disciplina> disciplinas) {
        this.setDisciplinas(disciplinas);
        return this;
    }

    public Curso addDisciplina(Disciplina disciplina) {
        this.disciplinas.add(disciplina);
        disciplina.setCurso(this);
        return this;
    }

    public Curso removeDisciplina(Disciplina disciplina) {
        this.disciplinas.remove(disciplina);
        disciplina.setCurso(null);
        return this;
    }

    public Set<Aluno> getAlunos() {
        return this.alunos;
    }

    public void setAlunos(Set<Aluno> alunos) {
        if (this.alunos != null) {
            this.alunos.forEach(i -> i.removeCurso(this));
        }
        if (alunos != null) {
            alunos.forEach(i -> i.addCurso(this));
        }
        this.alunos = alunos;
    }

    public Curso alunos(Set<Aluno> alunos) {
        this.setAlunos(alunos);
        return this;
    }

    public Curso addAluno(Aluno aluno) {
        this.alunos.add(aluno);
        aluno.getCursos().add(this);
        return this;
    }

    public Curso removeAluno(Aluno aluno) {
        this.alunos.remove(aluno);
        aluno.getCursos().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Curso)) {
            return false;
        }
        return getId() != null && getId().equals(((Curso) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Curso{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", duracao=" + getDuracao() +
            "}";
    }
}

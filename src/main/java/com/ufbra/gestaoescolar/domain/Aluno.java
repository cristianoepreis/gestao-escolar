package com.ufbra.gestaoescolar.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Aluno.
 */
@Entity
@Table(name = "aluno")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Aluno implements Serializable {

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
    @Column(name = "data_de_nascimento", nullable = false)
    private LocalDate dataDeNascimento;

    @NotNull
    @Column(name = "cpf", nullable = false)
    private String cpf;

    @Column(name = "endereco")
    private String endereco;

    @Column(name = "telefone")
    private String telefone;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "aluno")
    @JsonIgnoreProperties(value = { "disciplina", "aluno" }, allowSetters = true)
    private Set<Nota> notas = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "rel_aluno__curso", joinColumns = @JoinColumn(name = "aluno_id"), inverseJoinColumns = @JoinColumn(name = "curso_id"))
    @JsonIgnoreProperties(value = { "disciplinas", "alunos" }, allowSetters = true)
    private Set<Curso> cursos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Aluno id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Aluno nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataDeNascimento() {
        return this.dataDeNascimento;
    }

    public Aluno dataDeNascimento(LocalDate dataDeNascimento) {
        this.setDataDeNascimento(dataDeNascimento);
        return this;
    }

    public void setDataDeNascimento(LocalDate dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }

    public String getCpf() {
        return this.cpf;
    }

    public Aluno cpf(String cpf) {
        this.setCpf(cpf);
        return this;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEndereco() {
        return this.endereco;
    }

    public Aluno endereco(String endereco) {
        this.setEndereco(endereco);
        return this;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return this.telefone;
    }

    public Aluno telefone(String telefone) {
        this.setTelefone(telefone);
        return this;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return this.email;
    }

    public Aluno email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Nota> getNotas() {
        return this.notas;
    }

    public void setNotas(Set<Nota> notas) {
        if (this.notas != null) {
            this.notas.forEach(i -> i.setAluno(null));
        }
        if (notas != null) {
            notas.forEach(i -> i.setAluno(this));
        }
        this.notas = notas;
    }

    public Aluno notas(Set<Nota> notas) {
        this.setNotas(notas);
        return this;
    }

    public Aluno addNota(Nota nota) {
        this.notas.add(nota);
        nota.setAluno(this);
        return this;
    }

    public Aluno removeNota(Nota nota) {
        this.notas.remove(nota);
        nota.setAluno(null);
        return this;
    }

    public Set<Curso> getCursos() {
        return this.cursos;
    }

    public void setCursos(Set<Curso> cursos) {
        this.cursos = cursos;
    }

    public Aluno cursos(Set<Curso> cursos) {
        this.setCursos(cursos);
        return this;
    }

    public Aluno addCurso(Curso curso) {
        this.cursos.add(curso);
        return this;
    }

    public Aluno removeCurso(Curso curso) {
        this.cursos.remove(curso);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Aluno)) {
            return false;
        }
        return getId() != null && getId().equals(((Aluno) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Aluno{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", dataDeNascimento='" + getDataDeNascimento() + "'" +
            ", cpf='" + getCpf() + "'" +
            ", endereco='" + getEndereco() + "'" +
            ", telefone='" + getTelefone() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}

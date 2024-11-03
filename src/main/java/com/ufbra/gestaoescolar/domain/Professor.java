package com.ufbra.gestaoescolar.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Professor.
 */
@Entity
@Table(name = "professor")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Professor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "especializacao")
    private String especializacao;

    @Column(name = "telefone")
    private String telefone;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_professor__disciplina",
        joinColumns = @JoinColumn(name = "professor_id"),
        inverseJoinColumns = @JoinColumn(name = "disciplina_id")
    )
    @JsonIgnoreProperties(value = { "ementas", "curso", "professors" }, allowSetters = true)
    private Set<Disciplina> disciplinas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Professor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Professor nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEspecializacao() {
        return this.especializacao;
    }

    public Professor especializacao(String especializacao) {
        this.setEspecializacao(especializacao);
        return this;
    }

    public void setEspecializacao(String especializacao) {
        this.especializacao = especializacao;
    }

    public String getTelefone() {
        return this.telefone;
    }

    public Professor telefone(String telefone) {
        this.setTelefone(telefone);
        return this;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return this.email;
    }

    public Professor email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Disciplina> getDisciplinas() {
        return this.disciplinas;
    }

    public void setDisciplinas(Set<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }

    public Professor disciplinas(Set<Disciplina> disciplinas) {
        this.setDisciplinas(disciplinas);
        return this;
    }

    public Professor addDisciplina(Disciplina disciplina) {
        this.disciplinas.add(disciplina);
        return this;
    }

    public Professor removeDisciplina(Disciplina disciplina) {
        this.disciplinas.remove(disciplina);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Professor)) {
            return false;
        }
        return getId() != null && getId().equals(((Professor) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Professor{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", especializacao='" + getEspecializacao() + "'" +
            ", telefone='" + getTelefone() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}

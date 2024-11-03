package com.ufbra.gestaoescolar.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.ufbra.gestaoescolar.domain.Professor} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfessorDTO implements Serializable {

    private Long id;

    @NotNull
    private String nome;

    private String especializacao;

    private String telefone;

    @NotNull
    private String email;

    private Set<DisciplinaDTO> disciplinas = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEspecializacao() {
        return especializacao;
    }

    public void setEspecializacao(String especializacao) {
        this.especializacao = especializacao;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<DisciplinaDTO> getDisciplinas() {
        return disciplinas;
    }

    public void setDisciplinas(Set<DisciplinaDTO> disciplinas) {
        this.disciplinas = disciplinas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfessorDTO)) {
            return false;
        }

        ProfessorDTO professorDTO = (ProfessorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, professorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfessorDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", especializacao='" + getEspecializacao() + "'" +
            ", telefone='" + getTelefone() + "'" +
            ", email='" + getEmail() + "'" +
            ", disciplinas=" + getDisciplinas() +
            "}";
    }
}

package com.ufbra.gestaoescolar.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.ufbra.gestaoescolar.domain.Disciplina} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DisciplinaDTO implements Serializable {

    private Long id;

    @NotNull
    private String nome;

    @NotNull
    private String codigo;

    @NotNull
    private Integer cargaHoraria;

    private CursoDTO curso;

    private Set<ProfessorDTO> professors = new HashSet<>();

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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Integer getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(Integer cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public CursoDTO getCurso() {
        return curso;
    }

    public void setCurso(CursoDTO curso) {
        this.curso = curso;
    }

    public Set<ProfessorDTO> getProfessors() {
        return professors;
    }

    public void setProfessors(Set<ProfessorDTO> professors) {
        this.professors = professors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DisciplinaDTO)) {
            return false;
        }

        DisciplinaDTO disciplinaDTO = (DisciplinaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, disciplinaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DisciplinaDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", codigo='" + getCodigo() + "'" +
            ", cargaHoraria=" + getCargaHoraria() +
            ", curso=" + getCurso() +
            ", professors=" + getProfessors() +
            "}";
    }
}

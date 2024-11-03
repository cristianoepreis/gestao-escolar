package com.ufbra.gestaoescolar.service.dto;

import com.ufbra.gestaoescolar.domain.enumeration.StatusMatricula;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.ufbra.gestaoescolar.domain.Matricula} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MatriculaDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate dataDeMatricula;

    @NotNull
    private StatusMatricula status;

    private AlunoDTO aluno;

    private CursoDTO curso;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataDeMatricula() {
        return dataDeMatricula;
    }

    public void setDataDeMatricula(LocalDate dataDeMatricula) {
        this.dataDeMatricula = dataDeMatricula;
    }

    public StatusMatricula getStatus() {
        return status;
    }

    public void setStatus(StatusMatricula status) {
        this.status = status;
    }

    public AlunoDTO getAluno() {
        return aluno;
    }

    public void setAluno(AlunoDTO aluno) {
        this.aluno = aluno;
    }

    public CursoDTO getCurso() {
        return curso;
    }

    public void setCurso(CursoDTO curso) {
        this.curso = curso;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MatriculaDTO)) {
            return false;
        }

        MatriculaDTO matriculaDTO = (MatriculaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, matriculaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MatriculaDTO{" +
            "id=" + getId() +
            ", dataDeMatricula='" + getDataDeMatricula() + "'" +
            ", status='" + getStatus() + "'" +
            ", aluno=" + getAluno() +
            ", curso=" + getCurso() +
            "}";
    }
}

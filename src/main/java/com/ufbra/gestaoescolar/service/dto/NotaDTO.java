package com.ufbra.gestaoescolar.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.ufbra.gestaoescolar.domain.Nota} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotaDTO implements Serializable {

    private Long id;

    @NotNull
    private Float pontuacao;

    @NotNull
    private LocalDate data;

    private DisciplinaDTO disciplina;

    private AlunoDTO aluno;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(Float pontuacao) {
        this.pontuacao = pontuacao;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public DisciplinaDTO getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(DisciplinaDTO disciplina) {
        this.disciplina = disciplina;
    }

    public AlunoDTO getAluno() {
        return aluno;
    }

    public void setAluno(AlunoDTO aluno) {
        this.aluno = aluno;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotaDTO)) {
            return false;
        }

        NotaDTO notaDTO = (NotaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, notaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotaDTO{" +
            "id=" + getId() +
            ", pontuacao=" + getPontuacao() +
            ", data='" + getData() + "'" +
            ", disciplina=" + getDisciplina() +
            ", aluno=" + getAluno() +
            "}";
    }
}

package com.ufbra.gestaoescolar.service.dto;

import jakarta.persistence.Lob;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.ufbra.gestaoescolar.domain.Ementa} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmentaDTO implements Serializable {

    private Long id;

    @Lob
    private String descricao;

    @Lob
    private String bibliografiaBasica;

    @Lob
    private String bibliografiaComplementar;

    @Lob
    private String praticaLaboratorial;

    @Lob
    private LocalDate ultimaAlteracao;

    private ProfessorDTO professor;

    private CursoDTO curso;

    private DisciplinaDTO disciplina;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getBibliografiaBasica() {
        return bibliografiaBasica;
    }

    public void setBibliografiaBasica(String bibliografiaBasica) {
        this.bibliografiaBasica = bibliografiaBasica;
    }

    public String getBibliografiaComplementar() {
        return bibliografiaComplementar;
    }

    public void setBibliografiaComplementar(String bibliografiaComplementar) {
        this.bibliografiaComplementar = bibliografiaComplementar;
    }

    public String getPraticaLaboratorial() {
        return praticaLaboratorial;
    }

    public void setPraticaLaboratorial(String praticaLaboratorial) {
        this.praticaLaboratorial = praticaLaboratorial;
    }

    public ProfessorDTO getProfessor() {
        return professor;
    }

    public void setProfessor(ProfessorDTO professor) {
        this.professor = professor;
    }

    public CursoDTO getCurso() {
        return curso;
    }

    public void setCurso(CursoDTO curso) {
        this.curso = curso;
    }

    public DisciplinaDTO getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(DisciplinaDTO disciplina) {
        this.disciplina = disciplina;
    }

    public LocalDate getUltimaAlteracao() {
        return ultimaAlteracao;
    }

    public void setUltimaAlteracao(LocalDate ultimaAlteracao) {
        this.ultimaAlteracao = ultimaAlteracao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmentaDTO)) {
            return false;
        }

        EmentaDTO ementaDTO = (EmentaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ementaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmentaDTO{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            ", bibliografiaBasica='" + getBibliografiaBasica() + "'" +
            ", bibliografiaComplementar='" + getBibliografiaComplementar() + "'" +
            ", praticaLaboratorial='" + getPraticaLaboratorial() + "'" +
            ", professor=" + getProfessor() +
            ", curso=" + getCurso() +
            ", disciplina=" + getDisciplina() +
            "}";
    }
}

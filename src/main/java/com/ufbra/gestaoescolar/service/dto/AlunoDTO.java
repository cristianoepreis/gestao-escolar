package com.ufbra.gestaoescolar.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.ufbra.gestaoescolar.domain.Aluno} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlunoDTO implements Serializable {

    private Long id;

    @NotNull
    private String nome;

    @NotNull
    private LocalDate dataDeNascimento;

    @NotNull
    private String cpf;

    private String endereco;

    private String telefone;

    @NotNull
    private String email;

    private Set<CursoDTO> cursos = new HashSet<>();

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

    public LocalDate getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(LocalDate dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
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

    public Set<CursoDTO> getCursos() {
        return cursos;
    }

    public void setCursos(Set<CursoDTO> cursos) {
        this.cursos = cursos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlunoDTO)) {
            return false;
        }

        AlunoDTO alunoDTO = (AlunoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alunoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlunoDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", dataDeNascimento='" + getDataDeNascimento() + "'" +
            ", cpf='" + getCpf() + "'" +
            ", endereco='" + getEndereco() + "'" +
            ", telefone='" + getTelefone() + "'" +
            ", email='" + getEmail() + "'" +
            ", cursos=" + getCursos() +
            "}";
    }
}

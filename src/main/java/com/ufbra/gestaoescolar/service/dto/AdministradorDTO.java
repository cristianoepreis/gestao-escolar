package com.ufbra.gestaoescolar.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.ufbra.gestaoescolar.domain.Administrador} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AdministradorDTO implements Serializable {

    private Long id;

    @NotNull
    private String nome;

    private String telefone;

    @NotNull
    private String email;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdministradorDTO)) {
            return false;
        }

        AdministradorDTO administradorDTO = (AdministradorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, administradorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdministradorDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", telefone='" + getTelefone() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}

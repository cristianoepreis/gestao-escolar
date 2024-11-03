package com.ufbra.gestaoescolar.repository;

import com.ufbra.gestaoescolar.domain.Administrador;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Administrador entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Long> {}

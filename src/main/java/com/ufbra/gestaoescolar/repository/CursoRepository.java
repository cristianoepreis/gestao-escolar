package com.ufbra.gestaoescolar.repository;

import com.ufbra.gestaoescolar.domain.Curso;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Curso entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {}

package com.ufbra.gestaoescolar.repository;

import com.ufbra.gestaoescolar.domain.Disciplina;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Disciplina entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {}

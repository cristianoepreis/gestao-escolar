package com.ufbra.gestaoescolar.repository;

import com.ufbra.gestaoescolar.domain.Professor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ProfessorRepositoryWithBagRelationships {
    Optional<Professor> fetchBagRelationships(Optional<Professor> professor);

    List<Professor> fetchBagRelationships(List<Professor> professors);

    Page<Professor> fetchBagRelationships(Page<Professor> professors);
}

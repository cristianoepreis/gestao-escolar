package com.ufbra.gestaoescolar.repository;

import com.ufbra.gestaoescolar.domain.Matricula;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Matricula entity.
 */
@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
    default Optional<Matricula> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Matricula> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Matricula> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select matricula from Matricula matricula left join fetch matricula.aluno left join fetch matricula.curso",
        countQuery = "select count(matricula) from Matricula matricula"
    )
    Page<Matricula> findAllWithToOneRelationships(Pageable pageable);

    @Query("select matricula from Matricula matricula left join fetch matricula.aluno left join fetch matricula.curso")
    List<Matricula> findAllWithToOneRelationships();

    @Query(
        "select matricula from Matricula matricula left join fetch matricula.aluno left join fetch matricula.curso where matricula.id =:id"
    )
    Optional<Matricula> findOneWithToOneRelationships(@Param("id") Long id);
}

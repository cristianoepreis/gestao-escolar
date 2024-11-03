package com.ufbra.gestaoescolar.repository;

import com.ufbra.gestaoescolar.domain.Nota;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Nota entity.
 */
@Repository
public interface NotaRepository extends JpaRepository<Nota, Long> {
    default Optional<Nota> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Nota> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Nota> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select nota from Nota nota left join fetch nota.disciplina left join fetch nota.aluno",
        countQuery = "select count(nota) from Nota nota"
    )
    Page<Nota> findAllWithToOneRelationships(Pageable pageable);

    @Query("select nota from Nota nota left join fetch nota.disciplina left join fetch nota.aluno")
    List<Nota> findAllWithToOneRelationships();

    @Query("select nota from Nota nota left join fetch nota.disciplina left join fetch nota.aluno where nota.id =:id")
    Optional<Nota> findOneWithToOneRelationships(@Param("id") Long id);
}

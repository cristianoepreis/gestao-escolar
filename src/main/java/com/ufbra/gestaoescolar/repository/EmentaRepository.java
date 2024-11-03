package com.ufbra.gestaoescolar.repository;

import com.ufbra.gestaoescolar.domain.Ementa;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Ementa entity.
 */
@Repository
public interface EmentaRepository extends JpaRepository<Ementa, Long> {
    default Optional<Ementa> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Ementa> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Ementa> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select ementa from Ementa ementa left join fetch ementa.professor left join fetch ementa.curso left join fetch ementa.disciplina",
        countQuery = "select count(ementa) from Ementa ementa"
    )
    Page<Ementa> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select ementa from Ementa ementa left join fetch ementa.professor left join fetch ementa.curso left join fetch ementa.disciplina"
    )
    List<Ementa> findAllWithToOneRelationships();

    @Query(
        "select ementa from Ementa ementa left join fetch ementa.professor left join fetch ementa.curso left join fetch ementa.disciplina where ementa.id =:id"
    )
    Optional<Ementa> findOneWithToOneRelationships(@Param("id") Long id);
}

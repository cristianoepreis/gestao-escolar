package com.ufbra.gestaoescolar.repository;

import com.ufbra.gestaoescolar.domain.Professor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ProfessorRepositoryWithBagRelationshipsImpl implements ProfessorRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String PROFESSORS_PARAMETER = "professors";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Professor> fetchBagRelationships(Optional<Professor> professor) {
        return professor.map(this::fetchDisciplinas);
    }

    @Override
    public Page<Professor> fetchBagRelationships(Page<Professor> professors) {
        return new PageImpl<>(fetchBagRelationships(professors.getContent()), professors.getPageable(), professors.getTotalElements());
    }

    @Override
    public List<Professor> fetchBagRelationships(List<Professor> professors) {
        return Optional.of(professors).map(this::fetchDisciplinas).orElse(Collections.emptyList());
    }

    Professor fetchDisciplinas(Professor result) {
        return entityManager
            .createQuery(
                "select professor from Professor professor left join fetch professor.disciplinas where professor.id = :id",
                Professor.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Professor> fetchDisciplinas(List<Professor> professors) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, professors.size()).forEach(index -> order.put(professors.get(index).getId(), index));
        List<Professor> result = entityManager
            .createQuery(
                "select professor from Professor professor left join fetch professor.disciplinas where professor in :professors",
                Professor.class
            )
            .setParameter(PROFESSORS_PARAMETER, professors)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}

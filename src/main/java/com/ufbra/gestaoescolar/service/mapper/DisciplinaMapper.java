package com.ufbra.gestaoescolar.service.mapper;

import com.ufbra.gestaoescolar.domain.Curso;
import com.ufbra.gestaoescolar.domain.Disciplina;
import com.ufbra.gestaoescolar.domain.Professor;
import com.ufbra.gestaoescolar.service.dto.CursoDTO;
import com.ufbra.gestaoescolar.service.dto.DisciplinaDTO;
import com.ufbra.gestaoescolar.service.dto.ProfessorDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Disciplina} and its DTO {@link DisciplinaDTO}.
 */
@Mapper(componentModel = "spring")
public interface DisciplinaMapper extends EntityMapper<DisciplinaDTO, Disciplina> {
    @Mapping(target = "curso", source = "curso", qualifiedByName = "cursoId")
    @Mapping(target = "professors", source = "professors", qualifiedByName = "professorIdSet")
    DisciplinaDTO toDto(Disciplina s);

    @Mapping(target = "professors", ignore = true)
    @Mapping(target = "removeProfessor", ignore = true)
    Disciplina toEntity(DisciplinaDTO disciplinaDTO);

    @Named("cursoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    CursoDTO toDtoCursoId(Curso curso);

    @Named("professorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    ProfessorDTO toDtoProfessorId(Professor professor);

    @Named("professorIdSet")
    default Set<ProfessorDTO> toDtoProfessorIdSet(Set<Professor> professor) {
        return professor.stream().map(this::toDtoProfessorId).collect(Collectors.toSet());
    }
}

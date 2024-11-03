package com.ufbra.gestaoescolar.service.mapper;

import com.ufbra.gestaoescolar.domain.Disciplina;
import com.ufbra.gestaoescolar.domain.Professor;
import com.ufbra.gestaoescolar.service.dto.DisciplinaDTO;
import com.ufbra.gestaoescolar.service.dto.ProfessorDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Professor} and its DTO {@link ProfessorDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfessorMapper extends EntityMapper<ProfessorDTO, Professor> {
    @Mapping(target = "disciplinas", source = "disciplinas", qualifiedByName = "disciplinaNomeSet")
    ProfessorDTO toDto(Professor s);

    @Mapping(target = "removeDisciplina", ignore = true)
    Professor toEntity(ProfessorDTO professorDTO);

    @Named("disciplinaNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    DisciplinaDTO toDtoDisciplinaNome(Disciplina disciplina);

    @Named("disciplinaNomeSet")
    default Set<DisciplinaDTO> toDtoDisciplinaNomeSet(Set<Disciplina> disciplina) {
        return disciplina.stream().map(this::toDtoDisciplinaNome).collect(Collectors.toSet());
    }
}

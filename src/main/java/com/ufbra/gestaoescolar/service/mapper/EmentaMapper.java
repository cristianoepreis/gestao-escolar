package com.ufbra.gestaoescolar.service.mapper;

import com.ufbra.gestaoescolar.domain.Curso;
import com.ufbra.gestaoescolar.domain.Disciplina;
import com.ufbra.gestaoescolar.domain.Ementa;
import com.ufbra.gestaoescolar.domain.Professor;
import com.ufbra.gestaoescolar.service.dto.CursoDTO;
import com.ufbra.gestaoescolar.service.dto.DisciplinaDTO;
import com.ufbra.gestaoescolar.service.dto.EmentaDTO;
import com.ufbra.gestaoescolar.service.dto.ProfessorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ementa} and its DTO {@link EmentaDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmentaMapper extends EntityMapper<EmentaDTO, Ementa> {
    @Mapping(target = "professor", source = "professor", qualifiedByName = "professorNome")
    @Mapping(target = "curso", source = "curso", qualifiedByName = "cursoNome")
    @Mapping(target = "disciplina", source = "disciplina", qualifiedByName = "disciplinaNome")
    EmentaDTO toDto(Ementa s);

    @Named("professorNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    ProfessorDTO toDtoProfessorNome(Professor professor);

    @Named("cursoNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    CursoDTO toDtoCursoNome(Curso curso);

    @Named("disciplinaNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    DisciplinaDTO toDtoDisciplinaNome(Disciplina disciplina);
}

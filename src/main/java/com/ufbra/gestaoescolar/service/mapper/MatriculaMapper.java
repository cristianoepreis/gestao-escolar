package com.ufbra.gestaoescolar.service.mapper;

import com.ufbra.gestaoescolar.domain.Aluno;
import com.ufbra.gestaoescolar.domain.Curso;
import com.ufbra.gestaoescolar.domain.Matricula;
import com.ufbra.gestaoescolar.service.dto.AlunoDTO;
import com.ufbra.gestaoescolar.service.dto.CursoDTO;
import com.ufbra.gestaoescolar.service.dto.MatriculaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Matricula} and its DTO {@link MatriculaDTO}.
 */
@Mapper(componentModel = "spring")
public interface MatriculaMapper extends EntityMapper<MatriculaDTO, Matricula> {
    @Mapping(target = "aluno", source = "aluno", qualifiedByName = "alunoNome")
    @Mapping(target = "curso", source = "curso", qualifiedByName = "cursoNome")
    MatriculaDTO toDto(Matricula s);

    @Named("alunoNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    AlunoDTO toDtoAlunoNome(Aluno aluno);

    @Named("cursoNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    CursoDTO toDtoCursoNome(Curso curso);
}

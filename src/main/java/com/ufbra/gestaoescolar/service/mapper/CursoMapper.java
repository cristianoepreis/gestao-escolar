package com.ufbra.gestaoescolar.service.mapper;

import com.ufbra.gestaoescolar.domain.Aluno;
import com.ufbra.gestaoescolar.domain.Curso;
import com.ufbra.gestaoescolar.service.dto.AlunoDTO;
import com.ufbra.gestaoescolar.service.dto.CursoDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Curso} and its DTO {@link CursoDTO}.
 */
@Mapper(componentModel = "spring")
public interface CursoMapper extends EntityMapper<CursoDTO, Curso> {
    @Mapping(target = "alunos", source = "alunos", qualifiedByName = "alunoIdSet")
    CursoDTO toDto(Curso s);

    @Mapping(target = "alunos", ignore = true)
    @Mapping(target = "removeAluno", ignore = true)
    Curso toEntity(CursoDTO cursoDTO);

    @Named("alunoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    AlunoDTO toDtoAlunoId(Aluno aluno);

    @Named("alunoIdSet")
    default Set<AlunoDTO> toDtoAlunoIdSet(Set<Aluno> aluno) {
        return aluno.stream().map(this::toDtoAlunoId).collect(Collectors.toSet());
    }
}

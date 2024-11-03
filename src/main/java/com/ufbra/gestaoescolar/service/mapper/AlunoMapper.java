package com.ufbra.gestaoescolar.service.mapper;

import com.ufbra.gestaoescolar.domain.Aluno;
import com.ufbra.gestaoescolar.domain.Curso;
import com.ufbra.gestaoescolar.service.dto.AlunoDTO;
import com.ufbra.gestaoescolar.service.dto.CursoDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Aluno} and its DTO {@link AlunoDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlunoMapper extends EntityMapper<AlunoDTO, Aluno> {
    @Mapping(target = "cursos", source = "cursos", qualifiedByName = "cursoNomeSet")
    AlunoDTO toDto(Aluno s);

    @Mapping(target = "removeCurso", ignore = true)
    Aluno toEntity(AlunoDTO alunoDTO);

    @Named("cursoNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    CursoDTO toDtoCursoNome(Curso curso);

    @Named("cursoNomeSet")
    default Set<CursoDTO> toDtoCursoNomeSet(Set<Curso> curso) {
        return curso.stream().map(this::toDtoCursoNome).collect(Collectors.toSet());
    }
}

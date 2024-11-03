package com.ufbra.gestaoescolar.service.mapper;

import com.ufbra.gestaoescolar.domain.Aluno;
import com.ufbra.gestaoescolar.domain.Disciplina;
import com.ufbra.gestaoescolar.domain.Nota;
import com.ufbra.gestaoescolar.service.dto.AlunoDTO;
import com.ufbra.gestaoescolar.service.dto.DisciplinaDTO;
import com.ufbra.gestaoescolar.service.dto.NotaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Nota} and its DTO {@link NotaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotaMapper extends EntityMapper<NotaDTO, Nota> {
    @Mapping(target = "disciplina", source = "disciplina", qualifiedByName = "disciplinaNome")
    @Mapping(target = "aluno", source = "aluno", qualifiedByName = "alunoNome")
    NotaDTO toDto(Nota s);

    @Named("disciplinaNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    DisciplinaDTO toDtoDisciplinaNome(Disciplina disciplina);

    @Named("alunoNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    AlunoDTO toDtoAlunoNome(Aluno aluno);
}

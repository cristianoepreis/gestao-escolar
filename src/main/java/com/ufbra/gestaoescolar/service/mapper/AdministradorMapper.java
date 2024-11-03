package com.ufbra.gestaoescolar.service.mapper;

import com.ufbra.gestaoescolar.domain.Administrador;
import com.ufbra.gestaoescolar.service.dto.AdministradorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Administrador} and its DTO {@link AdministradorDTO}.
 */
@Mapper(componentModel = "spring")
public interface AdministradorMapper extends EntityMapper<AdministradorDTO, Administrador> {}

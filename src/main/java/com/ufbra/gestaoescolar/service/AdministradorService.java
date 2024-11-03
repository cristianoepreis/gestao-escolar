package com.ufbra.gestaoescolar.service;

import com.ufbra.gestaoescolar.domain.Administrador;
import com.ufbra.gestaoescolar.repository.AdministradorRepository;
import com.ufbra.gestaoescolar.service.dto.AdministradorDTO;
import com.ufbra.gestaoescolar.service.mapper.AdministradorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ufbra.gestaoescolar.domain.Administrador}.
 */
@Service
@Transactional
public class AdministradorService {

    private static final Logger LOG = LoggerFactory.getLogger(AdministradorService.class);

    private final AdministradorRepository administradorRepository;

    private final AdministradorMapper administradorMapper;

    public AdministradorService(AdministradorRepository administradorRepository, AdministradorMapper administradorMapper) {
        this.administradorRepository = administradorRepository;
        this.administradorMapper = administradorMapper;
    }

    /**
     * Save a administrador.
     *
     * @param administradorDTO the entity to save.
     * @return the persisted entity.
     */
    public AdministradorDTO save(AdministradorDTO administradorDTO) {
        LOG.debug("Request to save Administrador : {}", administradorDTO);
        Administrador administrador = administradorMapper.toEntity(administradorDTO);
        administrador = administradorRepository.save(administrador);
        return administradorMapper.toDto(administrador);
    }

    /**
     * Update a administrador.
     *
     * @param administradorDTO the entity to save.
     * @return the persisted entity.
     */
    public AdministradorDTO update(AdministradorDTO administradorDTO) {
        LOG.debug("Request to update Administrador : {}", administradorDTO);
        Administrador administrador = administradorMapper.toEntity(administradorDTO);
        administrador = administradorRepository.save(administrador);
        return administradorMapper.toDto(administrador);
    }

    /**
     * Partially update a administrador.
     *
     * @param administradorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AdministradorDTO> partialUpdate(AdministradorDTO administradorDTO) {
        LOG.debug("Request to partially update Administrador : {}", administradorDTO);

        return administradorRepository
            .findById(administradorDTO.getId())
            .map(existingAdministrador -> {
                administradorMapper.partialUpdate(existingAdministrador, administradorDTO);

                return existingAdministrador;
            })
            .map(administradorRepository::save)
            .map(administradorMapper::toDto);
    }

    /**
     * Get all the administradors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AdministradorDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Administradors");
        return administradorRepository.findAll(pageable).map(administradorMapper::toDto);
    }

    /**
     * Get one administrador by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AdministradorDTO> findOne(Long id) {
        LOG.debug("Request to get Administrador : {}", id);
        return administradorRepository.findById(id).map(administradorMapper::toDto);
    }

    /**
     * Delete the administrador by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Administrador : {}", id);
        administradorRepository.deleteById(id);
    }
}

package com.ufbra.gestaoescolar.service;

import com.ufbra.gestaoescolar.domain.Ementa;
import com.ufbra.gestaoescolar.repository.EmentaRepository;
import com.ufbra.gestaoescolar.service.dto.EmentaDTO;
import com.ufbra.gestaoescolar.service.mapper.EmentaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ufbra.gestaoescolar.domain.Ementa}.
 */
@Service
@Transactional
public class EmentaService {

    private static final Logger LOG = LoggerFactory.getLogger(EmentaService.class);

    private final EmentaRepository ementaRepository;

    private final EmentaMapper ementaMapper;

    public EmentaService(EmentaRepository ementaRepository, EmentaMapper ementaMapper) {
        this.ementaRepository = ementaRepository;
        this.ementaMapper = ementaMapper;
    }

    /**
     * Save a ementa.
     *
     * @param ementaDTO the entity to save.
     * @return the persisted entity.
     */
    public EmentaDTO save(EmentaDTO ementaDTO) {
        LOG.debug("Request to save Ementa : {}", ementaDTO);
        Ementa ementa = ementaMapper.toEntity(ementaDTO);
        ementa = ementaRepository.save(ementa);
        return ementaMapper.toDto(ementa);
    }

    /**
     * Update a ementa.
     *
     * @param ementaDTO the entity to save.
     * @return the persisted entity.
     */
    public EmentaDTO update(EmentaDTO ementaDTO) {
        LOG.debug("Request to update Ementa : {}", ementaDTO);
        Ementa ementa = ementaMapper.toEntity(ementaDTO);
        ementa = ementaRepository.save(ementa);
        return ementaMapper.toDto(ementa);
    }

    /**
     * Partially update a ementa.
     *
     * @param ementaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EmentaDTO> partialUpdate(EmentaDTO ementaDTO) {
        LOG.debug("Request to partially update Ementa : {}", ementaDTO);

        return ementaRepository
            .findById(ementaDTO.getId())
            .map(existingEmenta -> {
                ementaMapper.partialUpdate(existingEmenta, ementaDTO);

                return existingEmenta;
            })
            .map(ementaRepository::save)
            .map(ementaMapper::toDto);
    }

    /**
     * Get all the ementas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EmentaDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Ementas");
        return ementaRepository.findAll(pageable).map(ementaMapper::toDto);
    }

    /**
     * Get all the ementas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<EmentaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return ementaRepository.findAllWithEagerRelationships(pageable).map(ementaMapper::toDto);
    }

    /**
     * Get one ementa by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EmentaDTO> findOne(Long id) {
        LOG.debug("Request to get Ementa : {}", id);
        return ementaRepository.findOneWithEagerRelationships(id).map(ementaMapper::toDto);
    }

    /**
     * Delete the ementa by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Ementa : {}", id);
        ementaRepository.deleteById(id);
    }
}

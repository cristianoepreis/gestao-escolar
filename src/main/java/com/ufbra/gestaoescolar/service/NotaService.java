package com.ufbra.gestaoescolar.service;

import com.ufbra.gestaoescolar.domain.Nota;
import com.ufbra.gestaoescolar.repository.NotaRepository;
import com.ufbra.gestaoescolar.service.dto.NotaDTO;
import com.ufbra.gestaoescolar.service.mapper.NotaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ufbra.gestaoescolar.domain.Nota}.
 */
@Service
@Transactional
public class NotaService {

    private static final Logger LOG = LoggerFactory.getLogger(NotaService.class);

    private final NotaRepository notaRepository;

    private final NotaMapper notaMapper;

    public NotaService(NotaRepository notaRepository, NotaMapper notaMapper) {
        this.notaRepository = notaRepository;
        this.notaMapper = notaMapper;
    }

    /**
     * Save a nota.
     *
     * @param notaDTO the entity to save.
     * @return the persisted entity.
     */
    public NotaDTO save(NotaDTO notaDTO) {
        LOG.debug("Request to save Nota : {}", notaDTO);
        Nota nota = notaMapper.toEntity(notaDTO);
        nota = notaRepository.save(nota);
        return notaMapper.toDto(nota);
    }

    /**
     * Update a nota.
     *
     * @param notaDTO the entity to save.
     * @return the persisted entity.
     */
    public NotaDTO update(NotaDTO notaDTO) {
        LOG.debug("Request to update Nota : {}", notaDTO);
        Nota nota = notaMapper.toEntity(notaDTO);
        nota = notaRepository.save(nota);
        return notaMapper.toDto(nota);
    }

    /**
     * Partially update a nota.
     *
     * @param notaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NotaDTO> partialUpdate(NotaDTO notaDTO) {
        LOG.debug("Request to partially update Nota : {}", notaDTO);

        return notaRepository
            .findById(notaDTO.getId())
            .map(existingNota -> {
                notaMapper.partialUpdate(existingNota, notaDTO);

                return existingNota;
            })
            .map(notaRepository::save)
            .map(notaMapper::toDto);
    }

    /**
     * Get all the notas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<NotaDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Notas");
        return notaRepository.findAll(pageable).map(notaMapper::toDto);
    }

    /**
     * Get all the notas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NotaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return notaRepository.findAllWithEagerRelationships(pageable).map(notaMapper::toDto);
    }

    /**
     * Get one nota by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NotaDTO> findOne(Long id) {
        LOG.debug("Request to get Nota : {}", id);
        return notaRepository.findOneWithEagerRelationships(id).map(notaMapper::toDto);
    }

    /**
     * Delete the nota by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Nota : {}", id);
        notaRepository.deleteById(id);
    }
}

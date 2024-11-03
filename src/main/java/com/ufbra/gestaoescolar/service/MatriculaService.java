package com.ufbra.gestaoescolar.service;

import com.ufbra.gestaoescolar.domain.Matricula;
import com.ufbra.gestaoescolar.repository.MatriculaRepository;
import com.ufbra.gestaoescolar.service.dto.MatriculaDTO;
import com.ufbra.gestaoescolar.service.mapper.MatriculaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ufbra.gestaoescolar.domain.Matricula}.
 */
@Service
@Transactional
public class MatriculaService {

    private static final Logger LOG = LoggerFactory.getLogger(MatriculaService.class);

    private final MatriculaRepository matriculaRepository;

    private final MatriculaMapper matriculaMapper;

    public MatriculaService(MatriculaRepository matriculaRepository, MatriculaMapper matriculaMapper) {
        this.matriculaRepository = matriculaRepository;
        this.matriculaMapper = matriculaMapper;
    }

    /**
     * Save a matricula.
     *
     * @param matriculaDTO the entity to save.
     * @return the persisted entity.
     */
    public MatriculaDTO save(MatriculaDTO matriculaDTO) {
        LOG.debug("Request to save Matricula : {}", matriculaDTO);
        Matricula matricula = matriculaMapper.toEntity(matriculaDTO);
        matricula = matriculaRepository.save(matricula);
        return matriculaMapper.toDto(matricula);
    }

    /**
     * Update a matricula.
     *
     * @param matriculaDTO the entity to save.
     * @return the persisted entity.
     */
    public MatriculaDTO update(MatriculaDTO matriculaDTO) {
        LOG.debug("Request to update Matricula : {}", matriculaDTO);
        Matricula matricula = matriculaMapper.toEntity(matriculaDTO);
        matricula = matriculaRepository.save(matricula);
        return matriculaMapper.toDto(matricula);
    }

    /**
     * Partially update a matricula.
     *
     * @param matriculaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MatriculaDTO> partialUpdate(MatriculaDTO matriculaDTO) {
        LOG.debug("Request to partially update Matricula : {}", matriculaDTO);

        return matriculaRepository
            .findById(matriculaDTO.getId())
            .map(existingMatricula -> {
                matriculaMapper.partialUpdate(existingMatricula, matriculaDTO);

                return existingMatricula;
            })
            .map(matriculaRepository::save)
            .map(matriculaMapper::toDto);
    }

    /**
     * Get all the matriculas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MatriculaDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Matriculas");
        return matriculaRepository.findAll(pageable).map(matriculaMapper::toDto);
    }

    /**
     * Get all the matriculas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<MatriculaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return matriculaRepository.findAllWithEagerRelationships(pageable).map(matriculaMapper::toDto);
    }

    /**
     * Get one matricula by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MatriculaDTO> findOne(Long id) {
        LOG.debug("Request to get Matricula : {}", id);
        return matriculaRepository.findOneWithEagerRelationships(id).map(matriculaMapper::toDto);
    }

    /**
     * Delete the matricula by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Matricula : {}", id);
        matriculaRepository.deleteById(id);
    }
}

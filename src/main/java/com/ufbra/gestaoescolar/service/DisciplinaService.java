package com.ufbra.gestaoescolar.service;

import com.ufbra.gestaoescolar.domain.Disciplina;
import com.ufbra.gestaoescolar.repository.DisciplinaRepository;
import com.ufbra.gestaoescolar.service.dto.DisciplinaDTO;
import com.ufbra.gestaoescolar.service.mapper.DisciplinaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ufbra.gestaoescolar.domain.Disciplina}.
 */
@Service
@Transactional
public class DisciplinaService {

    private static final Logger LOG = LoggerFactory.getLogger(DisciplinaService.class);

    private final DisciplinaRepository disciplinaRepository;

    private final DisciplinaMapper disciplinaMapper;

    public DisciplinaService(DisciplinaRepository disciplinaRepository, DisciplinaMapper disciplinaMapper) {
        this.disciplinaRepository = disciplinaRepository;
        this.disciplinaMapper = disciplinaMapper;
    }

    /**
     * Save a disciplina.
     *
     * @param disciplinaDTO the entity to save.
     * @return the persisted entity.
     */
    public DisciplinaDTO save(DisciplinaDTO disciplinaDTO) {
        LOG.debug("Request to save Disciplina : {}", disciplinaDTO);
        Disciplina disciplina = disciplinaMapper.toEntity(disciplinaDTO);
        disciplina = disciplinaRepository.save(disciplina);
        return disciplinaMapper.toDto(disciplina);
    }

    /**
     * Update a disciplina.
     *
     * @param disciplinaDTO the entity to save.
     * @return the persisted entity.
     */
    public DisciplinaDTO update(DisciplinaDTO disciplinaDTO) {
        LOG.debug("Request to update Disciplina : {}", disciplinaDTO);
        Disciplina disciplina = disciplinaMapper.toEntity(disciplinaDTO);
        disciplina = disciplinaRepository.save(disciplina);
        return disciplinaMapper.toDto(disciplina);
    }

    /**
     * Partially update a disciplina.
     *
     * @param disciplinaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DisciplinaDTO> partialUpdate(DisciplinaDTO disciplinaDTO) {
        LOG.debug("Request to partially update Disciplina : {}", disciplinaDTO);

        return disciplinaRepository
            .findById(disciplinaDTO.getId())
            .map(existingDisciplina -> {
                disciplinaMapper.partialUpdate(existingDisciplina, disciplinaDTO);

                return existingDisciplina;
            })
            .map(disciplinaRepository::save)
            .map(disciplinaMapper::toDto);
    }

    /**
     * Get all the disciplinas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DisciplinaDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Disciplinas");
        return disciplinaRepository.findAll(pageable).map(disciplinaMapper::toDto);
    }

    /**
     * Get one disciplina by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DisciplinaDTO> findOne(Long id) {
        LOG.debug("Request to get Disciplina : {}", id);
        return disciplinaRepository.findById(id).map(disciplinaMapper::toDto);
    }

    /**
     * Delete the disciplina by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Disciplina : {}", id);
        disciplinaRepository.deleteById(id);
    }
}

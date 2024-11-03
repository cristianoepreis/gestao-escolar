package com.ufbra.gestaoescolar.web.rest;

import com.ufbra.gestaoescolar.repository.EmentaRepository;
import com.ufbra.gestaoescolar.service.EmentaService;
import com.ufbra.gestaoescolar.service.dto.EmentaDTO;
import com.ufbra.gestaoescolar.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ufbra.gestaoescolar.domain.Ementa}.
 */
@RestController
@RequestMapping("/api/ementas")
public class EmentaResource {

    private static final Logger LOG = LoggerFactory.getLogger(EmentaResource.class);

    private static final String ENTITY_NAME = "ementa";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmentaService ementaService;

    private final EmentaRepository ementaRepository;

    public EmentaResource(EmentaService ementaService, EmentaRepository ementaRepository) {
        this.ementaService = ementaService;
        this.ementaRepository = ementaRepository;
    }

    /**
     * {@code POST  /ementas} : Create a new ementa.
     *
     * @param ementaDTO the ementaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ementaDTO, or with status {@code 400 (Bad Request)} if the ementa has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmentaDTO> createEmenta(@RequestBody EmentaDTO ementaDTO) throws URISyntaxException {
        LOG.debug("REST request to save Ementa : {}", ementaDTO);
        if (ementaDTO.getId() != null) {
            throw new BadRequestAlertException("A new ementa cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ementaDTO = ementaService.save(ementaDTO);
        return ResponseEntity.created(new URI("/api/ementas/" + ementaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ementaDTO.getId().toString()))
            .body(ementaDTO);
    }

    /**
     * {@code PUT  /ementas/:id} : Updates an existing ementa.
     *
     * @param id the id of the ementaDTO to save.
     * @param ementaDTO the ementaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ementaDTO,
     * or with status {@code 400 (Bad Request)} if the ementaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ementaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmentaDTO> updateEmenta(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EmentaDTO ementaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Ementa : {}, {}", id, ementaDTO);
        if (ementaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ementaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ementaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ementaDTO = ementaService.update(ementaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ementaDTO.getId().toString()))
            .body(ementaDTO);
    }

    /**
     * {@code PATCH  /ementas/:id} : Partial updates given fields of an existing ementa, field will ignore if it is null
     *
     * @param id the id of the ementaDTO to save.
     * @param ementaDTO the ementaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ementaDTO,
     * or with status {@code 400 (Bad Request)} if the ementaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ementaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ementaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmentaDTO> partialUpdateEmenta(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EmentaDTO ementaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Ementa partially : {}, {}", id, ementaDTO);
        if (ementaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ementaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ementaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmentaDTO> result = ementaService.partialUpdate(ementaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ementaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ementas} : get all the ementas.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ementas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EmentaDTO>> getAllEmentas(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Ementas");
        Page<EmentaDTO> page;
        if (eagerload) {
            page = ementaService.findAllWithEagerRelationships(pageable);
        } else {
            page = ementaService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ementas/:id} : get the "id" ementa.
     *
     * @param id the id of the ementaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ementaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmentaDTO> getEmenta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Ementa : {}", id);
        Optional<EmentaDTO> ementaDTO = ementaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ementaDTO);
    }

    /**
     * {@code DELETE  /ementas/:id} : delete the "id" ementa.
     *
     * @param id the id of the ementaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmenta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Ementa : {}", id);
        ementaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

package com.ufbra.gestaoescolar.web.rest;

import com.ufbra.gestaoescolar.repository.NotaRepository;
import com.ufbra.gestaoescolar.service.NotaService;
import com.ufbra.gestaoescolar.service.dto.NotaDTO;
import com.ufbra.gestaoescolar.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.ufbra.gestaoescolar.domain.Nota}.
 */
@RestController
@RequestMapping("/api/notas")
public class NotaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NotaResource.class);

    private static final String ENTITY_NAME = "nota";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NotaService notaService;

    private final NotaRepository notaRepository;

    public NotaResource(NotaService notaService, NotaRepository notaRepository) {
        this.notaService = notaService;
        this.notaRepository = notaRepository;
    }

    /**
     * {@code POST  /notas} : Create a new nota.
     *
     * @param notaDTO the notaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new notaDTO, or with status {@code 400 (Bad Request)} if the nota has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NotaDTO> createNota(@Valid @RequestBody NotaDTO notaDTO) throws URISyntaxException {
        LOG.debug("REST request to save Nota : {}", notaDTO);
        if (notaDTO.getId() != null) {
            throw new BadRequestAlertException("A new nota cannot already have an ID", ENTITY_NAME, "idexists");
        }
        notaDTO = notaService.save(notaDTO);
        return ResponseEntity.created(new URI("/api/notas/" + notaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, notaDTO.getId().toString()))
            .body(notaDTO);
    }

    /**
     * {@code PUT  /notas/:id} : Updates an existing nota.
     *
     * @param id the id of the notaDTO to save.
     * @param notaDTO the notaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notaDTO,
     * or with status {@code 400 (Bad Request)} if the notaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the notaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NotaDTO> updateNota(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NotaDTO notaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Nota : {}, {}", id, notaDTO);
        if (notaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        notaDTO = notaService.update(notaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notaDTO.getId().toString()))
            .body(notaDTO);
    }

    /**
     * {@code PATCH  /notas/:id} : Partial updates given fields of an existing nota, field will ignore if it is null
     *
     * @param id the id of the notaDTO to save.
     * @param notaDTO the notaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notaDTO,
     * or with status {@code 400 (Bad Request)} if the notaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the notaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the notaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NotaDTO> partialUpdateNota(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NotaDTO notaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Nota partially : {}, {}", id, notaDTO);
        if (notaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NotaDTO> result = notaService.partialUpdate(notaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /notas} : get all the notas.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NotaDTO>> getAllNotas(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Notas");
        Page<NotaDTO> page;
        if (eagerload) {
            page = notaService.findAllWithEagerRelationships(pageable);
        } else {
            page = notaService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /notas/:id} : get the "id" nota.
     *
     * @param id the id of the notaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the notaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NotaDTO> getNota(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Nota : {}", id);
        Optional<NotaDTO> notaDTO = notaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(notaDTO);
    }

    /**
     * {@code DELETE  /notas/:id} : delete the "id" nota.
     *
     * @param id the id of the notaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNota(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Nota : {}", id);
        notaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

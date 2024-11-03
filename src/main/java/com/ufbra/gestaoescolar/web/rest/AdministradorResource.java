package com.ufbra.gestaoescolar.web.rest;

import com.ufbra.gestaoescolar.repository.AdministradorRepository;
import com.ufbra.gestaoescolar.service.AdministradorService;
import com.ufbra.gestaoescolar.service.dto.AdministradorDTO;
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
 * REST controller for managing {@link com.ufbra.gestaoescolar.domain.Administrador}.
 */
@RestController
@RequestMapping("/api/administradors")
public class AdministradorResource {

    private static final Logger LOG = LoggerFactory.getLogger(AdministradorResource.class);

    private static final String ENTITY_NAME = "administrador";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AdministradorService administradorService;

    private final AdministradorRepository administradorRepository;

    public AdministradorResource(AdministradorService administradorService, AdministradorRepository administradorRepository) {
        this.administradorService = administradorService;
        this.administradorRepository = administradorRepository;
    }

    /**
     * {@code POST  /administradors} : Create a new administrador.
     *
     * @param administradorDTO the administradorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new administradorDTO, or with status {@code 400 (Bad Request)} if the administrador has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AdministradorDTO> createAdministrador(@Valid @RequestBody AdministradorDTO administradorDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save Administrador : {}", administradorDTO);
        if (administradorDTO.getId() != null) {
            throw new BadRequestAlertException("A new administrador cannot already have an ID", ENTITY_NAME, "idexists");
        }
        administradorDTO = administradorService.save(administradorDTO);
        return ResponseEntity.created(new URI("/api/administradors/" + administradorDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, administradorDTO.getId().toString()))
            .body(administradorDTO);
    }

    /**
     * {@code PUT  /administradors/:id} : Updates an existing administrador.
     *
     * @param id the id of the administradorDTO to save.
     * @param administradorDTO the administradorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated administradorDTO,
     * or with status {@code 400 (Bad Request)} if the administradorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the administradorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AdministradorDTO> updateAdministrador(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AdministradorDTO administradorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Administrador : {}, {}", id, administradorDTO);
        if (administradorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, administradorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!administradorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        administradorDTO = administradorService.update(administradorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, administradorDTO.getId().toString()))
            .body(administradorDTO);
    }

    /**
     * {@code PATCH  /administradors/:id} : Partial updates given fields of an existing administrador, field will ignore if it is null
     *
     * @param id the id of the administradorDTO to save.
     * @param administradorDTO the administradorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated administradorDTO,
     * or with status {@code 400 (Bad Request)} if the administradorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the administradorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the administradorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AdministradorDTO> partialUpdateAdministrador(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AdministradorDTO administradorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Administrador partially : {}, {}", id, administradorDTO);
        if (administradorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, administradorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!administradorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AdministradorDTO> result = administradorService.partialUpdate(administradorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, administradorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /administradors} : get all the administradors.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of administradors in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AdministradorDTO>> getAllAdministradors(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Administradors");
        Page<AdministradorDTO> page = administradorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /administradors/:id} : get the "id" administrador.
     *
     * @param id the id of the administradorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the administradorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdministradorDTO> getAdministrador(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Administrador : {}", id);
        Optional<AdministradorDTO> administradorDTO = administradorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(administradorDTO);
    }

    /**
     * {@code DELETE  /administradors/:id} : delete the "id" administrador.
     *
     * @param id the id of the administradorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdministrador(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Administrador : {}", id);
        administradorService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

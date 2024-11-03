package com.ufbra.gestaoescolar.web.rest;

import static com.ufbra.gestaoescolar.domain.AdministradorAsserts.*;
import static com.ufbra.gestaoescolar.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufbra.gestaoescolar.IntegrationTest;
import com.ufbra.gestaoescolar.domain.Administrador;
import com.ufbra.gestaoescolar.repository.AdministradorRepository;
import com.ufbra.gestaoescolar.service.dto.AdministradorDTO;
import com.ufbra.gestaoescolar.service.mapper.AdministradorMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AdministradorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AdministradorResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/administradors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private AdministradorMapper administradorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdministradorMockMvc;

    private Administrador administrador;

    private Administrador insertedAdministrador;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Administrador createEntity() {
        return new Administrador().nome(DEFAULT_NOME).telefone(DEFAULT_TELEFONE).email(DEFAULT_EMAIL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Administrador createUpdatedEntity() {
        return new Administrador().nome(UPDATED_NOME).telefone(UPDATED_TELEFONE).email(UPDATED_EMAIL);
    }

    @BeforeEach
    public void initTest() {
        administrador = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAdministrador != null) {
            administradorRepository.delete(insertedAdministrador);
            insertedAdministrador = null;
        }
    }

    @Test
    @Transactional
    void createAdministrador() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Administrador
        AdministradorDTO administradorDTO = administradorMapper.toDto(administrador);
        var returnedAdministradorDTO = om.readValue(
            restAdministradorMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(administradorDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AdministradorDTO.class
        );

        // Validate the Administrador in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAdministrador = administradorMapper.toEntity(returnedAdministradorDTO);
        assertAdministradorUpdatableFieldsEquals(returnedAdministrador, getPersistedAdministrador(returnedAdministrador));

        insertedAdministrador = returnedAdministrador;
    }

    @Test
    @Transactional
    void createAdministradorWithExistingId() throws Exception {
        // Create the Administrador with an existing ID
        administrador.setId(1L);
        AdministradorDTO administradorDTO = administradorMapper.toDto(administrador);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdministradorMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(administradorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrador in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        administrador.setNome(null);

        // Create the Administrador, which fails.
        AdministradorDTO administradorDTO = administradorMapper.toDto(administrador);

        restAdministradorMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(administradorDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        administrador.setEmail(null);

        // Create the Administrador, which fails.
        AdministradorDTO administradorDTO = administradorMapper.toDto(administrador);

        restAdministradorMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(administradorDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAdministradors() throws Exception {
        // Initialize the database
        insertedAdministrador = administradorRepository.saveAndFlush(administrador);

        // Get all the administradorList
        restAdministradorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(administrador.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    void getAdministrador() throws Exception {
        // Initialize the database
        insertedAdministrador = administradorRepository.saveAndFlush(administrador);

        // Get the administrador
        restAdministradorMockMvc
            .perform(get(ENTITY_API_URL_ID, administrador.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(administrador.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.telefone").value(DEFAULT_TELEFONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getNonExistingAdministrador() throws Exception {
        // Get the administrador
        restAdministradorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAdministrador() throws Exception {
        // Initialize the database
        insertedAdministrador = administradorRepository.saveAndFlush(administrador);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the administrador
        Administrador updatedAdministrador = administradorRepository.findById(administrador.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAdministrador are not directly saved in db
        em.detach(updatedAdministrador);
        updatedAdministrador.nome(UPDATED_NOME).telefone(UPDATED_TELEFONE).email(UPDATED_EMAIL);
        AdministradorDTO administradorDTO = administradorMapper.toDto(updatedAdministrador);

        restAdministradorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, administradorDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(administradorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Administrador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAdministradorToMatchAllProperties(updatedAdministrador);
    }

    @Test
    @Transactional
    void putNonExistingAdministrador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        administrador.setId(longCount.incrementAndGet());

        // Create the Administrador
        AdministradorDTO administradorDTO = administradorMapper.toDto(administrador);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdministradorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, administradorDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(administradorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAdministrador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        administrador.setId(longCount.incrementAndGet());

        // Create the Administrador
        AdministradorDTO administradorDTO = administradorMapper.toDto(administrador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministradorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(administradorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAdministrador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        administrador.setId(longCount.incrementAndGet());

        // Create the Administrador
        AdministradorDTO administradorDTO = administradorMapper.toDto(administrador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministradorMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(administradorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Administrador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAdministradorWithPatch() throws Exception {
        // Initialize the database
        insertedAdministrador = administradorRepository.saveAndFlush(administrador);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the administrador using partial update
        Administrador partialUpdatedAdministrador = new Administrador();
        partialUpdatedAdministrador.setId(administrador.getId());

        partialUpdatedAdministrador.nome(UPDATED_NOME);

        restAdministradorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdministrador.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAdministrador))
            )
            .andExpect(status().isOk());

        // Validate the Administrador in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAdministradorUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAdministrador, administrador),
            getPersistedAdministrador(administrador)
        );
    }

    @Test
    @Transactional
    void fullUpdateAdministradorWithPatch() throws Exception {
        // Initialize the database
        insertedAdministrador = administradorRepository.saveAndFlush(administrador);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the administrador using partial update
        Administrador partialUpdatedAdministrador = new Administrador();
        partialUpdatedAdministrador.setId(administrador.getId());

        partialUpdatedAdministrador.nome(UPDATED_NOME).telefone(UPDATED_TELEFONE).email(UPDATED_EMAIL);

        restAdministradorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdministrador.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAdministrador))
            )
            .andExpect(status().isOk());

        // Validate the Administrador in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAdministradorUpdatableFieldsEquals(partialUpdatedAdministrador, getPersistedAdministrador(partialUpdatedAdministrador));
    }

    @Test
    @Transactional
    void patchNonExistingAdministrador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        administrador.setId(longCount.incrementAndGet());

        // Create the Administrador
        AdministradorDTO administradorDTO = administradorMapper.toDto(administrador);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdministradorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, administradorDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(administradorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAdministrador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        administrador.setId(longCount.incrementAndGet());

        // Create the Administrador
        AdministradorDTO administradorDTO = administradorMapper.toDto(administrador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministradorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(administradorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAdministrador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        administrador.setId(longCount.incrementAndGet());

        // Create the Administrador
        AdministradorDTO administradorDTO = administradorMapper.toDto(administrador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministradorMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(administradorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Administrador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAdministrador() throws Exception {
        // Initialize the database
        insertedAdministrador = administradorRepository.saveAndFlush(administrador);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the administrador
        restAdministradorMockMvc
            .perform(delete(ENTITY_API_URL_ID, administrador.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return administradorRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Administrador getPersistedAdministrador(Administrador administrador) {
        return administradorRepository.findById(administrador.getId()).orElseThrow();
    }

    protected void assertPersistedAdministradorToMatchAllProperties(Administrador expectedAdministrador) {
        assertAdministradorAllPropertiesEquals(expectedAdministrador, getPersistedAdministrador(expectedAdministrador));
    }

    protected void assertPersistedAdministradorToMatchUpdatableProperties(Administrador expectedAdministrador) {
        assertAdministradorAllUpdatablePropertiesEquals(expectedAdministrador, getPersistedAdministrador(expectedAdministrador));
    }
}

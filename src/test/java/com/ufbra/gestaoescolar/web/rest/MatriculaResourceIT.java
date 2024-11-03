package com.ufbra.gestaoescolar.web.rest;

import static com.ufbra.gestaoescolar.domain.MatriculaAsserts.*;
import static com.ufbra.gestaoescolar.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufbra.gestaoescolar.IntegrationTest;
import com.ufbra.gestaoescolar.domain.Matricula;
import com.ufbra.gestaoescolar.domain.enumeration.StatusMatricula;
import com.ufbra.gestaoescolar.repository.MatriculaRepository;
import com.ufbra.gestaoescolar.service.MatriculaService;
import com.ufbra.gestaoescolar.service.dto.MatriculaDTO;
import com.ufbra.gestaoescolar.service.mapper.MatriculaMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MatriculaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MatriculaResourceIT {

    private static final LocalDate DEFAULT_DATA_DE_MATRICULA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_DE_MATRICULA = LocalDate.now(ZoneId.systemDefault());

    private static final StatusMatricula DEFAULT_STATUS = StatusMatricula.ATIVO;
    private static final StatusMatricula UPDATED_STATUS = StatusMatricula.INATIVO;

    private static final String ENTITY_API_URL = "/api/matriculas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Mock
    private MatriculaRepository matriculaRepositoryMock;

    @Autowired
    private MatriculaMapper matriculaMapper;

    @Mock
    private MatriculaService matriculaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMatriculaMockMvc;

    private Matricula matricula;

    private Matricula insertedMatricula;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Matricula createEntity() {
        return new Matricula().dataDeMatricula(DEFAULT_DATA_DE_MATRICULA).status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Matricula createUpdatedEntity() {
        return new Matricula().dataDeMatricula(UPDATED_DATA_DE_MATRICULA).status(UPDATED_STATUS);
    }

    @BeforeEach
    public void initTest() {
        matricula = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMatricula != null) {
            matriculaRepository.delete(insertedMatricula);
            insertedMatricula = null;
        }
    }

    @Test
    @Transactional
    void createMatricula() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Matricula
        MatriculaDTO matriculaDTO = matriculaMapper.toDto(matricula);
        var returnedMatriculaDTO = om.readValue(
            restMatriculaMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(matriculaDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MatriculaDTO.class
        );

        // Validate the Matricula in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMatricula = matriculaMapper.toEntity(returnedMatriculaDTO);
        assertMatriculaUpdatableFieldsEquals(returnedMatricula, getPersistedMatricula(returnedMatricula));

        insertedMatricula = returnedMatricula;
    }

    @Test
    @Transactional
    void createMatriculaWithExistingId() throws Exception {
        // Create the Matricula with an existing ID
        matricula.setId(1L);
        MatriculaDTO matriculaDTO = matriculaMapper.toDto(matricula);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMatriculaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(matriculaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Matricula in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDataDeMatriculaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        matricula.setDataDeMatricula(null);

        // Create the Matricula, which fails.
        MatriculaDTO matriculaDTO = matriculaMapper.toDto(matricula);

        restMatriculaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(matriculaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        matricula.setStatus(null);

        // Create the Matricula, which fails.
        MatriculaDTO matriculaDTO = matriculaMapper.toDto(matricula);

        restMatriculaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(matriculaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMatriculas() throws Exception {
        // Initialize the database
        insertedMatricula = matriculaRepository.saveAndFlush(matricula);

        // Get all the matriculaList
        restMatriculaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(matricula.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataDeMatricula").value(hasItem(DEFAULT_DATA_DE_MATRICULA.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMatriculasWithEagerRelationshipsIsEnabled() throws Exception {
        when(matriculaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMatriculaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(matriculaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMatriculasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(matriculaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMatriculaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(matriculaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMatricula() throws Exception {
        // Initialize the database
        insertedMatricula = matriculaRepository.saveAndFlush(matricula);

        // Get the matricula
        restMatriculaMockMvc
            .perform(get(ENTITY_API_URL_ID, matricula.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(matricula.getId().intValue()))
            .andExpect(jsonPath("$.dataDeMatricula").value(DEFAULT_DATA_DE_MATRICULA.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMatricula() throws Exception {
        // Get the matricula
        restMatriculaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMatricula() throws Exception {
        // Initialize the database
        insertedMatricula = matriculaRepository.saveAndFlush(matricula);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the matricula
        Matricula updatedMatricula = matriculaRepository.findById(matricula.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMatricula are not directly saved in db
        em.detach(updatedMatricula);
        updatedMatricula.dataDeMatricula(UPDATED_DATA_DE_MATRICULA).status(UPDATED_STATUS);
        MatriculaDTO matriculaDTO = matriculaMapper.toDto(updatedMatricula);

        restMatriculaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, matriculaDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(matriculaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Matricula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMatriculaToMatchAllProperties(updatedMatricula);
    }

    @Test
    @Transactional
    void putNonExistingMatricula() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        matricula.setId(longCount.incrementAndGet());

        // Create the Matricula
        MatriculaDTO matriculaDTO = matriculaMapper.toDto(matricula);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMatriculaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, matriculaDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(matriculaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Matricula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMatricula() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        matricula.setId(longCount.incrementAndGet());

        // Create the Matricula
        MatriculaDTO matriculaDTO = matriculaMapper.toDto(matricula);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatriculaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(matriculaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Matricula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMatricula() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        matricula.setId(longCount.incrementAndGet());

        // Create the Matricula
        MatriculaDTO matriculaDTO = matriculaMapper.toDto(matricula);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatriculaMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(matriculaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Matricula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMatriculaWithPatch() throws Exception {
        // Initialize the database
        insertedMatricula = matriculaRepository.saveAndFlush(matricula);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the matricula using partial update
        Matricula partialUpdatedMatricula = new Matricula();
        partialUpdatedMatricula.setId(matricula.getId());

        partialUpdatedMatricula.status(UPDATED_STATUS);

        restMatriculaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMatricula.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMatricula))
            )
            .andExpect(status().isOk());

        // Validate the Matricula in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMatriculaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMatricula, matricula),
            getPersistedMatricula(matricula)
        );
    }

    @Test
    @Transactional
    void fullUpdateMatriculaWithPatch() throws Exception {
        // Initialize the database
        insertedMatricula = matriculaRepository.saveAndFlush(matricula);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the matricula using partial update
        Matricula partialUpdatedMatricula = new Matricula();
        partialUpdatedMatricula.setId(matricula.getId());

        partialUpdatedMatricula.dataDeMatricula(UPDATED_DATA_DE_MATRICULA).status(UPDATED_STATUS);

        restMatriculaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMatricula.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMatricula))
            )
            .andExpect(status().isOk());

        // Validate the Matricula in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMatriculaUpdatableFieldsEquals(partialUpdatedMatricula, getPersistedMatricula(partialUpdatedMatricula));
    }

    @Test
    @Transactional
    void patchNonExistingMatricula() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        matricula.setId(longCount.incrementAndGet());

        // Create the Matricula
        MatriculaDTO matriculaDTO = matriculaMapper.toDto(matricula);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMatriculaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, matriculaDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(matriculaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Matricula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMatricula() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        matricula.setId(longCount.incrementAndGet());

        // Create the Matricula
        MatriculaDTO matriculaDTO = matriculaMapper.toDto(matricula);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatriculaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(matriculaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Matricula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMatricula() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        matricula.setId(longCount.incrementAndGet());

        // Create the Matricula
        MatriculaDTO matriculaDTO = matriculaMapper.toDto(matricula);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatriculaMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(matriculaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Matricula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMatricula() throws Exception {
        // Initialize the database
        insertedMatricula = matriculaRepository.saveAndFlush(matricula);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the matricula
        restMatriculaMockMvc
            .perform(delete(ENTITY_API_URL_ID, matricula.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return matriculaRepository.count();
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

    protected Matricula getPersistedMatricula(Matricula matricula) {
        return matriculaRepository.findById(matricula.getId()).orElseThrow();
    }

    protected void assertPersistedMatriculaToMatchAllProperties(Matricula expectedMatricula) {
        assertMatriculaAllPropertiesEquals(expectedMatricula, getPersistedMatricula(expectedMatricula));
    }

    protected void assertPersistedMatriculaToMatchUpdatableProperties(Matricula expectedMatricula) {
        assertMatriculaAllUpdatablePropertiesEquals(expectedMatricula, getPersistedMatricula(expectedMatricula));
    }
}

package com.ufbra.gestaoescolar.web.rest;

import static com.ufbra.gestaoescolar.domain.NotaAsserts.*;
import static com.ufbra.gestaoescolar.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufbra.gestaoescolar.IntegrationTest;
import com.ufbra.gestaoescolar.domain.Nota;
import com.ufbra.gestaoescolar.repository.NotaRepository;
import com.ufbra.gestaoescolar.service.NotaService;
import com.ufbra.gestaoescolar.service.dto.NotaDTO;
import com.ufbra.gestaoescolar.service.mapper.NotaMapper;
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
 * Integration tests for the {@link NotaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NotaResourceIT {

    private static final Float DEFAULT_PONTUACAO = 1F;
    private static final Float UPDATED_PONTUACAO = 2F;

    private static final LocalDate DEFAULT_DATA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/notas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NotaRepository notaRepository;

    @Mock
    private NotaRepository notaRepositoryMock;

    @Autowired
    private NotaMapper notaMapper;

    @Mock
    private NotaService notaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNotaMockMvc;

    private Nota nota;

    private Nota insertedNota;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Nota createEntity() {
        return new Nota().pontuacao(DEFAULT_PONTUACAO).data(DEFAULT_DATA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Nota createUpdatedEntity() {
        return new Nota().pontuacao(UPDATED_PONTUACAO).data(UPDATED_DATA);
    }

    @BeforeEach
    public void initTest() {
        nota = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNota != null) {
            notaRepository.delete(insertedNota);
            insertedNota = null;
        }
    }

    @Test
    @Transactional
    void createNota() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Nota
        NotaDTO notaDTO = notaMapper.toDto(nota);
        var returnedNotaDTO = om.readValue(
            restNotaMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NotaDTO.class
        );

        // Validate the Nota in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNota = notaMapper.toEntity(returnedNotaDTO);
        assertNotaUpdatableFieldsEquals(returnedNota, getPersistedNota(returnedNota));

        insertedNota = returnedNota;
    }

    @Test
    @Transactional
    void createNotaWithExistingId() throws Exception {
        // Create the Nota with an existing ID
        nota.setId(1L);
        NotaDTO notaDTO = notaMapper.toDto(nota);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Nota in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPontuacaoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nota.setPontuacao(null);

        // Create the Nota, which fails.
        NotaDTO notaDTO = notaMapper.toDto(nota);

        restNotaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDataIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nota.setData(null);

        // Create the Nota, which fails.
        NotaDTO notaDTO = notaMapper.toDto(nota);

        restNotaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNotas() throws Exception {
        // Initialize the database
        insertedNota = notaRepository.saveAndFlush(nota);

        // Get all the notaList
        restNotaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nota.getId().intValue())))
            .andExpect(jsonPath("$.[*].pontuacao").value(hasItem(DEFAULT_PONTUACAO.doubleValue())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNotasWithEagerRelationshipsIsEnabled() throws Exception {
        when(notaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNotaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(notaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNotasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(notaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNotaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(notaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNota() throws Exception {
        // Initialize the database
        insertedNota = notaRepository.saveAndFlush(nota);

        // Get the nota
        restNotaMockMvc
            .perform(get(ENTITY_API_URL_ID, nota.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nota.getId().intValue()))
            .andExpect(jsonPath("$.pontuacao").value(DEFAULT_PONTUACAO.doubleValue()))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA.toString()));
    }

    @Test
    @Transactional
    void getNonExistingNota() throws Exception {
        // Get the nota
        restNotaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNota() throws Exception {
        // Initialize the database
        insertedNota = notaRepository.saveAndFlush(nota);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nota
        Nota updatedNota = notaRepository.findById(nota.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNota are not directly saved in db
        em.detach(updatedNota);
        updatedNota.pontuacao(UPDATED_PONTUACAO).data(UPDATED_DATA);
        NotaDTO notaDTO = notaMapper.toDto(updatedNota);

        restNotaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notaDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Nota in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNotaToMatchAllProperties(updatedNota);
    }

    @Test
    @Transactional
    void putNonExistingNota() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nota.setId(longCount.incrementAndGet());

        // Create the Nota
        NotaDTO notaDTO = notaMapper.toDto(nota);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notaDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nota in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNota() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nota.setId(longCount.incrementAndGet());

        // Create the Nota
        NotaDTO notaDTO = notaMapper.toDto(nota);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nota in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNota() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nota.setId(longCount.incrementAndGet());

        // Create the Nota
        NotaDTO notaDTO = notaMapper.toDto(nota);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotaMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Nota in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNotaWithPatch() throws Exception {
        // Initialize the database
        insertedNota = notaRepository.saveAndFlush(nota);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nota using partial update
        Nota partialUpdatedNota = new Nota();
        partialUpdatedNota.setId(nota.getId());

        partialUpdatedNota.data(UPDATED_DATA);

        restNotaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNota.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNota))
            )
            .andExpect(status().isOk());

        // Validate the Nota in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNotaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedNota, nota), getPersistedNota(nota));
    }

    @Test
    @Transactional
    void fullUpdateNotaWithPatch() throws Exception {
        // Initialize the database
        insertedNota = notaRepository.saveAndFlush(nota);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nota using partial update
        Nota partialUpdatedNota = new Nota();
        partialUpdatedNota.setId(nota.getId());

        partialUpdatedNota.pontuacao(UPDATED_PONTUACAO).data(UPDATED_DATA);

        restNotaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNota.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNota))
            )
            .andExpect(status().isOk());

        // Validate the Nota in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNotaUpdatableFieldsEquals(partialUpdatedNota, getPersistedNota(partialUpdatedNota));
    }

    @Test
    @Transactional
    void patchNonExistingNota() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nota.setId(longCount.incrementAndGet());

        // Create the Nota
        NotaDTO notaDTO = notaMapper.toDto(nota);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, notaDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(notaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nota in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNota() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nota.setId(longCount.incrementAndGet());

        // Create the Nota
        NotaDTO notaDTO = notaMapper.toDto(nota);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(notaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nota in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNota() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nota.setId(longCount.incrementAndGet());

        // Create the Nota
        NotaDTO notaDTO = notaMapper.toDto(nota);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotaMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(notaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Nota in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNota() throws Exception {
        // Initialize the database
        insertedNota = notaRepository.saveAndFlush(nota);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nota
        restNotaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nota.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return notaRepository.count();
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

    protected Nota getPersistedNota(Nota nota) {
        return notaRepository.findById(nota.getId()).orElseThrow();
    }

    protected void assertPersistedNotaToMatchAllProperties(Nota expectedNota) {
        assertNotaAllPropertiesEquals(expectedNota, getPersistedNota(expectedNota));
    }

    protected void assertPersistedNotaToMatchUpdatableProperties(Nota expectedNota) {
        assertNotaAllUpdatablePropertiesEquals(expectedNota, getPersistedNota(expectedNota));
    }
}

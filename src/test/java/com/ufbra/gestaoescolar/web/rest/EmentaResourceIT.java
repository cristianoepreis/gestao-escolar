package com.ufbra.gestaoescolar.web.rest;

import static com.ufbra.gestaoescolar.domain.EmentaAsserts.*;
import static com.ufbra.gestaoescolar.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufbra.gestaoescolar.IntegrationTest;
import com.ufbra.gestaoescolar.domain.Ementa;
import com.ufbra.gestaoescolar.repository.EmentaRepository;
import com.ufbra.gestaoescolar.service.EmentaService;
import com.ufbra.gestaoescolar.service.dto.EmentaDTO;
import com.ufbra.gestaoescolar.service.mapper.EmentaMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link EmentaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EmentaResourceIT {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String DEFAULT_BIBLIOGRAFIA_BASICA = "AAAAAAAAAA";
    private static final String UPDATED_BIBLIOGRAFIA_BASICA = "BBBBBBBBBB";

    private static final String DEFAULT_BIBLIOGRAFIA_COMPLEMENTAR = "AAAAAAAAAA";
    private static final String UPDATED_BIBLIOGRAFIA_COMPLEMENTAR = "BBBBBBBBBB";

    private static final String DEFAULT_PRATICA_LABORATORIAL = "AAAAAAAAAA";
    private static final String UPDATED_PRATICA_LABORATORIAL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ementas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmentaRepository ementaRepository;

    @Mock
    private EmentaRepository ementaRepositoryMock;

    @Autowired
    private EmentaMapper ementaMapper;

    @Mock
    private EmentaService ementaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmentaMockMvc;

    private Ementa ementa;

    private Ementa insertedEmenta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ementa createEntity() {
        return new Ementa()
            .descricao(DEFAULT_DESCRICAO)
            .bibliografiaBasica(DEFAULT_BIBLIOGRAFIA_BASICA)
            .bibliografiaComplementar(DEFAULT_BIBLIOGRAFIA_COMPLEMENTAR)
            .praticaLaboratorial(DEFAULT_PRATICA_LABORATORIAL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ementa createUpdatedEntity() {
        return new Ementa()
            .descricao(UPDATED_DESCRICAO)
            .bibliografiaBasica(UPDATED_BIBLIOGRAFIA_BASICA)
            .bibliografiaComplementar(UPDATED_BIBLIOGRAFIA_COMPLEMENTAR)
            .praticaLaboratorial(UPDATED_PRATICA_LABORATORIAL);
    }

    @BeforeEach
    public void initTest() {
        ementa = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedEmenta != null) {
            ementaRepository.delete(insertedEmenta);
            insertedEmenta = null;
        }
    }

    @Test
    @Transactional
    void createEmenta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Ementa
        EmentaDTO ementaDTO = ementaMapper.toDto(ementa);
        var returnedEmentaDTO = om.readValue(
            restEmentaMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ementaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EmentaDTO.class
        );

        // Validate the Ementa in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEmenta = ementaMapper.toEntity(returnedEmentaDTO);
        assertEmentaUpdatableFieldsEquals(returnedEmenta, getPersistedEmenta(returnedEmenta));

        insertedEmenta = returnedEmenta;
    }

    @Test
    @Transactional
    void createEmentaWithExistingId() throws Exception {
        // Create the Ementa with an existing ID
        ementa.setId(1L);
        EmentaDTO ementaDTO = ementaMapper.toDto(ementa);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmentaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ementaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ementa in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEmentas() throws Exception {
        // Initialize the database
        insertedEmenta = ementaRepository.saveAndFlush(ementa);

        // Get all the ementaList
        restEmentaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ementa.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())))
            .andExpect(jsonPath("$.[*].bibliografiaBasica").value(hasItem(DEFAULT_BIBLIOGRAFIA_BASICA.toString())))
            .andExpect(jsonPath("$.[*].bibliografiaComplementar").value(hasItem(DEFAULT_BIBLIOGRAFIA_COMPLEMENTAR.toString())))
            .andExpect(jsonPath("$.[*].praticaLaboratorial").value(hasItem(DEFAULT_PRATICA_LABORATORIAL.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmentasWithEagerRelationshipsIsEnabled() throws Exception {
        when(ementaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmentaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(ementaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmentasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(ementaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmentaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(ementaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEmenta() throws Exception {
        // Initialize the database
        insertedEmenta = ementaRepository.saveAndFlush(ementa);

        // Get the ementa
        restEmentaMockMvc
            .perform(get(ENTITY_API_URL_ID, ementa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ementa.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()))
            .andExpect(jsonPath("$.bibliografiaBasica").value(DEFAULT_BIBLIOGRAFIA_BASICA.toString()))
            .andExpect(jsonPath("$.bibliografiaComplementar").value(DEFAULT_BIBLIOGRAFIA_COMPLEMENTAR.toString()))
            .andExpect(jsonPath("$.praticaLaboratorial").value(DEFAULT_PRATICA_LABORATORIAL.toString()));
    }

    @Test
    @Transactional
    void getNonExistingEmenta() throws Exception {
        // Get the ementa
        restEmentaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmenta() throws Exception {
        // Initialize the database
        insertedEmenta = ementaRepository.saveAndFlush(ementa);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ementa
        Ementa updatedEmenta = ementaRepository.findById(ementa.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEmenta are not directly saved in db
        em.detach(updatedEmenta);
        updatedEmenta
            .descricao(UPDATED_DESCRICAO)
            .bibliografiaBasica(UPDATED_BIBLIOGRAFIA_BASICA)
            .bibliografiaComplementar(UPDATED_BIBLIOGRAFIA_COMPLEMENTAR)
            .praticaLaboratorial(UPDATED_PRATICA_LABORATORIAL);
        EmentaDTO ementaDTO = ementaMapper.toDto(updatedEmenta);

        restEmentaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ementaDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ementaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Ementa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmentaToMatchAllProperties(updatedEmenta);
    }

    @Test
    @Transactional
    void putNonExistingEmenta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ementa.setId(longCount.incrementAndGet());

        // Create the Ementa
        EmentaDTO ementaDTO = ementaMapper.toDto(ementa);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmentaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ementaDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ementaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ementa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmenta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ementa.setId(longCount.incrementAndGet());

        // Create the Ementa
        EmentaDTO ementaDTO = ementaMapper.toDto(ementa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmentaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ementaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ementa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmenta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ementa.setId(longCount.incrementAndGet());

        // Create the Ementa
        EmentaDTO ementaDTO = ementaMapper.toDto(ementa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmentaMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ementaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ementa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmentaWithPatch() throws Exception {
        // Initialize the database
        insertedEmenta = ementaRepository.saveAndFlush(ementa);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ementa using partial update
        Ementa partialUpdatedEmenta = new Ementa();
        partialUpdatedEmenta.setId(ementa.getId());

        partialUpdatedEmenta.descricao(UPDATED_DESCRICAO).praticaLaboratorial(UPDATED_PRATICA_LABORATORIAL);

        restEmentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmenta.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmenta))
            )
            .andExpect(status().isOk());

        // Validate the Ementa in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmentaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedEmenta, ementa), getPersistedEmenta(ementa));
    }

    @Test
    @Transactional
    void fullUpdateEmentaWithPatch() throws Exception {
        // Initialize the database
        insertedEmenta = ementaRepository.saveAndFlush(ementa);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ementa using partial update
        Ementa partialUpdatedEmenta = new Ementa();
        partialUpdatedEmenta.setId(ementa.getId());

        partialUpdatedEmenta
            .descricao(UPDATED_DESCRICAO)
            .bibliografiaBasica(UPDATED_BIBLIOGRAFIA_BASICA)
            .bibliografiaComplementar(UPDATED_BIBLIOGRAFIA_COMPLEMENTAR)
            .praticaLaboratorial(UPDATED_PRATICA_LABORATORIAL);

        restEmentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmenta.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmenta))
            )
            .andExpect(status().isOk());

        // Validate the Ementa in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmentaUpdatableFieldsEquals(partialUpdatedEmenta, getPersistedEmenta(partialUpdatedEmenta));
    }

    @Test
    @Transactional
    void patchNonExistingEmenta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ementa.setId(longCount.incrementAndGet());

        // Create the Ementa
        EmentaDTO ementaDTO = ementaMapper.toDto(ementa);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ementaDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ementaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ementa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmenta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ementa.setId(longCount.incrementAndGet());

        // Create the Ementa
        EmentaDTO ementaDTO = ementaMapper.toDto(ementa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ementaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ementa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmenta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ementa.setId(longCount.incrementAndGet());

        // Create the Ementa
        EmentaDTO ementaDTO = ementaMapper.toDto(ementa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmentaMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ementaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ementa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmenta() throws Exception {
        // Initialize the database
        insertedEmenta = ementaRepository.saveAndFlush(ementa);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the ementa
        restEmentaMockMvc
            .perform(delete(ENTITY_API_URL_ID, ementa.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return ementaRepository.count();
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

    protected Ementa getPersistedEmenta(Ementa ementa) {
        return ementaRepository.findById(ementa.getId()).orElseThrow();
    }

    protected void assertPersistedEmentaToMatchAllProperties(Ementa expectedEmenta) {
        assertEmentaAllPropertiesEquals(expectedEmenta, getPersistedEmenta(expectedEmenta));
    }

    protected void assertPersistedEmentaToMatchUpdatableProperties(Ementa expectedEmenta) {
        assertEmentaAllUpdatablePropertiesEquals(expectedEmenta, getPersistedEmenta(expectedEmenta));
    }
}

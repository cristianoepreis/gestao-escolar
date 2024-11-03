package com.ufbra.gestaoescolar.web.rest;

import static com.ufbra.gestaoescolar.domain.DisciplinaAsserts.*;
import static com.ufbra.gestaoescolar.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufbra.gestaoescolar.IntegrationTest;
import com.ufbra.gestaoescolar.domain.Disciplina;
import com.ufbra.gestaoescolar.repository.DisciplinaRepository;
import com.ufbra.gestaoescolar.service.dto.DisciplinaDTO;
import com.ufbra.gestaoescolar.service.mapper.DisciplinaMapper;
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
 * Integration tests for the {@link DisciplinaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DisciplinaResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final Integer DEFAULT_CARGA_HORARIA = 1;
    private static final Integer UPDATED_CARGA_HORARIA = 2;

    private static final String ENTITY_API_URL = "/api/disciplinas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private DisciplinaMapper disciplinaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDisciplinaMockMvc;

    private Disciplina disciplina;

    private Disciplina insertedDisciplina;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Disciplina createEntity() {
        return new Disciplina().nome(DEFAULT_NOME).codigo(DEFAULT_CODIGO).cargaHoraria(DEFAULT_CARGA_HORARIA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Disciplina createUpdatedEntity() {
        return new Disciplina().nome(UPDATED_NOME).codigo(UPDATED_CODIGO).cargaHoraria(UPDATED_CARGA_HORARIA);
    }

    @BeforeEach
    public void initTest() {
        disciplina = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedDisciplina != null) {
            disciplinaRepository.delete(insertedDisciplina);
            insertedDisciplina = null;
        }
    }

    @Test
    @Transactional
    void createDisciplina() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Disciplina
        DisciplinaDTO disciplinaDTO = disciplinaMapper.toDto(disciplina);
        var returnedDisciplinaDTO = om.readValue(
            restDisciplinaMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(disciplinaDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DisciplinaDTO.class
        );

        // Validate the Disciplina in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDisciplina = disciplinaMapper.toEntity(returnedDisciplinaDTO);
        assertDisciplinaUpdatableFieldsEquals(returnedDisciplina, getPersistedDisciplina(returnedDisciplina));

        insertedDisciplina = returnedDisciplina;
    }

    @Test
    @Transactional
    void createDisciplinaWithExistingId() throws Exception {
        // Create the Disciplina with an existing ID
        disciplina.setId(1L);
        DisciplinaDTO disciplinaDTO = disciplinaMapper.toDto(disciplina);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDisciplinaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(disciplinaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Disciplina in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        disciplina.setNome(null);

        // Create the Disciplina, which fails.
        DisciplinaDTO disciplinaDTO = disciplinaMapper.toDto(disciplina);

        restDisciplinaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(disciplinaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        disciplina.setCodigo(null);

        // Create the Disciplina, which fails.
        DisciplinaDTO disciplinaDTO = disciplinaMapper.toDto(disciplina);

        restDisciplinaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(disciplinaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCargaHorariaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        disciplina.setCargaHoraria(null);

        // Create the Disciplina, which fails.
        DisciplinaDTO disciplinaDTO = disciplinaMapper.toDto(disciplina);

        restDisciplinaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(disciplinaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDisciplinas() throws Exception {
        // Initialize the database
        insertedDisciplina = disciplinaRepository.saveAndFlush(disciplina);

        // Get all the disciplinaList
        restDisciplinaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(disciplina.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].cargaHoraria").value(hasItem(DEFAULT_CARGA_HORARIA)));
    }

    @Test
    @Transactional
    void getDisciplina() throws Exception {
        // Initialize the database
        insertedDisciplina = disciplinaRepository.saveAndFlush(disciplina);

        // Get the disciplina
        restDisciplinaMockMvc
            .perform(get(ENTITY_API_URL_ID, disciplina.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(disciplina.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.cargaHoraria").value(DEFAULT_CARGA_HORARIA));
    }

    @Test
    @Transactional
    void getNonExistingDisciplina() throws Exception {
        // Get the disciplina
        restDisciplinaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDisciplina() throws Exception {
        // Initialize the database
        insertedDisciplina = disciplinaRepository.saveAndFlush(disciplina);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the disciplina
        Disciplina updatedDisciplina = disciplinaRepository.findById(disciplina.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDisciplina are not directly saved in db
        em.detach(updatedDisciplina);
        updatedDisciplina.nome(UPDATED_NOME).codigo(UPDATED_CODIGO).cargaHoraria(UPDATED_CARGA_HORARIA);
        DisciplinaDTO disciplinaDTO = disciplinaMapper.toDto(updatedDisciplina);

        restDisciplinaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, disciplinaDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(disciplinaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Disciplina in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDisciplinaToMatchAllProperties(updatedDisciplina);
    }

    @Test
    @Transactional
    void putNonExistingDisciplina() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disciplina.setId(longCount.incrementAndGet());

        // Create the Disciplina
        DisciplinaDTO disciplinaDTO = disciplinaMapper.toDto(disciplina);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDisciplinaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, disciplinaDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(disciplinaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disciplina in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDisciplina() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disciplina.setId(longCount.incrementAndGet());

        // Create the Disciplina
        DisciplinaDTO disciplinaDTO = disciplinaMapper.toDto(disciplina);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisciplinaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(disciplinaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disciplina in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDisciplina() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disciplina.setId(longCount.incrementAndGet());

        // Create the Disciplina
        DisciplinaDTO disciplinaDTO = disciplinaMapper.toDto(disciplina);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisciplinaMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(disciplinaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Disciplina in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDisciplinaWithPatch() throws Exception {
        // Initialize the database
        insertedDisciplina = disciplinaRepository.saveAndFlush(disciplina);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the disciplina using partial update
        Disciplina partialUpdatedDisciplina = new Disciplina();
        partialUpdatedDisciplina.setId(disciplina.getId());

        partialUpdatedDisciplina.codigo(UPDATED_CODIGO).cargaHoraria(UPDATED_CARGA_HORARIA);

        restDisciplinaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDisciplina.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDisciplina))
            )
            .andExpect(status().isOk());

        // Validate the Disciplina in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDisciplinaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDisciplina, disciplina),
            getPersistedDisciplina(disciplina)
        );
    }

    @Test
    @Transactional
    void fullUpdateDisciplinaWithPatch() throws Exception {
        // Initialize the database
        insertedDisciplina = disciplinaRepository.saveAndFlush(disciplina);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the disciplina using partial update
        Disciplina partialUpdatedDisciplina = new Disciplina();
        partialUpdatedDisciplina.setId(disciplina.getId());

        partialUpdatedDisciplina.nome(UPDATED_NOME).codigo(UPDATED_CODIGO).cargaHoraria(UPDATED_CARGA_HORARIA);

        restDisciplinaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDisciplina.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDisciplina))
            )
            .andExpect(status().isOk());

        // Validate the Disciplina in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDisciplinaUpdatableFieldsEquals(partialUpdatedDisciplina, getPersistedDisciplina(partialUpdatedDisciplina));
    }

    @Test
    @Transactional
    void patchNonExistingDisciplina() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disciplina.setId(longCount.incrementAndGet());

        // Create the Disciplina
        DisciplinaDTO disciplinaDTO = disciplinaMapper.toDto(disciplina);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDisciplinaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, disciplinaDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(disciplinaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disciplina in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDisciplina() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disciplina.setId(longCount.incrementAndGet());

        // Create the Disciplina
        DisciplinaDTO disciplinaDTO = disciplinaMapper.toDto(disciplina);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisciplinaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(disciplinaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disciplina in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDisciplina() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disciplina.setId(longCount.incrementAndGet());

        // Create the Disciplina
        DisciplinaDTO disciplinaDTO = disciplinaMapper.toDto(disciplina);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisciplinaMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(disciplinaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Disciplina in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDisciplina() throws Exception {
        // Initialize the database
        insertedDisciplina = disciplinaRepository.saveAndFlush(disciplina);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the disciplina
        restDisciplinaMockMvc
            .perform(delete(ENTITY_API_URL_ID, disciplina.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return disciplinaRepository.count();
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

    protected Disciplina getPersistedDisciplina(Disciplina disciplina) {
        return disciplinaRepository.findById(disciplina.getId()).orElseThrow();
    }

    protected void assertPersistedDisciplinaToMatchAllProperties(Disciplina expectedDisciplina) {
        assertDisciplinaAllPropertiesEquals(expectedDisciplina, getPersistedDisciplina(expectedDisciplina));
    }

    protected void assertPersistedDisciplinaToMatchUpdatableProperties(Disciplina expectedDisciplina) {
        assertDisciplinaAllUpdatablePropertiesEquals(expectedDisciplina, getPersistedDisciplina(expectedDisciplina));
    }
}

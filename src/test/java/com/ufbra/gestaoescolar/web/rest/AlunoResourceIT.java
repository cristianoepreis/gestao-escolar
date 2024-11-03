package com.ufbra.gestaoescolar.web.rest;

import static com.ufbra.gestaoescolar.domain.AlunoAsserts.*;
import static com.ufbra.gestaoescolar.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufbra.gestaoescolar.IntegrationTest;
import com.ufbra.gestaoescolar.domain.Aluno;
import com.ufbra.gestaoescolar.repository.AlunoRepository;
import com.ufbra.gestaoescolar.service.AlunoService;
import com.ufbra.gestaoescolar.service.dto.AlunoDTO;
import com.ufbra.gestaoescolar.service.mapper.AlunoMapper;
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
 * Integration tests for the {@link AlunoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AlunoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATA_DE_NASCIMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_DE_NASCIMENTO = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CPF = "AAAAAAAAAA";
    private static final String UPDATED_CPF = "BBBBBBBBBB";

    private static final String DEFAULT_ENDERECO = "AAAAAAAAAA";
    private static final String UPDATED_ENDERECO = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/alunos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlunoRepository alunoRepository;

    @Mock
    private AlunoRepository alunoRepositoryMock;

    @Autowired
    private AlunoMapper alunoMapper;

    @Mock
    private AlunoService alunoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlunoMockMvc;

    private Aluno aluno;

    private Aluno insertedAluno;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Aluno createEntity() {
        return new Aluno()
            .nome(DEFAULT_NOME)
            .dataDeNascimento(DEFAULT_DATA_DE_NASCIMENTO)
            .cpf(DEFAULT_CPF)
            .endereco(DEFAULT_ENDERECO)
            .telefone(DEFAULT_TELEFONE)
            .email(DEFAULT_EMAIL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Aluno createUpdatedEntity() {
        return new Aluno()
            .nome(UPDATED_NOME)
            .dataDeNascimento(UPDATED_DATA_DE_NASCIMENTO)
            .cpf(UPDATED_CPF)
            .endereco(UPDATED_ENDERECO)
            .telefone(UPDATED_TELEFONE)
            .email(UPDATED_EMAIL);
    }

    @BeforeEach
    public void initTest() {
        aluno = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAluno != null) {
            alunoRepository.delete(insertedAluno);
            insertedAluno = null;
        }
    }

    @Test
    @Transactional
    void createAluno() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Aluno
        AlunoDTO alunoDTO = alunoMapper.toDto(aluno);
        var returnedAlunoDTO = om.readValue(
            restAlunoMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alunoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlunoDTO.class
        );

        // Validate the Aluno in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAluno = alunoMapper.toEntity(returnedAlunoDTO);
        assertAlunoUpdatableFieldsEquals(returnedAluno, getPersistedAluno(returnedAluno));

        insertedAluno = returnedAluno;
    }

    @Test
    @Transactional
    void createAlunoWithExistingId() throws Exception {
        // Create the Aluno with an existing ID
        aluno.setId(1L);
        AlunoDTO alunoDTO = alunoMapper.toDto(aluno);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlunoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alunoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Aluno in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        aluno.setNome(null);

        // Create the Aluno, which fails.
        AlunoDTO alunoDTO = alunoMapper.toDto(aluno);

        restAlunoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alunoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDataDeNascimentoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        aluno.setDataDeNascimento(null);

        // Create the Aluno, which fails.
        AlunoDTO alunoDTO = alunoMapper.toDto(aluno);

        restAlunoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alunoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCpfIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        aluno.setCpf(null);

        // Create the Aluno, which fails.
        AlunoDTO alunoDTO = alunoMapper.toDto(aluno);

        restAlunoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alunoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        aluno.setEmail(null);

        // Create the Aluno, which fails.
        AlunoDTO alunoDTO = alunoMapper.toDto(aluno);

        restAlunoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alunoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlunos() throws Exception {
        // Initialize the database
        insertedAluno = alunoRepository.saveAndFlush(aluno);

        // Get all the alunoList
        restAlunoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aluno.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].dataDeNascimento").value(hasItem(DEFAULT_DATA_DE_NASCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF)))
            .andExpect(jsonPath("$.[*].endereco").value(hasItem(DEFAULT_ENDERECO)))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAlunosWithEagerRelationshipsIsEnabled() throws Exception {
        when(alunoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAlunoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(alunoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAlunosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(alunoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAlunoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(alunoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAluno() throws Exception {
        // Initialize the database
        insertedAluno = alunoRepository.saveAndFlush(aluno);

        // Get the aluno
        restAlunoMockMvc
            .perform(get(ENTITY_API_URL_ID, aluno.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aluno.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.dataDeNascimento").value(DEFAULT_DATA_DE_NASCIMENTO.toString()))
            .andExpect(jsonPath("$.cpf").value(DEFAULT_CPF))
            .andExpect(jsonPath("$.endereco").value(DEFAULT_ENDERECO))
            .andExpect(jsonPath("$.telefone").value(DEFAULT_TELEFONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getNonExistingAluno() throws Exception {
        // Get the aluno
        restAlunoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAluno() throws Exception {
        // Initialize the database
        insertedAluno = alunoRepository.saveAndFlush(aluno);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aluno
        Aluno updatedAluno = alunoRepository.findById(aluno.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAluno are not directly saved in db
        em.detach(updatedAluno);
        updatedAluno
            .nome(UPDATED_NOME)
            .dataDeNascimento(UPDATED_DATA_DE_NASCIMENTO)
            .cpf(UPDATED_CPF)
            .endereco(UPDATED_ENDERECO)
            .telefone(UPDATED_TELEFONE)
            .email(UPDATED_EMAIL);
        AlunoDTO alunoDTO = alunoMapper.toDto(updatedAluno);

        restAlunoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alunoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alunoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Aluno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlunoToMatchAllProperties(updatedAluno);
    }

    @Test
    @Transactional
    void putNonExistingAluno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aluno.setId(longCount.incrementAndGet());

        // Create the Aluno
        AlunoDTO alunoDTO = alunoMapper.toDto(aluno);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlunoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alunoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alunoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Aluno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAluno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aluno.setId(longCount.incrementAndGet());

        // Create the Aluno
        AlunoDTO alunoDTO = alunoMapper.toDto(aluno);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlunoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alunoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Aluno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAluno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aluno.setId(longCount.incrementAndGet());

        // Create the Aluno
        AlunoDTO alunoDTO = alunoMapper.toDto(aluno);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlunoMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alunoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Aluno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlunoWithPatch() throws Exception {
        // Initialize the database
        insertedAluno = alunoRepository.saveAndFlush(aluno);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aluno using partial update
        Aluno partialUpdatedAluno = new Aluno();
        partialUpdatedAluno.setId(aluno.getId());

        partialUpdatedAluno.dataDeNascimento(UPDATED_DATA_DE_NASCIMENTO).cpf(UPDATED_CPF).endereco(UPDATED_ENDERECO);

        restAlunoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAluno.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAluno))
            )
            .andExpect(status().isOk());

        // Validate the Aluno in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlunoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAluno, aluno), getPersistedAluno(aluno));
    }

    @Test
    @Transactional
    void fullUpdateAlunoWithPatch() throws Exception {
        // Initialize the database
        insertedAluno = alunoRepository.saveAndFlush(aluno);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aluno using partial update
        Aluno partialUpdatedAluno = new Aluno();
        partialUpdatedAluno.setId(aluno.getId());

        partialUpdatedAluno
            .nome(UPDATED_NOME)
            .dataDeNascimento(UPDATED_DATA_DE_NASCIMENTO)
            .cpf(UPDATED_CPF)
            .endereco(UPDATED_ENDERECO)
            .telefone(UPDATED_TELEFONE)
            .email(UPDATED_EMAIL);

        restAlunoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAluno.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAluno))
            )
            .andExpect(status().isOk());

        // Validate the Aluno in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlunoUpdatableFieldsEquals(partialUpdatedAluno, getPersistedAluno(partialUpdatedAluno));
    }

    @Test
    @Transactional
    void patchNonExistingAluno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aluno.setId(longCount.incrementAndGet());

        // Create the Aluno
        AlunoDTO alunoDTO = alunoMapper.toDto(aluno);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlunoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alunoDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alunoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Aluno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAluno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aluno.setId(longCount.incrementAndGet());

        // Create the Aluno
        AlunoDTO alunoDTO = alunoMapper.toDto(aluno);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlunoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alunoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Aluno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAluno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aluno.setId(longCount.incrementAndGet());

        // Create the Aluno
        AlunoDTO alunoDTO = alunoMapper.toDto(aluno);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlunoMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alunoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Aluno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAluno() throws Exception {
        // Initialize the database
        insertedAluno = alunoRepository.saveAndFlush(aluno);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the aluno
        restAlunoMockMvc
            .perform(delete(ENTITY_API_URL_ID, aluno.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alunoRepository.count();
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

    protected Aluno getPersistedAluno(Aluno aluno) {
        return alunoRepository.findById(aluno.getId()).orElseThrow();
    }

    protected void assertPersistedAlunoToMatchAllProperties(Aluno expectedAluno) {
        assertAlunoAllPropertiesEquals(expectedAluno, getPersistedAluno(expectedAluno));
    }

    protected void assertPersistedAlunoToMatchUpdatableProperties(Aluno expectedAluno) {
        assertAlunoAllUpdatablePropertiesEquals(expectedAluno, getPersistedAluno(expectedAluno));
    }
}

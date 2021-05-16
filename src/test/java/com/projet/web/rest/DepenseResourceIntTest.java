package com.projet.web.rest;

import com.projet.PfeApp;

import com.projet.domain.Depense;
import com.projet.domain.Operation;
import com.projet.domain.Fournisseur;
import com.projet.domain.Compte;
import com.projet.domain.Mode_de_paiment;
import com.projet.repository.DepenseRepository;
import com.projet.repository.search.DepenseSearchRepository;
import com.projet.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DepenseResource REST controller.
 *
 * @see DepenseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PfeApp.class)
public class DepenseResourceIntTest {

    private static final LocalDate DEFAULT_DATE_DEPENSE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEPENSE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_ECHEANCE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ECHEANCE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVATION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_DOCUMENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DOCUMENT = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_DOCUMENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DOCUMENT_CONTENT_TYPE = "image/png";

    @Autowired
    private DepenseRepository depenseRepository;

    @Autowired
    private DepenseSearchRepository depenseSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDepenseMockMvc;

    private Depense depense;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DepenseResource depenseResource = new DepenseResource(depenseRepository, depenseSearchRepository);
        this.restDepenseMockMvc = MockMvcBuilders.standaloneSetup(depenseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Depense createEntity(EntityManager em) {
        Depense depense = new Depense()
            .date_depense(DEFAULT_DATE_DEPENSE)
            .date_echeance(DEFAULT_DATE_ECHEANCE)
            .reference(DEFAULT_REFERENCE)
            .observation(DEFAULT_OBSERVATION)
            .document(DEFAULT_DOCUMENT)
            .documentContentType(DEFAULT_DOCUMENT_CONTENT_TYPE);
        // Add required entity
        Operation operation = OperationResourceIntTest.createEntity(em);
        em.persist(operation);
        em.flush();
        depense.setOperation(operation);
        // Add required entity
        Fournisseur fournisseur = FournisseurResourceIntTest.createEntity(em);
        em.persist(fournisseur);
        em.flush();
        depense.setFournisseur(fournisseur);
        // Add required entity
        Compte compte = CompteResourceIntTest.createEntity(em);
        em.persist(compte);
        em.flush();
        depense.setCompte(compte);
        // Add required entity
        Mode_de_paiment mode_de_paiment = Mode_de_paimentResourceIntTest.createEntity(em);
        em.persist(mode_de_paiment);
        em.flush();
        depense.setMode_de_paiment(mode_de_paiment);
        return depense;
    }

    @Before
    public void initTest() {
        depenseSearchRepository.deleteAll();
        depense = createEntity(em);
    }

    @Test
    @Transactional
    public void createDepense() throws Exception {
        int databaseSizeBeforeCreate = depenseRepository.findAll().size();

        // Create the Depense
        restDepenseMockMvc.perform(post("/api/depenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depense)))
            .andExpect(status().isCreated());

        // Validate the Depense in the database
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeCreate + 1);
        Depense testDepense = depenseList.get(depenseList.size() - 1);
        assertThat(testDepense.getDate_depense()).isEqualTo(DEFAULT_DATE_DEPENSE);
        assertThat(testDepense.getDate_echeance()).isEqualTo(DEFAULT_DATE_ECHEANCE);
        assertThat(testDepense.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testDepense.getObservation()).isEqualTo(DEFAULT_OBSERVATION);
        assertThat(testDepense.getDocument()).isEqualTo(DEFAULT_DOCUMENT);
        assertThat(testDepense.getDocumentContentType()).isEqualTo(DEFAULT_DOCUMENT_CONTENT_TYPE);

        // Validate the Depense in Elasticsearch
        Depense depenseEs = depenseSearchRepository.findOne(testDepense.getId());
        assertThat(depenseEs).isEqualToComparingFieldByField(testDepense);
    }

    @Test
    @Transactional
    public void createDepenseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = depenseRepository.findAll().size();

        // Create the Depense with an existing ID
        depense.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepenseMockMvc.perform(post("/api/depenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depense)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDate_depenseIsRequired() throws Exception {
        int databaseSizeBeforeTest = depenseRepository.findAll().size();
        // set the field null
        depense.setDate_depense(null);

        // Create the Depense, which fails.

        restDepenseMockMvc.perform(post("/api/depenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depense)))
            .andExpect(status().isBadRequest());

        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDate_echeanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = depenseRepository.findAll().size();
        // set the field null
        depense.setDate_echeance(null);

        // Create the Depense, which fails.

        restDepenseMockMvc.perform(post("/api/depenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depense)))
            .andExpect(status().isBadRequest());

        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReferenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = depenseRepository.findAll().size();
        // set the field null
        depense.setReference(null);

        // Create the Depense, which fails.

        restDepenseMockMvc.perform(post("/api/depenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depense)))
            .andExpect(status().isBadRequest());

        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkObservationIsRequired() throws Exception {
        int databaseSizeBeforeTest = depenseRepository.findAll().size();
        // set the field null
        depense.setObservation(null);

        // Create the Depense, which fails.

        restDepenseMockMvc.perform(post("/api/depenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depense)))
            .andExpect(status().isBadRequest());

        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDocumentIsRequired() throws Exception {
        int databaseSizeBeforeTest = depenseRepository.findAll().size();
        // set the field null
        depense.setDocument(null);

        // Create the Depense, which fails.

        restDepenseMockMvc.perform(post("/api/depenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depense)))
            .andExpect(status().isBadRequest());

        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDepenses() throws Exception {
        // Initialize the database
        depenseRepository.saveAndFlush(depense);

        // Get all the depenseList
        restDepenseMockMvc.perform(get("/api/depenses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depense.getId().intValue())))
            .andExpect(jsonPath("$.[*].date_depense").value(hasItem(DEFAULT_DATE_DEPENSE.toString())))
            .andExpect(jsonPath("$.[*].date_echeance").value(hasItem(DEFAULT_DATE_ECHEANCE.toString())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE.toString())))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION.toString())))
            .andExpect(jsonPath("$.[*].documentContentType").value(hasItem(DEFAULT_DOCUMENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].document").value(hasItem(Base64Utils.encodeToString(DEFAULT_DOCUMENT))));
    }

    @Test
    @Transactional
    public void getDepense() throws Exception {
        // Initialize the database
        depenseRepository.saveAndFlush(depense);

        // Get the depense
        restDepenseMockMvc.perform(get("/api/depenses/{id}", depense.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(depense.getId().intValue()))
            .andExpect(jsonPath("$.date_depense").value(DEFAULT_DATE_DEPENSE.toString()))
            .andExpect(jsonPath("$.date_echeance").value(DEFAULT_DATE_ECHEANCE.toString()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE.toString()))
            .andExpect(jsonPath("$.observation").value(DEFAULT_OBSERVATION.toString()))
            .andExpect(jsonPath("$.documentContentType").value(DEFAULT_DOCUMENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.document").value(Base64Utils.encodeToString(DEFAULT_DOCUMENT)));
    }

    @Test
    @Transactional
    public void getNonExistingDepense() throws Exception {
        // Get the depense
        restDepenseMockMvc.perform(get("/api/depenses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDepense() throws Exception {
        // Initialize the database
        depenseRepository.saveAndFlush(depense);
        depenseSearchRepository.save(depense);
        int databaseSizeBeforeUpdate = depenseRepository.findAll().size();

        // Update the depense
        Depense updatedDepense = depenseRepository.findOne(depense.getId());
        updatedDepense
            .date_depense(UPDATED_DATE_DEPENSE)
            .date_echeance(UPDATED_DATE_ECHEANCE)
            .reference(UPDATED_REFERENCE)
            .observation(UPDATED_OBSERVATION)
            .document(UPDATED_DOCUMENT)
            .documentContentType(UPDATED_DOCUMENT_CONTENT_TYPE);

        restDepenseMockMvc.perform(put("/api/depenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDepense)))
            .andExpect(status().isOk());

        // Validate the Depense in the database
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeUpdate);
        Depense testDepense = depenseList.get(depenseList.size() - 1);
        assertThat(testDepense.getDate_depense()).isEqualTo(UPDATED_DATE_DEPENSE);
        assertThat(testDepense.getDate_echeance()).isEqualTo(UPDATED_DATE_ECHEANCE);
        assertThat(testDepense.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testDepense.getObservation()).isEqualTo(UPDATED_OBSERVATION);
        assertThat(testDepense.getDocument()).isEqualTo(UPDATED_DOCUMENT);
        assertThat(testDepense.getDocumentContentType()).isEqualTo(UPDATED_DOCUMENT_CONTENT_TYPE);

        // Validate the Depense in Elasticsearch
        Depense depenseEs = depenseSearchRepository.findOne(testDepense.getId());
        assertThat(depenseEs).isEqualToComparingFieldByField(testDepense);
    }

    @Test
    @Transactional
    public void updateNonExistingDepense() throws Exception {
        int databaseSizeBeforeUpdate = depenseRepository.findAll().size();

        // Create the Depense

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDepenseMockMvc.perform(put("/api/depenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depense)))
            .andExpect(status().isCreated());

        // Validate the Depense in the database
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDepense() throws Exception {
        // Initialize the database
        depenseRepository.saveAndFlush(depense);
        depenseSearchRepository.save(depense);
        int databaseSizeBeforeDelete = depenseRepository.findAll().size();

        // Get the depense
        restDepenseMockMvc.perform(delete("/api/depenses/{id}", depense.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean depenseExistsInEs = depenseSearchRepository.exists(depense.getId());
        assertThat(depenseExistsInEs).isFalse();

        // Validate the database is empty
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDepense() throws Exception {
        // Initialize the database
        depenseRepository.saveAndFlush(depense);
        depenseSearchRepository.save(depense);

        // Search the depense
        restDepenseMockMvc.perform(get("/api/_search/depenses?query=id:" + depense.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depense.getId().intValue())))
            .andExpect(jsonPath("$.[*].date_depense").value(hasItem(DEFAULT_DATE_DEPENSE.toString())))
            .andExpect(jsonPath("$.[*].date_echeance").value(hasItem(DEFAULT_DATE_ECHEANCE.toString())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE.toString())))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION.toString())))
            .andExpect(jsonPath("$.[*].documentContentType").value(hasItem(DEFAULT_DOCUMENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].document").value(hasItem(Base64Utils.encodeToString(DEFAULT_DOCUMENT))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Depense.class);
    }
}

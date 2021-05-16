package com.projet.web.rest;

import com.projet.PfeApp;

import com.projet.domain.Devis;
import com.projet.domain.Client;
import com.projet.domain.Mode_de_paiment;
import com.projet.repository.DevisRepository;
import com.projet.repository.search.DevisSearchRepository;
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

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DevisResource REST controller.
 *
 * @see DevisResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PfeApp.class)
public class DevisResourceIntTest {

    private static final Integer DEFAULT_NUMERO_DEVIS = 1;
    private static final Integer UPDATED_NUMERO_DEVIS = 2;

    private static final LocalDate DEFAULT_DATE_DEVIS = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEVIS = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_MONTANT = 1D;
    private static final Double UPDATED_MONTANT = 2D;

    @Autowired
    private DevisRepository devisRepository;

    @Autowired
    private DevisSearchRepository devisSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDevisMockMvc;

    private Devis devis;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DevisResource devisResource = new DevisResource(devisRepository, devisSearchRepository);
        this.restDevisMockMvc = MockMvcBuilders.standaloneSetup(devisResource)
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
    public static Devis createEntity(EntityManager em) {
        Devis devis = new Devis()
            .numero_Devis(DEFAULT_NUMERO_DEVIS)
            .date_Devis(DEFAULT_DATE_DEVIS)
            .montant(DEFAULT_MONTANT);
        // Add required entity
        Client client = ClientResourceIntTest.createEntity(em);
        em.persist(client);
        em.flush();
        devis.setClient(client);
        // Add required entity
        Mode_de_paiment mode_de_paiment = Mode_de_paimentResourceIntTest.createEntity(em);
        em.persist(mode_de_paiment);
        em.flush();
        devis.setMode_de_paiment(mode_de_paiment);
        return devis;
    }

    @Before
    public void initTest() {
        devisSearchRepository.deleteAll();
        devis = createEntity(em);
    }

    @Test
    @Transactional
    public void createDevis() throws Exception {
        int databaseSizeBeforeCreate = devisRepository.findAll().size();

        // Create the Devis
        restDevisMockMvc.perform(post("/api/devis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(devis)))
            .andExpect(status().isCreated());

        // Validate the Devis in the database
        List<Devis> devisList = devisRepository.findAll();
        assertThat(devisList).hasSize(databaseSizeBeforeCreate + 1);
        Devis testDevis = devisList.get(devisList.size() - 1);
        assertThat(testDevis.getNumero_Devis()).isEqualTo(DEFAULT_NUMERO_DEVIS);
        assertThat(testDevis.getDate_Devis()).isEqualTo(DEFAULT_DATE_DEVIS);
        assertThat(testDevis.getMontant()).isEqualTo(DEFAULT_MONTANT);

        // Validate the Devis in Elasticsearch
        Devis devisEs = devisSearchRepository.findOne(testDevis.getId());
        assertThat(devisEs).isEqualToComparingFieldByField(testDevis);
    }

    @Test
    @Transactional
    public void createDevisWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = devisRepository.findAll().size();

        // Create the Devis with an existing ID
        devis.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDevisMockMvc.perform(post("/api/devis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(devis)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Devis> devisList = devisRepository.findAll();
        assertThat(devisList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNumero_DevisIsRequired() throws Exception {
        int databaseSizeBeforeTest = devisRepository.findAll().size();
        // set the field null
        devis.setNumero_Devis(null);

        // Create the Devis, which fails.

        restDevisMockMvc.perform(post("/api/devis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(devis)))
            .andExpect(status().isBadRequest());

        List<Devis> devisList = devisRepository.findAll();
        assertThat(devisList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDate_DevisIsRequired() throws Exception {
        int databaseSizeBeforeTest = devisRepository.findAll().size();
        // set the field null
        devis.setDate_Devis(null);

        // Create the Devis, which fails.

        restDevisMockMvc.perform(post("/api/devis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(devis)))
            .andExpect(status().isBadRequest());

        List<Devis> devisList = devisRepository.findAll();
        assertThat(devisList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMontantIsRequired() throws Exception {
        int databaseSizeBeforeTest = devisRepository.findAll().size();
        // set the field null
        devis.setMontant(null);

        // Create the Devis, which fails.

        restDevisMockMvc.perform(post("/api/devis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(devis)))
            .andExpect(status().isBadRequest());

        List<Devis> devisList = devisRepository.findAll();
        assertThat(devisList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDevis() throws Exception {
        // Initialize the database
        devisRepository.saveAndFlush(devis);

        // Get all the devisList
        restDevisMockMvc.perform(get("/api/devis?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(devis.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero_Devis").value(hasItem(DEFAULT_NUMERO_DEVIS)))
            .andExpect(jsonPath("$.[*].date_Devis").value(hasItem(DEFAULT_DATE_DEVIS.toString())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())));
    }

    @Test
    @Transactional
    public void getDevis() throws Exception {
        // Initialize the database
        devisRepository.saveAndFlush(devis);

        // Get the devis
        restDevisMockMvc.perform(get("/api/devis/{id}", devis.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(devis.getId().intValue()))
            .andExpect(jsonPath("$.numero_Devis").value(DEFAULT_NUMERO_DEVIS))
            .andExpect(jsonPath("$.date_Devis").value(DEFAULT_DATE_DEVIS.toString()))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDevis() throws Exception {
        // Get the devis
        restDevisMockMvc.perform(get("/api/devis/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDevis() throws Exception {
        // Initialize the database
        devisRepository.saveAndFlush(devis);
        devisSearchRepository.save(devis);
        int databaseSizeBeforeUpdate = devisRepository.findAll().size();

        // Update the devis
        Devis updatedDevis = devisRepository.findOne(devis.getId());
        updatedDevis
            .numero_Devis(UPDATED_NUMERO_DEVIS)
            .date_Devis(UPDATED_DATE_DEVIS)
            .montant(UPDATED_MONTANT);

        restDevisMockMvc.perform(put("/api/devis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDevis)))
            .andExpect(status().isOk());

        // Validate the Devis in the database
        List<Devis> devisList = devisRepository.findAll();
        assertThat(devisList).hasSize(databaseSizeBeforeUpdate);
        Devis testDevis = devisList.get(devisList.size() - 1);
        assertThat(testDevis.getNumero_Devis()).isEqualTo(UPDATED_NUMERO_DEVIS);
        assertThat(testDevis.getDate_Devis()).isEqualTo(UPDATED_DATE_DEVIS);
        assertThat(testDevis.getMontant()).isEqualTo(UPDATED_MONTANT);

        // Validate the Devis in Elasticsearch
        Devis devisEs = devisSearchRepository.findOne(testDevis.getId());
        assertThat(devisEs).isEqualToComparingFieldByField(testDevis);
    }

    @Test
    @Transactional
    public void updateNonExistingDevis() throws Exception {
        int databaseSizeBeforeUpdate = devisRepository.findAll().size();

        // Create the Devis

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDevisMockMvc.perform(put("/api/devis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(devis)))
            .andExpect(status().isCreated());

        // Validate the Devis in the database
        List<Devis> devisList = devisRepository.findAll();
        assertThat(devisList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDevis() throws Exception {
        // Initialize the database
        devisRepository.saveAndFlush(devis);
        devisSearchRepository.save(devis);
        int databaseSizeBeforeDelete = devisRepository.findAll().size();

        // Get the devis
        restDevisMockMvc.perform(delete("/api/devis/{id}", devis.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean devisExistsInEs = devisSearchRepository.exists(devis.getId());
        assertThat(devisExistsInEs).isFalse();

        // Validate the database is empty
        List<Devis> devisList = devisRepository.findAll();
        assertThat(devisList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDevis() throws Exception {
        // Initialize the database
        devisRepository.saveAndFlush(devis);
        devisSearchRepository.save(devis);

        // Search the devis
        restDevisMockMvc.perform(get("/api/_search/devis?query=id:" + devis.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(devis.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero_Devis").value(hasItem(DEFAULT_NUMERO_DEVIS)))
            .andExpect(jsonPath("$.[*].date_Devis").value(hasItem(DEFAULT_DATE_DEVIS.toString())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Devis.class);
    }
}

package com.projet.web.rest;

import com.projet.PfeApp;

import com.projet.domain.Avoirs;
import com.projet.domain.Facture;
import com.projet.repository.AvoirsRepository;
import com.projet.repository.search.AvoirsSearchRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AvoirsResource REST controller.
 *
 * @see AvoirsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PfeApp.class)
public class AvoirsResourceIntTest {

    private static final Integer DEFAULT_NUMERO_FACTURE_AVOIRS = 1;
    private static final Integer UPDATED_NUMERO_FACTURE_AVOIRS = 2;

    @Autowired
    private AvoirsRepository avoirsRepository;

    @Autowired
    private AvoirsSearchRepository avoirsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAvoirsMockMvc;

    private Avoirs avoirs;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AvoirsResource avoirsResource = new AvoirsResource(avoirsRepository, avoirsSearchRepository);
        this.restAvoirsMockMvc = MockMvcBuilders.standaloneSetup(avoirsResource)
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
    public static Avoirs createEntity(EntityManager em) {
        Avoirs avoirs = new Avoirs()
            .numero_factureAvoirs(DEFAULT_NUMERO_FACTURE_AVOIRS);
        // Add required entity
        Facture facture = FactureResourceIntTest.createEntity(em);
        em.persist(facture);
        em.flush();
        avoirs.setFacture(facture);
        return avoirs;
    }

    @Before
    public void initTest() {
        avoirsSearchRepository.deleteAll();
        avoirs = createEntity(em);
    }

    @Test
    @Transactional
    public void createAvoirs() throws Exception {
        int databaseSizeBeforeCreate = avoirsRepository.findAll().size();

        // Create the Avoirs
        restAvoirsMockMvc.perform(post("/api/avoirs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(avoirs)))
            .andExpect(status().isCreated());

        // Validate the Avoirs in the database
        List<Avoirs> avoirsList = avoirsRepository.findAll();
        assertThat(avoirsList).hasSize(databaseSizeBeforeCreate + 1);
        Avoirs testAvoirs = avoirsList.get(avoirsList.size() - 1);
        assertThat(testAvoirs.getNumero_factureAvoirs()).isEqualTo(DEFAULT_NUMERO_FACTURE_AVOIRS);

        // Validate the Avoirs in Elasticsearch
        Avoirs avoirsEs = avoirsSearchRepository.findOne(testAvoirs.getId());
        assertThat(avoirsEs).isEqualToComparingFieldByField(testAvoirs);
    }

    @Test
    @Transactional
    public void createAvoirsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = avoirsRepository.findAll().size();

        // Create the Avoirs with an existing ID
        avoirs.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAvoirsMockMvc.perform(post("/api/avoirs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(avoirs)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Avoirs> avoirsList = avoirsRepository.findAll();
        assertThat(avoirsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNumero_factureAvoirsIsRequired() throws Exception {
        int databaseSizeBeforeTest = avoirsRepository.findAll().size();
        // set the field null
        avoirs.setNumero_factureAvoirs(null);

        // Create the Avoirs, which fails.

        restAvoirsMockMvc.perform(post("/api/avoirs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(avoirs)))
            .andExpect(status().isBadRequest());

        List<Avoirs> avoirsList = avoirsRepository.findAll();
        assertThat(avoirsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAvoirs() throws Exception {
        // Initialize the database
        avoirsRepository.saveAndFlush(avoirs);

        // Get all the avoirsList
        restAvoirsMockMvc.perform(get("/api/avoirs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(avoirs.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero_factureAvoirs").value(hasItem(DEFAULT_NUMERO_FACTURE_AVOIRS)));
    }

    @Test
    @Transactional
    public void getAvoirs() throws Exception {
        // Initialize the database
        avoirsRepository.saveAndFlush(avoirs);

        // Get the avoirs
        restAvoirsMockMvc.perform(get("/api/avoirs/{id}", avoirs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(avoirs.getId().intValue()))
            .andExpect(jsonPath("$.numero_factureAvoirs").value(DEFAULT_NUMERO_FACTURE_AVOIRS));
    }

    @Test
    @Transactional
    public void getNonExistingAvoirs() throws Exception {
        // Get the avoirs
        restAvoirsMockMvc.perform(get("/api/avoirs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAvoirs() throws Exception {
        // Initialize the database
        avoirsRepository.saveAndFlush(avoirs);
        avoirsSearchRepository.save(avoirs);
        int databaseSizeBeforeUpdate = avoirsRepository.findAll().size();

        // Update the avoirs
        Avoirs updatedAvoirs = avoirsRepository.findOne(avoirs.getId());
        updatedAvoirs
            .numero_factureAvoirs(UPDATED_NUMERO_FACTURE_AVOIRS);

        restAvoirsMockMvc.perform(put("/api/avoirs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAvoirs)))
            .andExpect(status().isOk());

        // Validate the Avoirs in the database
        List<Avoirs> avoirsList = avoirsRepository.findAll();
        assertThat(avoirsList).hasSize(databaseSizeBeforeUpdate);
        Avoirs testAvoirs = avoirsList.get(avoirsList.size() - 1);
        assertThat(testAvoirs.getNumero_factureAvoirs()).isEqualTo(UPDATED_NUMERO_FACTURE_AVOIRS);

        // Validate the Avoirs in Elasticsearch
        Avoirs avoirsEs = avoirsSearchRepository.findOne(testAvoirs.getId());
        assertThat(avoirsEs).isEqualToComparingFieldByField(testAvoirs);
    }

    @Test
    @Transactional
    public void updateNonExistingAvoirs() throws Exception {
        int databaseSizeBeforeUpdate = avoirsRepository.findAll().size();

        // Create the Avoirs

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAvoirsMockMvc.perform(put("/api/avoirs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(avoirs)))
            .andExpect(status().isCreated());

        // Validate the Avoirs in the database
        List<Avoirs> avoirsList = avoirsRepository.findAll();
        assertThat(avoirsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAvoirs() throws Exception {
        // Initialize the database
        avoirsRepository.saveAndFlush(avoirs);
        avoirsSearchRepository.save(avoirs);
        int databaseSizeBeforeDelete = avoirsRepository.findAll().size();

        // Get the avoirs
        restAvoirsMockMvc.perform(delete("/api/avoirs/{id}", avoirs.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean avoirsExistsInEs = avoirsSearchRepository.exists(avoirs.getId());
        assertThat(avoirsExistsInEs).isFalse();

        // Validate the database is empty
        List<Avoirs> avoirsList = avoirsRepository.findAll();
        assertThat(avoirsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAvoirs() throws Exception {
        // Initialize the database
        avoirsRepository.saveAndFlush(avoirs);
        avoirsSearchRepository.save(avoirs);

        // Search the avoirs
        restAvoirsMockMvc.perform(get("/api/_search/avoirs?query=id:" + avoirs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(avoirs.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero_factureAvoirs").value(hasItem(DEFAULT_NUMERO_FACTURE_AVOIRS)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Avoirs.class);
    }
}

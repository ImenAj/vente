package com.projet.web.rest;

import com.projet.PfeApp;

import com.projet.domain.Bons_de_livrison;
import com.projet.domain.Condition_de_reglement;
import com.projet.domain.Client;
import com.projet.domain.Mode_de_paiment;
import com.projet.repository.Bons_de_livrisonRepository;
import com.projet.repository.search.Bons_de_livrisonSearchRepository;
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
 * Test class for the Bons_de_livrisonResource REST controller.
 *
 * @see Bons_de_livrisonResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PfeApp.class)
public class Bons_de_livrisonResourceIntTest {

    private static final LocalDate DEFAULT_DATE_LIVRAISON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_LIVRAISON = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_OBJET = "AAAAAAAAAA";
    private static final String UPDATED_OBJET = "BBBBBBBBBB";

    @Autowired
    private Bons_de_livrisonRepository bons_de_livrisonRepository;

    @Autowired
    private Bons_de_livrisonSearchRepository bons_de_livrisonSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBons_de_livrisonMockMvc;

    private Bons_de_livrison bons_de_livrison;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Bons_de_livrisonResource bons_de_livrisonResource = new Bons_de_livrisonResource(bons_de_livrisonRepository, bons_de_livrisonSearchRepository);
        this.restBons_de_livrisonMockMvc = MockMvcBuilders.standaloneSetup(bons_de_livrisonResource)
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
    public static Bons_de_livrison createEntity(EntityManager em) {
        Bons_de_livrison bons_de_livrison = new Bons_de_livrison()
            .date_livraison(DEFAULT_DATE_LIVRAISON)
            .objet(DEFAULT_OBJET);
        // Add required entity
        Condition_de_reglement condition_de_reglement = Condition_de_reglementResourceIntTest.createEntity(em);
        em.persist(condition_de_reglement);
        em.flush();
        bons_de_livrison.setCondition_de_reglement(condition_de_reglement);
        // Add required entity
        Client client = ClientResourceIntTest.createEntity(em);
        em.persist(client);
        em.flush();
        bons_de_livrison.setClient(client);
        // Add required entity
        Mode_de_paiment mode_de_paiment = Mode_de_paimentResourceIntTest.createEntity(em);
        em.persist(mode_de_paiment);
        em.flush();
        bons_de_livrison.setMode_de_paiment(mode_de_paiment);
        return bons_de_livrison;
    }

    @Before
    public void initTest() {
        bons_de_livrisonSearchRepository.deleteAll();
        bons_de_livrison = createEntity(em);
    }

    @Test
    @Transactional
    public void createBons_de_livrison() throws Exception {
        int databaseSizeBeforeCreate = bons_de_livrisonRepository.findAll().size();

        // Create the Bons_de_livrison
        restBons_de_livrisonMockMvc.perform(post("/api/bons-de-livrisons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bons_de_livrison)))
            .andExpect(status().isCreated());

        // Validate the Bons_de_livrison in the database
        List<Bons_de_livrison> bons_de_livrisonList = bons_de_livrisonRepository.findAll();
        assertThat(bons_de_livrisonList).hasSize(databaseSizeBeforeCreate + 1);
        Bons_de_livrison testBons_de_livrison = bons_de_livrisonList.get(bons_de_livrisonList.size() - 1);
        assertThat(testBons_de_livrison.getDate_livraison()).isEqualTo(DEFAULT_DATE_LIVRAISON);
        assertThat(testBons_de_livrison.getObjet()).isEqualTo(DEFAULT_OBJET);

        // Validate the Bons_de_livrison in Elasticsearch
        Bons_de_livrison bons_de_livrisonEs = bons_de_livrisonSearchRepository.findOne(testBons_de_livrison.getId());
        assertThat(bons_de_livrisonEs).isEqualToComparingFieldByField(testBons_de_livrison);
    }

    @Test
    @Transactional
    public void createBons_de_livrisonWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bons_de_livrisonRepository.findAll().size();

        // Create the Bons_de_livrison with an existing ID
        bons_de_livrison.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBons_de_livrisonMockMvc.perform(post("/api/bons-de-livrisons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bons_de_livrison)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Bons_de_livrison> bons_de_livrisonList = bons_de_livrisonRepository.findAll();
        assertThat(bons_de_livrisonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDate_livraisonIsRequired() throws Exception {
        int databaseSizeBeforeTest = bons_de_livrisonRepository.findAll().size();
        // set the field null
        bons_de_livrison.setDate_livraison(null);

        // Create the Bons_de_livrison, which fails.

        restBons_de_livrisonMockMvc.perform(post("/api/bons-de-livrisons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bons_de_livrison)))
            .andExpect(status().isBadRequest());

        List<Bons_de_livrison> bons_de_livrisonList = bons_de_livrisonRepository.findAll();
        assertThat(bons_de_livrisonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBons_de_livrisons() throws Exception {
        // Initialize the database
        bons_de_livrisonRepository.saveAndFlush(bons_de_livrison);

        // Get all the bons_de_livrisonList
        restBons_de_livrisonMockMvc.perform(get("/api/bons-de-livrisons?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bons_de_livrison.getId().intValue())))
            .andExpect(jsonPath("$.[*].date_livraison").value(hasItem(DEFAULT_DATE_LIVRAISON.toString())))
            .andExpect(jsonPath("$.[*].objet").value(hasItem(DEFAULT_OBJET.toString())));
    }

    @Test
    @Transactional
    public void getBons_de_livrison() throws Exception {
        // Initialize the database
        bons_de_livrisonRepository.saveAndFlush(bons_de_livrison);

        // Get the bons_de_livrison
        restBons_de_livrisonMockMvc.perform(get("/api/bons-de-livrisons/{id}", bons_de_livrison.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bons_de_livrison.getId().intValue()))
            .andExpect(jsonPath("$.date_livraison").value(DEFAULT_DATE_LIVRAISON.toString()))
            .andExpect(jsonPath("$.objet").value(DEFAULT_OBJET.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBons_de_livrison() throws Exception {
        // Get the bons_de_livrison
        restBons_de_livrisonMockMvc.perform(get("/api/bons-de-livrisons/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBons_de_livrison() throws Exception {
        // Initialize the database
        bons_de_livrisonRepository.saveAndFlush(bons_de_livrison);
        bons_de_livrisonSearchRepository.save(bons_de_livrison);
        int databaseSizeBeforeUpdate = bons_de_livrisonRepository.findAll().size();

        // Update the bons_de_livrison
        Bons_de_livrison updatedBons_de_livrison = bons_de_livrisonRepository.findOne(bons_de_livrison.getId());
        updatedBons_de_livrison
            .date_livraison(UPDATED_DATE_LIVRAISON)
            .objet(UPDATED_OBJET);

        restBons_de_livrisonMockMvc.perform(put("/api/bons-de-livrisons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBons_de_livrison)))
            .andExpect(status().isOk());

        // Validate the Bons_de_livrison in the database
        List<Bons_de_livrison> bons_de_livrisonList = bons_de_livrisonRepository.findAll();
        assertThat(bons_de_livrisonList).hasSize(databaseSizeBeforeUpdate);
        Bons_de_livrison testBons_de_livrison = bons_de_livrisonList.get(bons_de_livrisonList.size() - 1);
        assertThat(testBons_de_livrison.getDate_livraison()).isEqualTo(UPDATED_DATE_LIVRAISON);
        assertThat(testBons_de_livrison.getObjet()).isEqualTo(UPDATED_OBJET);

        // Validate the Bons_de_livrison in Elasticsearch
        Bons_de_livrison bons_de_livrisonEs = bons_de_livrisonSearchRepository.findOne(testBons_de_livrison.getId());
        assertThat(bons_de_livrisonEs).isEqualToComparingFieldByField(testBons_de_livrison);
    }

    @Test
    @Transactional
    public void updateNonExistingBons_de_livrison() throws Exception {
        int databaseSizeBeforeUpdate = bons_de_livrisonRepository.findAll().size();

        // Create the Bons_de_livrison

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBons_de_livrisonMockMvc.perform(put("/api/bons-de-livrisons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bons_de_livrison)))
            .andExpect(status().isCreated());

        // Validate the Bons_de_livrison in the database
        List<Bons_de_livrison> bons_de_livrisonList = bons_de_livrisonRepository.findAll();
        assertThat(bons_de_livrisonList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBons_de_livrison() throws Exception {
        // Initialize the database
        bons_de_livrisonRepository.saveAndFlush(bons_de_livrison);
        bons_de_livrisonSearchRepository.save(bons_de_livrison);
        int databaseSizeBeforeDelete = bons_de_livrisonRepository.findAll().size();

        // Get the bons_de_livrison
        restBons_de_livrisonMockMvc.perform(delete("/api/bons-de-livrisons/{id}", bons_de_livrison.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean bons_de_livrisonExistsInEs = bons_de_livrisonSearchRepository.exists(bons_de_livrison.getId());
        assertThat(bons_de_livrisonExistsInEs).isFalse();

        // Validate the database is empty
        List<Bons_de_livrison> bons_de_livrisonList = bons_de_livrisonRepository.findAll();
        assertThat(bons_de_livrisonList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBons_de_livrison() throws Exception {
        // Initialize the database
        bons_de_livrisonRepository.saveAndFlush(bons_de_livrison);
        bons_de_livrisonSearchRepository.save(bons_de_livrison);

        // Search the bons_de_livrison
        restBons_de_livrisonMockMvc.perform(get("/api/_search/bons-de-livrisons?query=id:" + bons_de_livrison.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bons_de_livrison.getId().intValue())))
            .andExpect(jsonPath("$.[*].date_livraison").value(hasItem(DEFAULT_DATE_LIVRAISON.toString())))
            .andExpect(jsonPath("$.[*].objet").value(hasItem(DEFAULT_OBJET.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bons_de_livrison.class);
    }
}

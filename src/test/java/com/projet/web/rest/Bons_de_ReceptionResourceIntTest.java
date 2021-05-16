package com.projet.web.rest;

import com.projet.PfeApp;

import com.projet.domain.Bons_de_Reception;
import com.projet.domain.Fournisseur;
import com.projet.domain.Condition_de_reglement;
import com.projet.repository.Bons_de_ReceptionRepository;
import com.projet.repository.search.Bons_de_ReceptionSearchRepository;
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
 * Test class for the Bons_de_ReceptionResource REST controller.
 *
 * @see Bons_de_ReceptionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PfeApp.class)
public class Bons_de_ReceptionResourceIntTest {

    private static final LocalDate DEFAULT_DATE_RECEPTION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_RECEPTION = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_OBJET = "AAAAAAAAAA";
    private static final String UPDATED_OBJET = "BBBBBBBBBB";

    @Autowired
    private Bons_de_ReceptionRepository bons_de_ReceptionRepository;

    @Autowired
    private Bons_de_ReceptionSearchRepository bons_de_ReceptionSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBons_de_ReceptionMockMvc;

    private Bons_de_Reception bons_de_Reception;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Bons_de_ReceptionResource bons_de_ReceptionResource = new Bons_de_ReceptionResource(bons_de_ReceptionRepository, bons_de_ReceptionSearchRepository);
        this.restBons_de_ReceptionMockMvc = MockMvcBuilders.standaloneSetup(bons_de_ReceptionResource)
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
    public static Bons_de_Reception createEntity(EntityManager em) {
        Bons_de_Reception bons_de_Reception = new Bons_de_Reception()
            .dateReception(DEFAULT_DATE_RECEPTION)
            .objet(DEFAULT_OBJET);
        // Add required entity
        Fournisseur fournisseur = FournisseurResourceIntTest.createEntity(em);
        em.persist(fournisseur);
        em.flush();
        bons_de_Reception.setFournisseur(fournisseur);
        // Add required entity
        Condition_de_reglement condition_de_reglement = Condition_de_reglementResourceIntTest.createEntity(em);
        em.persist(condition_de_reglement);
        em.flush();
        bons_de_Reception.setCondition_de_reglement(condition_de_reglement);
        return bons_de_Reception;
    }

    @Before
    public void initTest() {
        bons_de_ReceptionSearchRepository.deleteAll();
        bons_de_Reception = createEntity(em);
    }

    @Test
    @Transactional
    public void createBons_de_Reception() throws Exception {
        int databaseSizeBeforeCreate = bons_de_ReceptionRepository.findAll().size();

        // Create the Bons_de_Reception
        restBons_de_ReceptionMockMvc.perform(post("/api/bons-de-receptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bons_de_Reception)))
            .andExpect(status().isCreated());

        // Validate the Bons_de_Reception in the database
        List<Bons_de_Reception> bons_de_ReceptionList = bons_de_ReceptionRepository.findAll();
        assertThat(bons_de_ReceptionList).hasSize(databaseSizeBeforeCreate + 1);
        Bons_de_Reception testBons_de_Reception = bons_de_ReceptionList.get(bons_de_ReceptionList.size() - 1);
        assertThat(testBons_de_Reception.getDateReception()).isEqualTo(DEFAULT_DATE_RECEPTION);
        assertThat(testBons_de_Reception.getObjet()).isEqualTo(DEFAULT_OBJET);

        // Validate the Bons_de_Reception in Elasticsearch
        Bons_de_Reception bons_de_ReceptionEs = bons_de_ReceptionSearchRepository.findOne(testBons_de_Reception.getId());
        assertThat(bons_de_ReceptionEs).isEqualToComparingFieldByField(testBons_de_Reception);
    }

    @Test
    @Transactional
    public void createBons_de_ReceptionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bons_de_ReceptionRepository.findAll().size();

        // Create the Bons_de_Reception with an existing ID
        bons_de_Reception.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBons_de_ReceptionMockMvc.perform(post("/api/bons-de-receptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bons_de_Reception)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Bons_de_Reception> bons_de_ReceptionList = bons_de_ReceptionRepository.findAll();
        assertThat(bons_de_ReceptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDateReceptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = bons_de_ReceptionRepository.findAll().size();
        // set the field null
        bons_de_Reception.setDateReception(null);

        // Create the Bons_de_Reception, which fails.

        restBons_de_ReceptionMockMvc.perform(post("/api/bons-de-receptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bons_de_Reception)))
            .andExpect(status().isBadRequest());

        List<Bons_de_Reception> bons_de_ReceptionList = bons_de_ReceptionRepository.findAll();
        assertThat(bons_de_ReceptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBons_de_Receptions() throws Exception {
        // Initialize the database
        bons_de_ReceptionRepository.saveAndFlush(bons_de_Reception);

        // Get all the bons_de_ReceptionList
        restBons_de_ReceptionMockMvc.perform(get("/api/bons-de-receptions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bons_de_Reception.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateReception").value(hasItem(DEFAULT_DATE_RECEPTION.toString())))
            .andExpect(jsonPath("$.[*].objet").value(hasItem(DEFAULT_OBJET.toString())));
    }

    @Test
    @Transactional
    public void getBons_de_Reception() throws Exception {
        // Initialize the database
        bons_de_ReceptionRepository.saveAndFlush(bons_de_Reception);

        // Get the bons_de_Reception
        restBons_de_ReceptionMockMvc.perform(get("/api/bons-de-receptions/{id}", bons_de_Reception.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bons_de_Reception.getId().intValue()))
            .andExpect(jsonPath("$.dateReception").value(DEFAULT_DATE_RECEPTION.toString()))
            .andExpect(jsonPath("$.objet").value(DEFAULT_OBJET.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBons_de_Reception() throws Exception {
        // Get the bons_de_Reception
        restBons_de_ReceptionMockMvc.perform(get("/api/bons-de-receptions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBons_de_Reception() throws Exception {
        // Initialize the database
        bons_de_ReceptionRepository.saveAndFlush(bons_de_Reception);
        bons_de_ReceptionSearchRepository.save(bons_de_Reception);
        int databaseSizeBeforeUpdate = bons_de_ReceptionRepository.findAll().size();

        // Update the bons_de_Reception
        Bons_de_Reception updatedBons_de_Reception = bons_de_ReceptionRepository.findOne(bons_de_Reception.getId());
        updatedBons_de_Reception
            .dateReception(UPDATED_DATE_RECEPTION)
            .objet(UPDATED_OBJET);

        restBons_de_ReceptionMockMvc.perform(put("/api/bons-de-receptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBons_de_Reception)))
            .andExpect(status().isOk());

        // Validate the Bons_de_Reception in the database
        List<Bons_de_Reception> bons_de_ReceptionList = bons_de_ReceptionRepository.findAll();
        assertThat(bons_de_ReceptionList).hasSize(databaseSizeBeforeUpdate);
        Bons_de_Reception testBons_de_Reception = bons_de_ReceptionList.get(bons_de_ReceptionList.size() - 1);
        assertThat(testBons_de_Reception.getDateReception()).isEqualTo(UPDATED_DATE_RECEPTION);
        assertThat(testBons_de_Reception.getObjet()).isEqualTo(UPDATED_OBJET);

        // Validate the Bons_de_Reception in Elasticsearch
        Bons_de_Reception bons_de_ReceptionEs = bons_de_ReceptionSearchRepository.findOne(testBons_de_Reception.getId());
        assertThat(bons_de_ReceptionEs).isEqualToComparingFieldByField(testBons_de_Reception);
    }

    @Test
    @Transactional
    public void updateNonExistingBons_de_Reception() throws Exception {
        int databaseSizeBeforeUpdate = bons_de_ReceptionRepository.findAll().size();

        // Create the Bons_de_Reception

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBons_de_ReceptionMockMvc.perform(put("/api/bons-de-receptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bons_de_Reception)))
            .andExpect(status().isCreated());

        // Validate the Bons_de_Reception in the database
        List<Bons_de_Reception> bons_de_ReceptionList = bons_de_ReceptionRepository.findAll();
        assertThat(bons_de_ReceptionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBons_de_Reception() throws Exception {
        // Initialize the database
        bons_de_ReceptionRepository.saveAndFlush(bons_de_Reception);
        bons_de_ReceptionSearchRepository.save(bons_de_Reception);
        int databaseSizeBeforeDelete = bons_de_ReceptionRepository.findAll().size();

        // Get the bons_de_Reception
        restBons_de_ReceptionMockMvc.perform(delete("/api/bons-de-receptions/{id}", bons_de_Reception.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean bons_de_ReceptionExistsInEs = bons_de_ReceptionSearchRepository.exists(bons_de_Reception.getId());
        assertThat(bons_de_ReceptionExistsInEs).isFalse();

        // Validate the database is empty
        List<Bons_de_Reception> bons_de_ReceptionList = bons_de_ReceptionRepository.findAll();
        assertThat(bons_de_ReceptionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBons_de_Reception() throws Exception {
        // Initialize the database
        bons_de_ReceptionRepository.saveAndFlush(bons_de_Reception);
        bons_de_ReceptionSearchRepository.save(bons_de_Reception);

        // Search the bons_de_Reception
        restBons_de_ReceptionMockMvc.perform(get("/api/_search/bons-de-receptions?query=id:" + bons_de_Reception.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bons_de_Reception.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateReception").value(hasItem(DEFAULT_DATE_RECEPTION.toString())))
            .andExpect(jsonPath("$.[*].objet").value(hasItem(DEFAULT_OBJET.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bons_de_Reception.class);
    }
}

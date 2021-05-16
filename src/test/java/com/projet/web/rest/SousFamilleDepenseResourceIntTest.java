package com.projet.web.rest;

import com.projet.PfeApp;

import com.projet.domain.SousFamilleDepense;
import com.projet.domain.FamilleDepense;
import com.projet.repository.SousFamilleDepenseRepository;
import com.projet.repository.search.SousFamilleDepenseSearchRepository;
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
 * Test class for the SousFamilleDepenseResource REST controller.
 *
 * @see SousFamilleDepenseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PfeApp.class)
public class SousFamilleDepenseResourceIntTest {

    private static final String DEFAULT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION = "BBBBBBBBBB";

    @Autowired
    private SousFamilleDepenseRepository sousFamilleDepenseRepository;

    @Autowired
    private SousFamilleDepenseSearchRepository sousFamilleDepenseSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSousFamilleDepenseMockMvc;

    private SousFamilleDepense sousFamilleDepense;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SousFamilleDepenseResource sousFamilleDepenseResource = new SousFamilleDepenseResource(sousFamilleDepenseRepository, sousFamilleDepenseSearchRepository);
        this.restSousFamilleDepenseMockMvc = MockMvcBuilders.standaloneSetup(sousFamilleDepenseResource)
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
    public static SousFamilleDepense createEntity(EntityManager em) {
        SousFamilleDepense sousFamilleDepense = new SousFamilleDepense()
            .designation(DEFAULT_DESIGNATION);
        // Add required entity
        FamilleDepense familleDepense = FamilleDepenseResourceIntTest.createEntity(em);
        em.persist(familleDepense);
        em.flush();
        sousFamilleDepense.setFamilleDepense(familleDepense);
        return sousFamilleDepense;
    }

    @Before
    public void initTest() {
        sousFamilleDepenseSearchRepository.deleteAll();
        sousFamilleDepense = createEntity(em);
    }

    @Test
    @Transactional
    public void createSousFamilleDepense() throws Exception {
        int databaseSizeBeforeCreate = sousFamilleDepenseRepository.findAll().size();

        // Create the SousFamilleDepense
        restSousFamilleDepenseMockMvc.perform(post("/api/sous-famille-depenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sousFamilleDepense)))
            .andExpect(status().isCreated());

        // Validate the SousFamilleDepense in the database
        List<SousFamilleDepense> sousFamilleDepenseList = sousFamilleDepenseRepository.findAll();
        assertThat(sousFamilleDepenseList).hasSize(databaseSizeBeforeCreate + 1);
        SousFamilleDepense testSousFamilleDepense = sousFamilleDepenseList.get(sousFamilleDepenseList.size() - 1);
        assertThat(testSousFamilleDepense.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);

        // Validate the SousFamilleDepense in Elasticsearch
        SousFamilleDepense sousFamilleDepenseEs = sousFamilleDepenseSearchRepository.findOne(testSousFamilleDepense.getId());
        assertThat(sousFamilleDepenseEs).isEqualToComparingFieldByField(testSousFamilleDepense);
    }

    @Test
    @Transactional
    public void createSousFamilleDepenseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sousFamilleDepenseRepository.findAll().size();

        // Create the SousFamilleDepense with an existing ID
        sousFamilleDepense.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSousFamilleDepenseMockMvc.perform(post("/api/sous-famille-depenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sousFamilleDepense)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<SousFamilleDepense> sousFamilleDepenseList = sousFamilleDepenseRepository.findAll();
        assertThat(sousFamilleDepenseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDesignationIsRequired() throws Exception {
        int databaseSizeBeforeTest = sousFamilleDepenseRepository.findAll().size();
        // set the field null
        sousFamilleDepense.setDesignation(null);

        // Create the SousFamilleDepense, which fails.

        restSousFamilleDepenseMockMvc.perform(post("/api/sous-famille-depenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sousFamilleDepense)))
            .andExpect(status().isBadRequest());

        List<SousFamilleDepense> sousFamilleDepenseList = sousFamilleDepenseRepository.findAll();
        assertThat(sousFamilleDepenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSousFamilleDepenses() throws Exception {
        // Initialize the database
        sousFamilleDepenseRepository.saveAndFlush(sousFamilleDepense);

        // Get all the sousFamilleDepenseList
        restSousFamilleDepenseMockMvc.perform(get("/api/sous-famille-depenses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sousFamilleDepense.getId().intValue())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION.toString())));
    }

    @Test
    @Transactional
    public void getSousFamilleDepense() throws Exception {
        // Initialize the database
        sousFamilleDepenseRepository.saveAndFlush(sousFamilleDepense);

        // Get the sousFamilleDepense
        restSousFamilleDepenseMockMvc.perform(get("/api/sous-famille-depenses/{id}", sousFamilleDepense.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sousFamilleDepense.getId().intValue()))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSousFamilleDepense() throws Exception {
        // Get the sousFamilleDepense
        restSousFamilleDepenseMockMvc.perform(get("/api/sous-famille-depenses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSousFamilleDepense() throws Exception {
        // Initialize the database
        sousFamilleDepenseRepository.saveAndFlush(sousFamilleDepense);
        sousFamilleDepenseSearchRepository.save(sousFamilleDepense);
        int databaseSizeBeforeUpdate = sousFamilleDepenseRepository.findAll().size();

        // Update the sousFamilleDepense
        SousFamilleDepense updatedSousFamilleDepense = sousFamilleDepenseRepository.findOne(sousFamilleDepense.getId());
        updatedSousFamilleDepense
            .designation(UPDATED_DESIGNATION);

        restSousFamilleDepenseMockMvc.perform(put("/api/sous-famille-depenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSousFamilleDepense)))
            .andExpect(status().isOk());

        // Validate the SousFamilleDepense in the database
        List<SousFamilleDepense> sousFamilleDepenseList = sousFamilleDepenseRepository.findAll();
        assertThat(sousFamilleDepenseList).hasSize(databaseSizeBeforeUpdate);
        SousFamilleDepense testSousFamilleDepense = sousFamilleDepenseList.get(sousFamilleDepenseList.size() - 1);
        assertThat(testSousFamilleDepense.getDesignation()).isEqualTo(UPDATED_DESIGNATION);

        // Validate the SousFamilleDepense in Elasticsearch
        SousFamilleDepense sousFamilleDepenseEs = sousFamilleDepenseSearchRepository.findOne(testSousFamilleDepense.getId());
        assertThat(sousFamilleDepenseEs).isEqualToComparingFieldByField(testSousFamilleDepense);
    }

    @Test
    @Transactional
    public void updateNonExistingSousFamilleDepense() throws Exception {
        int databaseSizeBeforeUpdate = sousFamilleDepenseRepository.findAll().size();

        // Create the SousFamilleDepense

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSousFamilleDepenseMockMvc.perform(put("/api/sous-famille-depenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sousFamilleDepense)))
            .andExpect(status().isCreated());

        // Validate the SousFamilleDepense in the database
        List<SousFamilleDepense> sousFamilleDepenseList = sousFamilleDepenseRepository.findAll();
        assertThat(sousFamilleDepenseList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSousFamilleDepense() throws Exception {
        // Initialize the database
        sousFamilleDepenseRepository.saveAndFlush(sousFamilleDepense);
        sousFamilleDepenseSearchRepository.save(sousFamilleDepense);
        int databaseSizeBeforeDelete = sousFamilleDepenseRepository.findAll().size();

        // Get the sousFamilleDepense
        restSousFamilleDepenseMockMvc.perform(delete("/api/sous-famille-depenses/{id}", sousFamilleDepense.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean sousFamilleDepenseExistsInEs = sousFamilleDepenseSearchRepository.exists(sousFamilleDepense.getId());
        assertThat(sousFamilleDepenseExistsInEs).isFalse();

        // Validate the database is empty
        List<SousFamilleDepense> sousFamilleDepenseList = sousFamilleDepenseRepository.findAll();
        assertThat(sousFamilleDepenseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSousFamilleDepense() throws Exception {
        // Initialize the database
        sousFamilleDepenseRepository.saveAndFlush(sousFamilleDepense);
        sousFamilleDepenseSearchRepository.save(sousFamilleDepense);

        // Search the sousFamilleDepense
        restSousFamilleDepenseMockMvc.perform(get("/api/_search/sous-famille-depenses?query=id:" + sousFamilleDepense.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sousFamilleDepense.getId().intValue())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SousFamilleDepense.class);
    }
}

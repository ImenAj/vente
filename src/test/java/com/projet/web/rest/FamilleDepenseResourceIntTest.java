package com.projet.web.rest;

import com.projet.PfeApp;

import com.projet.domain.FamilleDepense;
import com.projet.repository.FamilleDepenseRepository;
import com.projet.repository.search.FamilleDepenseSearchRepository;
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
 * Test class for the FamilleDepenseResource REST controller.
 *
 * @see FamilleDepenseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PfeApp.class)
public class FamilleDepenseResourceIntTest {

    private static final String DEFAULT_DESIGNATION_FAMILLE = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION_FAMILLE = "BBBBBBBBBB";

    @Autowired
    private FamilleDepenseRepository familleDepenseRepository;

    @Autowired
    private FamilleDepenseSearchRepository familleDepenseSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFamilleDepenseMockMvc;

    private FamilleDepense familleDepense;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FamilleDepenseResource familleDepenseResource = new FamilleDepenseResource(familleDepenseRepository, familleDepenseSearchRepository);
        this.restFamilleDepenseMockMvc = MockMvcBuilders.standaloneSetup(familleDepenseResource)
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
    public static FamilleDepense createEntity(EntityManager em) {
        FamilleDepense familleDepense = new FamilleDepense()
            .designationFamille(DEFAULT_DESIGNATION_FAMILLE);
        return familleDepense;
    }

    @Before
    public void initTest() {
        familleDepenseSearchRepository.deleteAll();
        familleDepense = createEntity(em);
    }

    @Test
    @Transactional
    public void createFamilleDepense() throws Exception {
        int databaseSizeBeforeCreate = familleDepenseRepository.findAll().size();

        // Create the FamilleDepense
        restFamilleDepenseMockMvc.perform(post("/api/famille-depenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(familleDepense)))
            .andExpect(status().isCreated());

        // Validate the FamilleDepense in the database
        List<FamilleDepense> familleDepenseList = familleDepenseRepository.findAll();
        assertThat(familleDepenseList).hasSize(databaseSizeBeforeCreate + 1);
        FamilleDepense testFamilleDepense = familleDepenseList.get(familleDepenseList.size() - 1);
        assertThat(testFamilleDepense.getDesignationFamille()).isEqualTo(DEFAULT_DESIGNATION_FAMILLE);

        // Validate the FamilleDepense in Elasticsearch
        FamilleDepense familleDepenseEs = familleDepenseSearchRepository.findOne(testFamilleDepense.getId());
        assertThat(familleDepenseEs).isEqualToComparingFieldByField(testFamilleDepense);
    }

    @Test
    @Transactional
    public void createFamilleDepenseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = familleDepenseRepository.findAll().size();

        // Create the FamilleDepense with an existing ID
        familleDepense.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFamilleDepenseMockMvc.perform(post("/api/famille-depenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(familleDepense)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<FamilleDepense> familleDepenseList = familleDepenseRepository.findAll();
        assertThat(familleDepenseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDesignationFamilleIsRequired() throws Exception {
        int databaseSizeBeforeTest = familleDepenseRepository.findAll().size();
        // set the field null
        familleDepense.setDesignationFamille(null);

        // Create the FamilleDepense, which fails.

        restFamilleDepenseMockMvc.perform(post("/api/famille-depenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(familleDepense)))
            .andExpect(status().isBadRequest());

        List<FamilleDepense> familleDepenseList = familleDepenseRepository.findAll();
        assertThat(familleDepenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFamilleDepenses() throws Exception {
        // Initialize the database
        familleDepenseRepository.saveAndFlush(familleDepense);

        // Get all the familleDepenseList
        restFamilleDepenseMockMvc.perform(get("/api/famille-depenses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(familleDepense.getId().intValue())))
            .andExpect(jsonPath("$.[*].designationFamille").value(hasItem(DEFAULT_DESIGNATION_FAMILLE.toString())));
    }

    @Test
    @Transactional
    public void getFamilleDepense() throws Exception {
        // Initialize the database
        familleDepenseRepository.saveAndFlush(familleDepense);

        // Get the familleDepense
        restFamilleDepenseMockMvc.perform(get("/api/famille-depenses/{id}", familleDepense.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(familleDepense.getId().intValue()))
            .andExpect(jsonPath("$.designationFamille").value(DEFAULT_DESIGNATION_FAMILLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFamilleDepense() throws Exception {
        // Get the familleDepense
        restFamilleDepenseMockMvc.perform(get("/api/famille-depenses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFamilleDepense() throws Exception {
        // Initialize the database
        familleDepenseRepository.saveAndFlush(familleDepense);
        familleDepenseSearchRepository.save(familleDepense);
        int databaseSizeBeforeUpdate = familleDepenseRepository.findAll().size();

        // Update the familleDepense
        FamilleDepense updatedFamilleDepense = familleDepenseRepository.findOne(familleDepense.getId());
        updatedFamilleDepense
            .designationFamille(UPDATED_DESIGNATION_FAMILLE);

        restFamilleDepenseMockMvc.perform(put("/api/famille-depenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFamilleDepense)))
            .andExpect(status().isOk());

        // Validate the FamilleDepense in the database
        List<FamilleDepense> familleDepenseList = familleDepenseRepository.findAll();
        assertThat(familleDepenseList).hasSize(databaseSizeBeforeUpdate);
        FamilleDepense testFamilleDepense = familleDepenseList.get(familleDepenseList.size() - 1);
        assertThat(testFamilleDepense.getDesignationFamille()).isEqualTo(UPDATED_DESIGNATION_FAMILLE);

        // Validate the FamilleDepense in Elasticsearch
        FamilleDepense familleDepenseEs = familleDepenseSearchRepository.findOne(testFamilleDepense.getId());
        assertThat(familleDepenseEs).isEqualToComparingFieldByField(testFamilleDepense);
    }

    @Test
    @Transactional
    public void updateNonExistingFamilleDepense() throws Exception {
        int databaseSizeBeforeUpdate = familleDepenseRepository.findAll().size();

        // Create the FamilleDepense

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFamilleDepenseMockMvc.perform(put("/api/famille-depenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(familleDepense)))
            .andExpect(status().isCreated());

        // Validate the FamilleDepense in the database
        List<FamilleDepense> familleDepenseList = familleDepenseRepository.findAll();
        assertThat(familleDepenseList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFamilleDepense() throws Exception {
        // Initialize the database
        familleDepenseRepository.saveAndFlush(familleDepense);
        familleDepenseSearchRepository.save(familleDepense);
        int databaseSizeBeforeDelete = familleDepenseRepository.findAll().size();

        // Get the familleDepense
        restFamilleDepenseMockMvc.perform(delete("/api/famille-depenses/{id}", familleDepense.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean familleDepenseExistsInEs = familleDepenseSearchRepository.exists(familleDepense.getId());
        assertThat(familleDepenseExistsInEs).isFalse();

        // Validate the database is empty
        List<FamilleDepense> familleDepenseList = familleDepenseRepository.findAll();
        assertThat(familleDepenseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFamilleDepense() throws Exception {
        // Initialize the database
        familleDepenseRepository.saveAndFlush(familleDepense);
        familleDepenseSearchRepository.save(familleDepense);

        // Search the familleDepense
        restFamilleDepenseMockMvc.perform(get("/api/_search/famille-depenses?query=id:" + familleDepense.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(familleDepense.getId().intValue())))
            .andExpect(jsonPath("$.[*].designationFamille").value(hasItem(DEFAULT_DESIGNATION_FAMILLE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FamilleDepense.class);
    }
}

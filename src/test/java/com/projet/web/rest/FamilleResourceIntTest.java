package com.projet.web.rest;

import com.projet.PfeApp;

import com.projet.domain.Famille;
import com.projet.repository.FamilleRepository;
import com.projet.repository.search.FamilleSearchRepository;
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
 * Test class for the FamilleResource REST controller.
 *
 * @see FamilleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PfeApp.class)
public class FamilleResourceIntTest {

    private static final String DEFAULT_LIBELLE_F = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_F = "BBBBBBBBBB";

    @Autowired
    private FamilleRepository familleRepository;

    @Autowired
    private FamilleSearchRepository familleSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFamilleMockMvc;

    private Famille famille;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FamilleResource familleResource = new FamilleResource(familleRepository, familleSearchRepository);
        this.restFamilleMockMvc = MockMvcBuilders.standaloneSetup(familleResource)
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
    public static Famille createEntity(EntityManager em) {
        Famille famille = new Famille()
            .libelleF(DEFAULT_LIBELLE_F);
        return famille;
    }

    @Before
    public void initTest() {
        familleSearchRepository.deleteAll();
        famille = createEntity(em);
    }

    @Test
    @Transactional
    public void createFamille() throws Exception {
        int databaseSizeBeforeCreate = familleRepository.findAll().size();

        // Create the Famille
        restFamilleMockMvc.perform(post("/api/familles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(famille)))
            .andExpect(status().isCreated());

        // Validate the Famille in the database
        List<Famille> familleList = familleRepository.findAll();
        assertThat(familleList).hasSize(databaseSizeBeforeCreate + 1);
        Famille testFamille = familleList.get(familleList.size() - 1);
        assertThat(testFamille.getLibelleF()).isEqualTo(DEFAULT_LIBELLE_F);

        // Validate the Famille in Elasticsearch
        Famille familleEs = familleSearchRepository.findOne(testFamille.getId());
        assertThat(familleEs).isEqualToComparingFieldByField(testFamille);
    }

    @Test
    @Transactional
    public void createFamilleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = familleRepository.findAll().size();

        // Create the Famille with an existing ID
        famille.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFamilleMockMvc.perform(post("/api/familles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(famille)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Famille> familleList = familleRepository.findAll();
        assertThat(familleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkLibelleFIsRequired() throws Exception {
        int databaseSizeBeforeTest = familleRepository.findAll().size();
        // set the field null
        famille.setLibelleF(null);

        // Create the Famille, which fails.

        restFamilleMockMvc.perform(post("/api/familles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(famille)))
            .andExpect(status().isBadRequest());

        List<Famille> familleList = familleRepository.findAll();
        assertThat(familleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFamilles() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList
        restFamilleMockMvc.perform(get("/api/familles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(famille.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleF").value(hasItem(DEFAULT_LIBELLE_F.toString())));
    }

    @Test
    @Transactional
    public void getFamille() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get the famille
        restFamilleMockMvc.perform(get("/api/familles/{id}", famille.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(famille.getId().intValue()))
            .andExpect(jsonPath("$.libelleF").value(DEFAULT_LIBELLE_F.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFamille() throws Exception {
        // Get the famille
        restFamilleMockMvc.perform(get("/api/familles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFamille() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);
        familleSearchRepository.save(famille);
        int databaseSizeBeforeUpdate = familleRepository.findAll().size();

        // Update the famille
        Famille updatedFamille = familleRepository.findOne(famille.getId());
        updatedFamille
            .libelleF(UPDATED_LIBELLE_F);

        restFamilleMockMvc.perform(put("/api/familles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFamille)))
            .andExpect(status().isOk());

        // Validate the Famille in the database
        List<Famille> familleList = familleRepository.findAll();
        assertThat(familleList).hasSize(databaseSizeBeforeUpdate);
        Famille testFamille = familleList.get(familleList.size() - 1);
        assertThat(testFamille.getLibelleF()).isEqualTo(UPDATED_LIBELLE_F);

        // Validate the Famille in Elasticsearch
        Famille familleEs = familleSearchRepository.findOne(testFamille.getId());
        assertThat(familleEs).isEqualToComparingFieldByField(testFamille);
    }

    @Test
    @Transactional
    public void updateNonExistingFamille() throws Exception {
        int databaseSizeBeforeUpdate = familleRepository.findAll().size();

        // Create the Famille

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFamilleMockMvc.perform(put("/api/familles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(famille)))
            .andExpect(status().isCreated());

        // Validate the Famille in the database
        List<Famille> familleList = familleRepository.findAll();
        assertThat(familleList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFamille() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);
        familleSearchRepository.save(famille);
        int databaseSizeBeforeDelete = familleRepository.findAll().size();

        // Get the famille
        restFamilleMockMvc.perform(delete("/api/familles/{id}", famille.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean familleExistsInEs = familleSearchRepository.exists(famille.getId());
        assertThat(familleExistsInEs).isFalse();

        // Validate the database is empty
        List<Famille> familleList = familleRepository.findAll();
        assertThat(familleList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFamille() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);
        familleSearchRepository.save(famille);

        // Search the famille
        restFamilleMockMvc.perform(get("/api/_search/familles?query=id:" + famille.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(famille.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleF").value(hasItem(DEFAULT_LIBELLE_F.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Famille.class);
    }
}

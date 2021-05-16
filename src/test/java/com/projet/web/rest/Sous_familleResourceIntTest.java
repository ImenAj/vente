package com.projet.web.rest;

import com.projet.PfeApp;

import com.projet.domain.Sous_famille;
import com.projet.repository.Sous_familleRepository;
import com.projet.repository.search.Sous_familleSearchRepository;
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
 * Test class for the Sous_familleResource REST controller.
 *
 * @see Sous_familleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PfeApp.class)
public class Sous_familleResourceIntTest {

    private static final String DEFAULT_LIBELLE_SF = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_SF = "BBBBBBBBBB";

    @Autowired
    private Sous_familleRepository sous_familleRepository;

    @Autowired
    private Sous_familleSearchRepository sous_familleSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSous_familleMockMvc;

    private Sous_famille sous_famille;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Sous_familleResource sous_familleResource = new Sous_familleResource(sous_familleRepository, sous_familleSearchRepository);
        this.restSous_familleMockMvc = MockMvcBuilders.standaloneSetup(sous_familleResource)
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
    public static Sous_famille createEntity(EntityManager em) {
        Sous_famille sous_famille = new Sous_famille()
            .libelleSF(DEFAULT_LIBELLE_SF);
        return sous_famille;
    }

    @Before
    public void initTest() {
        sous_familleSearchRepository.deleteAll();
        sous_famille = createEntity(em);
    }

    @Test
    @Transactional
    public void createSous_famille() throws Exception {
        int databaseSizeBeforeCreate = sous_familleRepository.findAll().size();

        // Create the Sous_famille
        restSous_familleMockMvc.perform(post("/api/sous-familles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sous_famille)))
            .andExpect(status().isCreated());

        // Validate the Sous_famille in the database
        List<Sous_famille> sous_familleList = sous_familleRepository.findAll();
        assertThat(sous_familleList).hasSize(databaseSizeBeforeCreate + 1);
        Sous_famille testSous_famille = sous_familleList.get(sous_familleList.size() - 1);
        assertThat(testSous_famille.getLibelleSF()).isEqualTo(DEFAULT_LIBELLE_SF);

        // Validate the Sous_famille in Elasticsearch
        Sous_famille sous_familleEs = sous_familleSearchRepository.findOne(testSous_famille.getId());
        assertThat(sous_familleEs).isEqualToComparingFieldByField(testSous_famille);
    }

    @Test
    @Transactional
    public void createSous_familleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sous_familleRepository.findAll().size();

        // Create the Sous_famille with an existing ID
        sous_famille.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSous_familleMockMvc.perform(post("/api/sous-familles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sous_famille)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Sous_famille> sous_familleList = sous_familleRepository.findAll();
        assertThat(sous_familleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkLibelleSFIsRequired() throws Exception {
        int databaseSizeBeforeTest = sous_familleRepository.findAll().size();
        // set the field null
        sous_famille.setLibelleSF(null);

        // Create the Sous_famille, which fails.

        restSous_familleMockMvc.perform(post("/api/sous-familles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sous_famille)))
            .andExpect(status().isBadRequest());

        List<Sous_famille> sous_familleList = sous_familleRepository.findAll();
        assertThat(sous_familleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSous_familles() throws Exception {
        // Initialize the database
        sous_familleRepository.saveAndFlush(sous_famille);

        // Get all the sous_familleList
        restSous_familleMockMvc.perform(get("/api/sous-familles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sous_famille.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleSF").value(hasItem(DEFAULT_LIBELLE_SF.toString())));
    }

    @Test
    @Transactional
    public void getSous_famille() throws Exception {
        // Initialize the database
        sous_familleRepository.saveAndFlush(sous_famille);

        // Get the sous_famille
        restSous_familleMockMvc.perform(get("/api/sous-familles/{id}", sous_famille.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sous_famille.getId().intValue()))
            .andExpect(jsonPath("$.libelleSF").value(DEFAULT_LIBELLE_SF.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSous_famille() throws Exception {
        // Get the sous_famille
        restSous_familleMockMvc.perform(get("/api/sous-familles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSous_famille() throws Exception {
        // Initialize the database
        sous_familleRepository.saveAndFlush(sous_famille);
        sous_familleSearchRepository.save(sous_famille);
        int databaseSizeBeforeUpdate = sous_familleRepository.findAll().size();

        // Update the sous_famille
        Sous_famille updatedSous_famille = sous_familleRepository.findOne(sous_famille.getId());
        updatedSous_famille
            .libelleSF(UPDATED_LIBELLE_SF);

        restSous_familleMockMvc.perform(put("/api/sous-familles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSous_famille)))
            .andExpect(status().isOk());

        // Validate the Sous_famille in the database
        List<Sous_famille> sous_familleList = sous_familleRepository.findAll();
        assertThat(sous_familleList).hasSize(databaseSizeBeforeUpdate);
        Sous_famille testSous_famille = sous_familleList.get(sous_familleList.size() - 1);
        assertThat(testSous_famille.getLibelleSF()).isEqualTo(UPDATED_LIBELLE_SF);

        // Validate the Sous_famille in Elasticsearch
        Sous_famille sous_familleEs = sous_familleSearchRepository.findOne(testSous_famille.getId());
        assertThat(sous_familleEs).isEqualToComparingFieldByField(testSous_famille);
    }

    @Test
    @Transactional
    public void updateNonExistingSous_famille() throws Exception {
        int databaseSizeBeforeUpdate = sous_familleRepository.findAll().size();

        // Create the Sous_famille

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSous_familleMockMvc.perform(put("/api/sous-familles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sous_famille)))
            .andExpect(status().isCreated());

        // Validate the Sous_famille in the database
        List<Sous_famille> sous_familleList = sous_familleRepository.findAll();
        assertThat(sous_familleList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSous_famille() throws Exception {
        // Initialize the database
        sous_familleRepository.saveAndFlush(sous_famille);
        sous_familleSearchRepository.save(sous_famille);
        int databaseSizeBeforeDelete = sous_familleRepository.findAll().size();

        // Get the sous_famille
        restSous_familleMockMvc.perform(delete("/api/sous-familles/{id}", sous_famille.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean sous_familleExistsInEs = sous_familleSearchRepository.exists(sous_famille.getId());
        assertThat(sous_familleExistsInEs).isFalse();

        // Validate the database is empty
        List<Sous_famille> sous_familleList = sous_familleRepository.findAll();
        assertThat(sous_familleList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSous_famille() throws Exception {
        // Initialize the database
        sous_familleRepository.saveAndFlush(sous_famille);
        sous_familleSearchRepository.save(sous_famille);

        // Search the sous_famille
        restSous_familleMockMvc.perform(get("/api/_search/sous-familles?query=id:" + sous_famille.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sous_famille.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleSF").value(hasItem(DEFAULT_LIBELLE_SF.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sous_famille.class);
    }
}

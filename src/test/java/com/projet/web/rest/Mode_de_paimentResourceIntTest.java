package com.projet.web.rest;

import com.projet.PfeApp;

import com.projet.domain.Mode_de_paiment;
import com.projet.repository.Mode_de_paimentRepository;
import com.projet.repository.search.Mode_de_paimentSearchRepository;
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
 * Test class for the Mode_de_paimentResource REST controller.
 *
 * @see Mode_de_paimentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PfeApp.class)
public class Mode_de_paimentResourceIntTest {

    private static final String DEFAULT_MODE = "AAAAAAAAAA";
    private static final String UPDATED_MODE = "BBBBBBBBBB";

    @Autowired
    private Mode_de_paimentRepository mode_de_paimentRepository;

    @Autowired
    private Mode_de_paimentSearchRepository mode_de_paimentSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMode_de_paimentMockMvc;

    private Mode_de_paiment mode_de_paiment;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Mode_de_paimentResource mode_de_paimentResource = new Mode_de_paimentResource(mode_de_paimentRepository, mode_de_paimentSearchRepository);
        this.restMode_de_paimentMockMvc = MockMvcBuilders.standaloneSetup(mode_de_paimentResource)
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
    public static Mode_de_paiment createEntity(EntityManager em) {
        Mode_de_paiment mode_de_paiment = new Mode_de_paiment()
            .mode(DEFAULT_MODE);
        return mode_de_paiment;
    }

    @Before
    public void initTest() {
        mode_de_paimentSearchRepository.deleteAll();
        mode_de_paiment = createEntity(em);
    }

    @Test
    @Transactional
    public void createMode_de_paiment() throws Exception {
        int databaseSizeBeforeCreate = mode_de_paimentRepository.findAll().size();

        // Create the Mode_de_paiment
        restMode_de_paimentMockMvc.perform(post("/api/mode-de-paiments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mode_de_paiment)))
            .andExpect(status().isCreated());

        // Validate the Mode_de_paiment in the database
        List<Mode_de_paiment> mode_de_paimentList = mode_de_paimentRepository.findAll();
        assertThat(mode_de_paimentList).hasSize(databaseSizeBeforeCreate + 1);
        Mode_de_paiment testMode_de_paiment = mode_de_paimentList.get(mode_de_paimentList.size() - 1);
        assertThat(testMode_de_paiment.getMode()).isEqualTo(DEFAULT_MODE);

        // Validate the Mode_de_paiment in Elasticsearch
        Mode_de_paiment mode_de_paimentEs = mode_de_paimentSearchRepository.findOne(testMode_de_paiment.getId());
        assertThat(mode_de_paimentEs).isEqualToComparingFieldByField(testMode_de_paiment);
    }

    @Test
    @Transactional
    public void createMode_de_paimentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = mode_de_paimentRepository.findAll().size();

        // Create the Mode_de_paiment with an existing ID
        mode_de_paiment.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMode_de_paimentMockMvc.perform(post("/api/mode-de-paiments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mode_de_paiment)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Mode_de_paiment> mode_de_paimentList = mode_de_paimentRepository.findAll();
        assertThat(mode_de_paimentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkModeIsRequired() throws Exception {
        int databaseSizeBeforeTest = mode_de_paimentRepository.findAll().size();
        // set the field null
        mode_de_paiment.setMode(null);

        // Create the Mode_de_paiment, which fails.

        restMode_de_paimentMockMvc.perform(post("/api/mode-de-paiments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mode_de_paiment)))
            .andExpect(status().isBadRequest());

        List<Mode_de_paiment> mode_de_paimentList = mode_de_paimentRepository.findAll();
        assertThat(mode_de_paimentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMode_de_paiments() throws Exception {
        // Initialize the database
        mode_de_paimentRepository.saveAndFlush(mode_de_paiment);

        // Get all the mode_de_paimentList
        restMode_de_paimentMockMvc.perform(get("/api/mode-de-paiments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mode_de_paiment.getId().intValue())))
            .andExpect(jsonPath("$.[*].mode").value(hasItem(DEFAULT_MODE.toString())));
    }

    @Test
    @Transactional
    public void getMode_de_paiment() throws Exception {
        // Initialize the database
        mode_de_paimentRepository.saveAndFlush(mode_de_paiment);

        // Get the mode_de_paiment
        restMode_de_paimentMockMvc.perform(get("/api/mode-de-paiments/{id}", mode_de_paiment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(mode_de_paiment.getId().intValue()))
            .andExpect(jsonPath("$.mode").value(DEFAULT_MODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMode_de_paiment() throws Exception {
        // Get the mode_de_paiment
        restMode_de_paimentMockMvc.perform(get("/api/mode-de-paiments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMode_de_paiment() throws Exception {
        // Initialize the database
        mode_de_paimentRepository.saveAndFlush(mode_de_paiment);
        mode_de_paimentSearchRepository.save(mode_de_paiment);
        int databaseSizeBeforeUpdate = mode_de_paimentRepository.findAll().size();

        // Update the mode_de_paiment
        Mode_de_paiment updatedMode_de_paiment = mode_de_paimentRepository.findOne(mode_de_paiment.getId());
        updatedMode_de_paiment
            .mode(UPDATED_MODE);

        restMode_de_paimentMockMvc.perform(put("/api/mode-de-paiments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMode_de_paiment)))
            .andExpect(status().isOk());

        // Validate the Mode_de_paiment in the database
        List<Mode_de_paiment> mode_de_paimentList = mode_de_paimentRepository.findAll();
        assertThat(mode_de_paimentList).hasSize(databaseSizeBeforeUpdate);
        Mode_de_paiment testMode_de_paiment = mode_de_paimentList.get(mode_de_paimentList.size() - 1);
        assertThat(testMode_de_paiment.getMode()).isEqualTo(UPDATED_MODE);

        // Validate the Mode_de_paiment in Elasticsearch
        Mode_de_paiment mode_de_paimentEs = mode_de_paimentSearchRepository.findOne(testMode_de_paiment.getId());
        assertThat(mode_de_paimentEs).isEqualToComparingFieldByField(testMode_de_paiment);
    }

    @Test
    @Transactional
    public void updateNonExistingMode_de_paiment() throws Exception {
        int databaseSizeBeforeUpdate = mode_de_paimentRepository.findAll().size();

        // Create the Mode_de_paiment

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMode_de_paimentMockMvc.perform(put("/api/mode-de-paiments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mode_de_paiment)))
            .andExpect(status().isCreated());

        // Validate the Mode_de_paiment in the database
        List<Mode_de_paiment> mode_de_paimentList = mode_de_paimentRepository.findAll();
        assertThat(mode_de_paimentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMode_de_paiment() throws Exception {
        // Initialize the database
        mode_de_paimentRepository.saveAndFlush(mode_de_paiment);
        mode_de_paimentSearchRepository.save(mode_de_paiment);
        int databaseSizeBeforeDelete = mode_de_paimentRepository.findAll().size();

        // Get the mode_de_paiment
        restMode_de_paimentMockMvc.perform(delete("/api/mode-de-paiments/{id}", mode_de_paiment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean mode_de_paimentExistsInEs = mode_de_paimentSearchRepository.exists(mode_de_paiment.getId());
        assertThat(mode_de_paimentExistsInEs).isFalse();

        // Validate the database is empty
        List<Mode_de_paiment> mode_de_paimentList = mode_de_paimentRepository.findAll();
        assertThat(mode_de_paimentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMode_de_paiment() throws Exception {
        // Initialize the database
        mode_de_paimentRepository.saveAndFlush(mode_de_paiment);
        mode_de_paimentSearchRepository.save(mode_de_paiment);

        // Search the mode_de_paiment
        restMode_de_paimentMockMvc.perform(get("/api/_search/mode-de-paiments?query=id:" + mode_de_paiment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mode_de_paiment.getId().intValue())))
            .andExpect(jsonPath("$.[*].mode").value(hasItem(DEFAULT_MODE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mode_de_paiment.class);
    }
}

package com.projet.web.rest;

import com.projet.PfeApp;

import com.projet.domain.Condition_de_reglement;
import com.projet.repository.Condition_de_reglementRepository;
import com.projet.repository.search.Condition_de_reglementSearchRepository;
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
 * Test class for the Condition_de_reglementResource REST controller.
 *
 * @see Condition_de_reglementResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PfeApp.class)
public class Condition_de_reglementResourceIntTest {

    private static final String DEFAULT_CONDITION = "AAAAAAAAAA";
    private static final String UPDATED_CONDITION = "BBBBBBBBBB";

    @Autowired
    private Condition_de_reglementRepository condition_de_reglementRepository;

    @Autowired
    private Condition_de_reglementSearchRepository condition_de_reglementSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCondition_de_reglementMockMvc;

    private Condition_de_reglement condition_de_reglement;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Condition_de_reglementResource condition_de_reglementResource = new Condition_de_reglementResource(condition_de_reglementRepository, condition_de_reglementSearchRepository);
        this.restCondition_de_reglementMockMvc = MockMvcBuilders.standaloneSetup(condition_de_reglementResource)
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
    public static Condition_de_reglement createEntity(EntityManager em) {
        Condition_de_reglement condition_de_reglement = new Condition_de_reglement()
            .condition(DEFAULT_CONDITION);
        return condition_de_reglement;
    }

    @Before
    public void initTest() {
        condition_de_reglementSearchRepository.deleteAll();
        condition_de_reglement = createEntity(em);
    }

    @Test
    @Transactional
    public void createCondition_de_reglement() throws Exception {
        int databaseSizeBeforeCreate = condition_de_reglementRepository.findAll().size();

        // Create the Condition_de_reglement
        restCondition_de_reglementMockMvc.perform(post("/api/condition-de-reglements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(condition_de_reglement)))
            .andExpect(status().isCreated());

        // Validate the Condition_de_reglement in the database
        List<Condition_de_reglement> condition_de_reglementList = condition_de_reglementRepository.findAll();
        assertThat(condition_de_reglementList).hasSize(databaseSizeBeforeCreate + 1);
        Condition_de_reglement testCondition_de_reglement = condition_de_reglementList.get(condition_de_reglementList.size() - 1);
        assertThat(testCondition_de_reglement.getCondition()).isEqualTo(DEFAULT_CONDITION);

        // Validate the Condition_de_reglement in Elasticsearch
        Condition_de_reglement condition_de_reglementEs = condition_de_reglementSearchRepository.findOne(testCondition_de_reglement.getId());
        assertThat(condition_de_reglementEs).isEqualToComparingFieldByField(testCondition_de_reglement);
    }

    @Test
    @Transactional
    public void createCondition_de_reglementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = condition_de_reglementRepository.findAll().size();

        // Create the Condition_de_reglement with an existing ID
        condition_de_reglement.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCondition_de_reglementMockMvc.perform(post("/api/condition-de-reglements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(condition_de_reglement)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Condition_de_reglement> condition_de_reglementList = condition_de_reglementRepository.findAll();
        assertThat(condition_de_reglementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkConditionIsRequired() throws Exception {
        int databaseSizeBeforeTest = condition_de_reglementRepository.findAll().size();
        // set the field null
        condition_de_reglement.setCondition(null);

        // Create the Condition_de_reglement, which fails.

        restCondition_de_reglementMockMvc.perform(post("/api/condition-de-reglements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(condition_de_reglement)))
            .andExpect(status().isBadRequest());

        List<Condition_de_reglement> condition_de_reglementList = condition_de_reglementRepository.findAll();
        assertThat(condition_de_reglementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCondition_de_reglements() throws Exception {
        // Initialize the database
        condition_de_reglementRepository.saveAndFlush(condition_de_reglement);

        // Get all the condition_de_reglementList
        restCondition_de_reglementMockMvc.perform(get("/api/condition-de-reglements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(condition_de_reglement.getId().intValue())))
            .andExpect(jsonPath("$.[*].condition").value(hasItem(DEFAULT_CONDITION.toString())));
    }

    @Test
    @Transactional
    public void getCondition_de_reglement() throws Exception {
        // Initialize the database
        condition_de_reglementRepository.saveAndFlush(condition_de_reglement);

        // Get the condition_de_reglement
        restCondition_de_reglementMockMvc.perform(get("/api/condition-de-reglements/{id}", condition_de_reglement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(condition_de_reglement.getId().intValue()))
            .andExpect(jsonPath("$.condition").value(DEFAULT_CONDITION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCondition_de_reglement() throws Exception {
        // Get the condition_de_reglement
        restCondition_de_reglementMockMvc.perform(get("/api/condition-de-reglements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCondition_de_reglement() throws Exception {
        // Initialize the database
        condition_de_reglementRepository.saveAndFlush(condition_de_reglement);
        condition_de_reglementSearchRepository.save(condition_de_reglement);
        int databaseSizeBeforeUpdate = condition_de_reglementRepository.findAll().size();

        // Update the condition_de_reglement
        Condition_de_reglement updatedCondition_de_reglement = condition_de_reglementRepository.findOne(condition_de_reglement.getId());
        updatedCondition_de_reglement
            .condition(UPDATED_CONDITION);

        restCondition_de_reglementMockMvc.perform(put("/api/condition-de-reglements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCondition_de_reglement)))
            .andExpect(status().isOk());

        // Validate the Condition_de_reglement in the database
        List<Condition_de_reglement> condition_de_reglementList = condition_de_reglementRepository.findAll();
        assertThat(condition_de_reglementList).hasSize(databaseSizeBeforeUpdate);
        Condition_de_reglement testCondition_de_reglement = condition_de_reglementList.get(condition_de_reglementList.size() - 1);
        assertThat(testCondition_de_reglement.getCondition()).isEqualTo(UPDATED_CONDITION);

        // Validate the Condition_de_reglement in Elasticsearch
        Condition_de_reglement condition_de_reglementEs = condition_de_reglementSearchRepository.findOne(testCondition_de_reglement.getId());
        assertThat(condition_de_reglementEs).isEqualToComparingFieldByField(testCondition_de_reglement);
    }

    @Test
    @Transactional
    public void updateNonExistingCondition_de_reglement() throws Exception {
        int databaseSizeBeforeUpdate = condition_de_reglementRepository.findAll().size();

        // Create the Condition_de_reglement

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCondition_de_reglementMockMvc.perform(put("/api/condition-de-reglements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(condition_de_reglement)))
            .andExpect(status().isCreated());

        // Validate the Condition_de_reglement in the database
        List<Condition_de_reglement> condition_de_reglementList = condition_de_reglementRepository.findAll();
        assertThat(condition_de_reglementList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCondition_de_reglement() throws Exception {
        // Initialize the database
        condition_de_reglementRepository.saveAndFlush(condition_de_reglement);
        condition_de_reglementSearchRepository.save(condition_de_reglement);
        int databaseSizeBeforeDelete = condition_de_reglementRepository.findAll().size();

        // Get the condition_de_reglement
        restCondition_de_reglementMockMvc.perform(delete("/api/condition-de-reglements/{id}", condition_de_reglement.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean condition_de_reglementExistsInEs = condition_de_reglementSearchRepository.exists(condition_de_reglement.getId());
        assertThat(condition_de_reglementExistsInEs).isFalse();

        // Validate the database is empty
        List<Condition_de_reglement> condition_de_reglementList = condition_de_reglementRepository.findAll();
        assertThat(condition_de_reglementList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCondition_de_reglement() throws Exception {
        // Initialize the database
        condition_de_reglementRepository.saveAndFlush(condition_de_reglement);
        condition_de_reglementSearchRepository.save(condition_de_reglement);

        // Search the condition_de_reglement
        restCondition_de_reglementMockMvc.perform(get("/api/_search/condition-de-reglements?query=id:" + condition_de_reglement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(condition_de_reglement.getId().intValue())))
            .andExpect(jsonPath("$.[*].condition").value(hasItem(DEFAULT_CONDITION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Condition_de_reglement.class);
    }
}

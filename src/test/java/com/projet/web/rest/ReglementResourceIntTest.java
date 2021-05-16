package com.projet.web.rest;

import com.projet.PfeApp;

import com.projet.domain.Reglement;
import com.projet.domain.Depense;
import com.projet.domain.Mode_de_paiment;
import com.projet.domain.Condition_de_reglement;
import com.projet.domain.Article;
import com.projet.domain.Facture;
import com.projet.repository.ReglementRepository;
import com.projet.repository.search.ReglementSearchRepository;
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
 * Test class for the ReglementResource REST controller.
 *
 * @see ReglementResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PfeApp.class)
public class ReglementResourceIntTest {

    private static final LocalDate DEFAULT_DATE_REGLEMENT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_REGLEMENT = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Double DEFAULT_MONTANT = 1D;
    private static final Double UPDATED_MONTANT = 2D;

    @Autowired
    private ReglementRepository reglementRepository;

    @Autowired
    private ReglementSearchRepository reglementSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReglementMockMvc;

    private Reglement reglement;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReglementResource reglementResource = new ReglementResource(reglementRepository, reglementSearchRepository);
        this.restReglementMockMvc = MockMvcBuilders.standaloneSetup(reglementResource)
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
    public static Reglement createEntity(EntityManager em) {
        Reglement reglement = new Reglement()
            .date_Reglement(DEFAULT_DATE_REGLEMENT)
            .libelle(DEFAULT_LIBELLE)
            .montant(DEFAULT_MONTANT);
        // Add required entity
        Depense depense = DepenseResourceIntTest.createEntity(em);
        em.persist(depense);
        em.flush();
        reglement.setDepense(depense);
        // Add required entity
        Mode_de_paiment mode_de_paiment = Mode_de_paimentResourceIntTest.createEntity(em);
        em.persist(mode_de_paiment);
        em.flush();
        reglement.setMode_de_paiment(mode_de_paiment);
        // Add required entity
        Condition_de_reglement condition_de_reglement = Condition_de_reglementResourceIntTest.createEntity(em);
        em.persist(condition_de_reglement);
        em.flush();
        reglement.setCondition_de_reglement(condition_de_reglement);
        // Add required entity
        Article article = ArticleResourceIntTest.createEntity(em);
        em.persist(article);
        em.flush();
        reglement.setArticle(article);
        // Add required entity
        Facture facture = FactureResourceIntTest.createEntity(em);
        em.persist(facture);
        em.flush();
        reglement.setFacture(facture);
        return reglement;
    }

    @Before
    public void initTest() {
        reglementSearchRepository.deleteAll();
        reglement = createEntity(em);
    }

    @Test
    @Transactional
    public void createReglement() throws Exception {
        int databaseSizeBeforeCreate = reglementRepository.findAll().size();

        // Create the Reglement
        restReglementMockMvc.perform(post("/api/reglements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reglement)))
            .andExpect(status().isCreated());

        // Validate the Reglement in the database
        List<Reglement> reglementList = reglementRepository.findAll();
        assertThat(reglementList).hasSize(databaseSizeBeforeCreate + 1);
        Reglement testReglement = reglementList.get(reglementList.size() - 1);
        assertThat(testReglement.getDate_Reglement()).isEqualTo(DEFAULT_DATE_REGLEMENT);
        assertThat(testReglement.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testReglement.getMontant()).isEqualTo(DEFAULT_MONTANT);

        // Validate the Reglement in Elasticsearch
        Reglement reglementEs = reglementSearchRepository.findOne(testReglement.getId());
        assertThat(reglementEs).isEqualToComparingFieldByField(testReglement);
    }

    @Test
    @Transactional
    public void createReglementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reglementRepository.findAll().size();

        // Create the Reglement with an existing ID
        reglement.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReglementMockMvc.perform(post("/api/reglements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reglement)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Reglement> reglementList = reglementRepository.findAll();
        assertThat(reglementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDate_ReglementIsRequired() throws Exception {
        int databaseSizeBeforeTest = reglementRepository.findAll().size();
        // set the field null
        reglement.setDate_Reglement(null);

        // Create the Reglement, which fails.

        restReglementMockMvc.perform(post("/api/reglements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reglement)))
            .andExpect(status().isBadRequest());

        List<Reglement> reglementList = reglementRepository.findAll();
        assertThat(reglementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = reglementRepository.findAll().size();
        // set the field null
        reglement.setLibelle(null);

        // Create the Reglement, which fails.

        restReglementMockMvc.perform(post("/api/reglements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reglement)))
            .andExpect(status().isBadRequest());

        List<Reglement> reglementList = reglementRepository.findAll();
        assertThat(reglementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMontantIsRequired() throws Exception {
        int databaseSizeBeforeTest = reglementRepository.findAll().size();
        // set the field null
        reglement.setMontant(null);

        // Create the Reglement, which fails.

        restReglementMockMvc.perform(post("/api/reglements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reglement)))
            .andExpect(status().isBadRequest());

        List<Reglement> reglementList = reglementRepository.findAll();
        assertThat(reglementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllReglements() throws Exception {
        // Initialize the database
        reglementRepository.saveAndFlush(reglement);

        // Get all the reglementList
        restReglementMockMvc.perform(get("/api/reglements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reglement.getId().intValue())))
            .andExpect(jsonPath("$.[*].date_Reglement").value(hasItem(DEFAULT_DATE_REGLEMENT.toString())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())));
    }

    @Test
    @Transactional
    public void getReglement() throws Exception {
        // Initialize the database
        reglementRepository.saveAndFlush(reglement);

        // Get the reglement
        restReglementMockMvc.perform(get("/api/reglements/{id}", reglement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reglement.getId().intValue()))
            .andExpect(jsonPath("$.date_Reglement").value(DEFAULT_DATE_REGLEMENT.toString()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingReglement() throws Exception {
        // Get the reglement
        restReglementMockMvc.perform(get("/api/reglements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReglement() throws Exception {
        // Initialize the database
        reglementRepository.saveAndFlush(reglement);
        reglementSearchRepository.save(reglement);
        int databaseSizeBeforeUpdate = reglementRepository.findAll().size();

        // Update the reglement
        Reglement updatedReglement = reglementRepository.findOne(reglement.getId());
        updatedReglement
            .date_Reglement(UPDATED_DATE_REGLEMENT)
            .libelle(UPDATED_LIBELLE)
            .montant(UPDATED_MONTANT);

        restReglementMockMvc.perform(put("/api/reglements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedReglement)))
            .andExpect(status().isOk());

        // Validate the Reglement in the database
        List<Reglement> reglementList = reglementRepository.findAll();
        assertThat(reglementList).hasSize(databaseSizeBeforeUpdate);
        Reglement testReglement = reglementList.get(reglementList.size() - 1);
        assertThat(testReglement.getDate_Reglement()).isEqualTo(UPDATED_DATE_REGLEMENT);
        assertThat(testReglement.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testReglement.getMontant()).isEqualTo(UPDATED_MONTANT);

        // Validate the Reglement in Elasticsearch
        Reglement reglementEs = reglementSearchRepository.findOne(testReglement.getId());
        assertThat(reglementEs).isEqualToComparingFieldByField(testReglement);
    }

    @Test
    @Transactional
    public void updateNonExistingReglement() throws Exception {
        int databaseSizeBeforeUpdate = reglementRepository.findAll().size();

        // Create the Reglement

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restReglementMockMvc.perform(put("/api/reglements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reglement)))
            .andExpect(status().isCreated());

        // Validate the Reglement in the database
        List<Reglement> reglementList = reglementRepository.findAll();
        assertThat(reglementList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteReglement() throws Exception {
        // Initialize the database
        reglementRepository.saveAndFlush(reglement);
        reglementSearchRepository.save(reglement);
        int databaseSizeBeforeDelete = reglementRepository.findAll().size();

        // Get the reglement
        restReglementMockMvc.perform(delete("/api/reglements/{id}", reglement.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean reglementExistsInEs = reglementSearchRepository.exists(reglement.getId());
        assertThat(reglementExistsInEs).isFalse();

        // Validate the database is empty
        List<Reglement> reglementList = reglementRepository.findAll();
        assertThat(reglementList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchReglement() throws Exception {
        // Initialize the database
        reglementRepository.saveAndFlush(reglement);
        reglementSearchRepository.save(reglement);

        // Search the reglement
        restReglementMockMvc.perform(get("/api/_search/reglements?query=id:" + reglement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reglement.getId().intValue())))
            .andExpect(jsonPath("$.[*].date_Reglement").value(hasItem(DEFAULT_DATE_REGLEMENT.toString())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reglement.class);
    }
}

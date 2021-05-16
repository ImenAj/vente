package com.projet.web.rest;

import com.projet.PfeApp;

import com.projet.domain.Bons_de_commande;
import com.projet.domain.Fournisseur;
import com.projet.repository.Bons_de_commandeRepository;
import com.projet.repository.search.Bons_de_commandeSearchRepository;
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
 * Test class for the Bons_de_commandeResource REST controller.
 *
 * @see Bons_de_commandeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PfeApp.class)
public class Bons_de_commandeResourceIntTest {

    private static final String DEFAULT_REFERANCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERANCE = "BBBBBBBBBB";

    private static final String DEFAULT_ETAT = "AAAAAAAAAA";
    private static final String UPDATED_ETAT = "BBBBBBBBBB";

    private static final String DEFAULT_SITE = "AAAAAAAAAA";
    private static final String UPDATED_SITE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_COMMANDE_E = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_COMMANDE_E = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_LIVRAISON_PREVUE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_LIVRAISON_PREVUE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_LIVRE = false;
    private static final Boolean UPDATED_LIVRE = true;

    @Autowired
    private Bons_de_commandeRepository bons_de_commandeRepository;

    @Autowired
    private Bons_de_commandeSearchRepository bons_de_commandeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBons_de_commandeMockMvc;

    private Bons_de_commande bons_de_commande;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Bons_de_commandeResource bons_de_commandeResource = new Bons_de_commandeResource(bons_de_commandeRepository, bons_de_commandeSearchRepository);
        this.restBons_de_commandeMockMvc = MockMvcBuilders.standaloneSetup(bons_de_commandeResource)
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
    public static Bons_de_commande createEntity(EntityManager em) {
        Bons_de_commande bons_de_commande = new Bons_de_commande()
            .referance(DEFAULT_REFERANCE)
            .etat(DEFAULT_ETAT)
            .site(DEFAULT_SITE)
            .date_commandeE(DEFAULT_DATE_COMMANDE_E)
            .date_livraison_prevue(DEFAULT_DATE_LIVRAISON_PREVUE)
            .livre(DEFAULT_LIVRE);
        // Add required entity
        Fournisseur fournisseur = FournisseurResourceIntTest.createEntity(em);
        em.persist(fournisseur);
        em.flush();
        bons_de_commande.setFournisseur(fournisseur);
        return bons_de_commande;
    }

    @Before
    public void initTest() {
        bons_de_commandeSearchRepository.deleteAll();
        bons_de_commande = createEntity(em);
    }

    @Test
    @Transactional
    public void createBons_de_commande() throws Exception {
        int databaseSizeBeforeCreate = bons_de_commandeRepository.findAll().size();

        // Create the Bons_de_commande
        restBons_de_commandeMockMvc.perform(post("/api/bons-de-commandes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bons_de_commande)))
            .andExpect(status().isCreated());

        // Validate the Bons_de_commande in the database
        List<Bons_de_commande> bons_de_commandeList = bons_de_commandeRepository.findAll();
        assertThat(bons_de_commandeList).hasSize(databaseSizeBeforeCreate + 1);
        Bons_de_commande testBons_de_commande = bons_de_commandeList.get(bons_de_commandeList.size() - 1);
        assertThat(testBons_de_commande.getReferance()).isEqualTo(DEFAULT_REFERANCE);
        assertThat(testBons_de_commande.getEtat()).isEqualTo(DEFAULT_ETAT);
        assertThat(testBons_de_commande.getSite()).isEqualTo(DEFAULT_SITE);
        assertThat(testBons_de_commande.getDate_commandeE()).isEqualTo(DEFAULT_DATE_COMMANDE_E);
        assertThat(testBons_de_commande.getDate_livraison_prevue()).isEqualTo(DEFAULT_DATE_LIVRAISON_PREVUE);
        assertThat(testBons_de_commande.isLivre()).isEqualTo(DEFAULT_LIVRE);

        // Validate the Bons_de_commande in Elasticsearch
        Bons_de_commande bons_de_commandeEs = bons_de_commandeSearchRepository.findOne(testBons_de_commande.getId());
        assertThat(bons_de_commandeEs).isEqualToComparingFieldByField(testBons_de_commande);
    }

    @Test
    @Transactional
    public void createBons_de_commandeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bons_de_commandeRepository.findAll().size();

        // Create the Bons_de_commande with an existing ID
        bons_de_commande.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBons_de_commandeMockMvc.perform(post("/api/bons-de-commandes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bons_de_commande)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Bons_de_commande> bons_de_commandeList = bons_de_commandeRepository.findAll();
        assertThat(bons_de_commandeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkReferanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = bons_de_commandeRepository.findAll().size();
        // set the field null
        bons_de_commande.setReferance(null);

        // Create the Bons_de_commande, which fails.

        restBons_de_commandeMockMvc.perform(post("/api/bons-de-commandes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bons_de_commande)))
            .andExpect(status().isBadRequest());

        List<Bons_de_commande> bons_de_commandeList = bons_de_commandeRepository.findAll();
        assertThat(bons_de_commandeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDate_commandeEIsRequired() throws Exception {
        int databaseSizeBeforeTest = bons_de_commandeRepository.findAll().size();
        // set the field null
        bons_de_commande.setDate_commandeE(null);

        // Create the Bons_de_commande, which fails.

        restBons_de_commandeMockMvc.perform(post("/api/bons-de-commandes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bons_de_commande)))
            .andExpect(status().isBadRequest());

        List<Bons_de_commande> bons_de_commandeList = bons_de_commandeRepository.findAll();
        assertThat(bons_de_commandeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDate_livraison_prevueIsRequired() throws Exception {
        int databaseSizeBeforeTest = bons_de_commandeRepository.findAll().size();
        // set the field null
        bons_de_commande.setDate_livraison_prevue(null);

        // Create the Bons_de_commande, which fails.

        restBons_de_commandeMockMvc.perform(post("/api/bons-de-commandes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bons_de_commande)))
            .andExpect(status().isBadRequest());

        List<Bons_de_commande> bons_de_commandeList = bons_de_commandeRepository.findAll();
        assertThat(bons_de_commandeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLivreIsRequired() throws Exception {
        int databaseSizeBeforeTest = bons_de_commandeRepository.findAll().size();
        // set the field null
        bons_de_commande.setLivre(null);

        // Create the Bons_de_commande, which fails.

        restBons_de_commandeMockMvc.perform(post("/api/bons-de-commandes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bons_de_commande)))
            .andExpect(status().isBadRequest());

        List<Bons_de_commande> bons_de_commandeList = bons_de_commandeRepository.findAll();
        assertThat(bons_de_commandeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBons_de_commandes() throws Exception {
        // Initialize the database
        bons_de_commandeRepository.saveAndFlush(bons_de_commande);

        // Get all the bons_de_commandeList
        restBons_de_commandeMockMvc.perform(get("/api/bons-de-commandes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bons_de_commande.getId().intValue())))
            .andExpect(jsonPath("$.[*].referance").value(hasItem(DEFAULT_REFERANCE.toString())))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())))
            .andExpect(jsonPath("$.[*].site").value(hasItem(DEFAULT_SITE.toString())))
            .andExpect(jsonPath("$.[*].date_commandeE").value(hasItem(DEFAULT_DATE_COMMANDE_E.toString())))
            .andExpect(jsonPath("$.[*].date_livraison_prevue").value(hasItem(DEFAULT_DATE_LIVRAISON_PREVUE.toString())))
            .andExpect(jsonPath("$.[*].livre").value(hasItem(DEFAULT_LIVRE.booleanValue())));
    }

    @Test
    @Transactional
    public void getBons_de_commande() throws Exception {
        // Initialize the database
        bons_de_commandeRepository.saveAndFlush(bons_de_commande);

        // Get the bons_de_commande
        restBons_de_commandeMockMvc.perform(get("/api/bons-de-commandes/{id}", bons_de_commande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bons_de_commande.getId().intValue()))
            .andExpect(jsonPath("$.referance").value(DEFAULT_REFERANCE.toString()))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT.toString()))
            .andExpect(jsonPath("$.site").value(DEFAULT_SITE.toString()))
            .andExpect(jsonPath("$.date_commandeE").value(DEFAULT_DATE_COMMANDE_E.toString()))
            .andExpect(jsonPath("$.date_livraison_prevue").value(DEFAULT_DATE_LIVRAISON_PREVUE.toString()))
            .andExpect(jsonPath("$.livre").value(DEFAULT_LIVRE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBons_de_commande() throws Exception {
        // Get the bons_de_commande
        restBons_de_commandeMockMvc.perform(get("/api/bons-de-commandes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBons_de_commande() throws Exception {
        // Initialize the database
        bons_de_commandeRepository.saveAndFlush(bons_de_commande);
        bons_de_commandeSearchRepository.save(bons_de_commande);
        int databaseSizeBeforeUpdate = bons_de_commandeRepository.findAll().size();

        // Update the bons_de_commande
        Bons_de_commande updatedBons_de_commande = bons_de_commandeRepository.findOne(bons_de_commande.getId());
        updatedBons_de_commande
            .referance(UPDATED_REFERANCE)
            .etat(UPDATED_ETAT)
            .site(UPDATED_SITE)
            .date_commandeE(UPDATED_DATE_COMMANDE_E)
            .date_livraison_prevue(UPDATED_DATE_LIVRAISON_PREVUE)
            .livre(UPDATED_LIVRE);

        restBons_de_commandeMockMvc.perform(put("/api/bons-de-commandes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBons_de_commande)))
            .andExpect(status().isOk());

        // Validate the Bons_de_commande in the database
        List<Bons_de_commande> bons_de_commandeList = bons_de_commandeRepository.findAll();
        assertThat(bons_de_commandeList).hasSize(databaseSizeBeforeUpdate);
        Bons_de_commande testBons_de_commande = bons_de_commandeList.get(bons_de_commandeList.size() - 1);
        assertThat(testBons_de_commande.getReferance()).isEqualTo(UPDATED_REFERANCE);
        assertThat(testBons_de_commande.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testBons_de_commande.getSite()).isEqualTo(UPDATED_SITE);
        assertThat(testBons_de_commande.getDate_commandeE()).isEqualTo(UPDATED_DATE_COMMANDE_E);
        assertThat(testBons_de_commande.getDate_livraison_prevue()).isEqualTo(UPDATED_DATE_LIVRAISON_PREVUE);
        assertThat(testBons_de_commande.isLivre()).isEqualTo(UPDATED_LIVRE);

        // Validate the Bons_de_commande in Elasticsearch
        Bons_de_commande bons_de_commandeEs = bons_de_commandeSearchRepository.findOne(testBons_de_commande.getId());
        assertThat(bons_de_commandeEs).isEqualToComparingFieldByField(testBons_de_commande);
    }

    @Test
    @Transactional
    public void updateNonExistingBons_de_commande() throws Exception {
        int databaseSizeBeforeUpdate = bons_de_commandeRepository.findAll().size();

        // Create the Bons_de_commande

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBons_de_commandeMockMvc.perform(put("/api/bons-de-commandes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bons_de_commande)))
            .andExpect(status().isCreated());

        // Validate the Bons_de_commande in the database
        List<Bons_de_commande> bons_de_commandeList = bons_de_commandeRepository.findAll();
        assertThat(bons_de_commandeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBons_de_commande() throws Exception {
        // Initialize the database
        bons_de_commandeRepository.saveAndFlush(bons_de_commande);
        bons_de_commandeSearchRepository.save(bons_de_commande);
        int databaseSizeBeforeDelete = bons_de_commandeRepository.findAll().size();

        // Get the bons_de_commande
        restBons_de_commandeMockMvc.perform(delete("/api/bons-de-commandes/{id}", bons_de_commande.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean bons_de_commandeExistsInEs = bons_de_commandeSearchRepository.exists(bons_de_commande.getId());
        assertThat(bons_de_commandeExistsInEs).isFalse();

        // Validate the database is empty
        List<Bons_de_commande> bons_de_commandeList = bons_de_commandeRepository.findAll();
        assertThat(bons_de_commandeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBons_de_commande() throws Exception {
        // Initialize the database
        bons_de_commandeRepository.saveAndFlush(bons_de_commande);
        bons_de_commandeSearchRepository.save(bons_de_commande);

        // Search the bons_de_commande
        restBons_de_commandeMockMvc.perform(get("/api/_search/bons-de-commandes?query=id:" + bons_de_commande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bons_de_commande.getId().intValue())))
            .andExpect(jsonPath("$.[*].referance").value(hasItem(DEFAULT_REFERANCE.toString())))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())))
            .andExpect(jsonPath("$.[*].site").value(hasItem(DEFAULT_SITE.toString())))
            .andExpect(jsonPath("$.[*].date_commandeE").value(hasItem(DEFAULT_DATE_COMMANDE_E.toString())))
            .andExpect(jsonPath("$.[*].date_livraison_prevue").value(hasItem(DEFAULT_DATE_LIVRAISON_PREVUE.toString())))
            .andExpect(jsonPath("$.[*].livre").value(hasItem(DEFAULT_LIVRE.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bons_de_commande.class);
    }
}

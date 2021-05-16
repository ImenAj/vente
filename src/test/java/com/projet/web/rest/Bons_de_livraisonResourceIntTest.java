package com.projet.web.rest;

import com.projet.PfeApp;

import com.projet.domain.Bons_de_livraison;
import com.projet.domain.Article;
import com.projet.repository.Bons_de_livraisonRepository;
import com.projet.repository.search.Bons_de_livraisonSearchRepository;
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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the Bons_de_livraisonResource REST controller.
 *
 * @see Bons_de_livraisonResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PfeApp.class)
public class Bons_de_livraisonResourceIntTest {

    private static final String DEFAULT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITE_COMMANDEES = 1;
    private static final Integer UPDATED_QUANTITE_COMMANDEES = 2;

    private static final LocalDate DEFAULT_DATE_LIVRAISON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_LIVRAISON = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_OBJET = "AAAAAAAAAA";
    private static final String UPDATED_OBJET = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PIECE_JOINTE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PIECE_JOINTE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_PIECE_JOINTE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PIECE_JOINTE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    @Autowired
    private Bons_de_livraisonRepository bons_de_livraisonRepository;

    @Autowired
    private Bons_de_livraisonSearchRepository bons_de_livraisonSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBons_de_livraisonMockMvc;

    private Bons_de_livraison bons_de_livraison;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Bons_de_livraisonResource bons_de_livraisonResource = new Bons_de_livraisonResource(bons_de_livraisonRepository, bons_de_livraisonSearchRepository);
        this.restBons_de_livraisonMockMvc = MockMvcBuilders.standaloneSetup(bons_de_livraisonResource)
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
    public static Bons_de_livraison createEntity(EntityManager em) {
        Bons_de_livraison bons_de_livraison = new Bons_de_livraison()
            .designation(DEFAULT_DESIGNATION)
            .quantite_commandees(DEFAULT_QUANTITE_COMMANDEES)
            .date_livraison(DEFAULT_DATE_LIVRAISON)
            .objet(DEFAULT_OBJET)
            .piece_jointe(DEFAULT_PIECE_JOINTE)
            .piece_jointeContentType(DEFAULT_PIECE_JOINTE_CONTENT_TYPE)
            .reference(DEFAULT_REFERENCE);
        // Add required entity
        Article article = ArticleResourceIntTest.createEntity(em);
        em.persist(article);
        em.flush();
        bons_de_livraison.setArticle(article);
        return bons_de_livraison;
    }

    @Before
    public void initTest() {
        bons_de_livraisonSearchRepository.deleteAll();
        bons_de_livraison = createEntity(em);
    }

    @Test
    @Transactional
    public void createBons_de_livraison() throws Exception {
        int databaseSizeBeforeCreate = bons_de_livraisonRepository.findAll().size();

        // Create the Bons_de_livraison
        restBons_de_livraisonMockMvc.perform(post("/api/bons-de-livraisons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bons_de_livraison)))
            .andExpect(status().isCreated());

        // Validate the Bons_de_livraison in the database
        List<Bons_de_livraison> bons_de_livraisonList = bons_de_livraisonRepository.findAll();
        assertThat(bons_de_livraisonList).hasSize(databaseSizeBeforeCreate + 1);
        Bons_de_livraison testBons_de_livraison = bons_de_livraisonList.get(bons_de_livraisonList.size() - 1);
        assertThat(testBons_de_livraison.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testBons_de_livraison.getQuantite_commandees()).isEqualTo(DEFAULT_QUANTITE_COMMANDEES);
        assertThat(testBons_de_livraison.getDate_livraison()).isEqualTo(DEFAULT_DATE_LIVRAISON);
        assertThat(testBons_de_livraison.getObjet()).isEqualTo(DEFAULT_OBJET);
        assertThat(testBons_de_livraison.getPiece_jointe()).isEqualTo(DEFAULT_PIECE_JOINTE);
        assertThat(testBons_de_livraison.getPiece_jointeContentType()).isEqualTo(DEFAULT_PIECE_JOINTE_CONTENT_TYPE);
        assertThat(testBons_de_livraison.getReference()).isEqualTo(DEFAULT_REFERENCE);

        // Validate the Bons_de_livraison in Elasticsearch
        Bons_de_livraison bons_de_livraisonEs = bons_de_livraisonSearchRepository.findOne(testBons_de_livraison.getId());
        assertThat(bons_de_livraisonEs).isEqualToComparingFieldByField(testBons_de_livraison);
    }

    @Test
    @Transactional
    public void createBons_de_livraisonWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bons_de_livraisonRepository.findAll().size();

        // Create the Bons_de_livraison with an existing ID
        bons_de_livraison.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBons_de_livraisonMockMvc.perform(post("/api/bons-de-livraisons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bons_de_livraison)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Bons_de_livraison> bons_de_livraisonList = bons_de_livraisonRepository.findAll();
        assertThat(bons_de_livraisonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDesignationIsRequired() throws Exception {
        int databaseSizeBeforeTest = bons_de_livraisonRepository.findAll().size();
        // set the field null
        bons_de_livraison.setDesignation(null);

        // Create the Bons_de_livraison, which fails.

        restBons_de_livraisonMockMvc.perform(post("/api/bons-de-livraisons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bons_de_livraison)))
            .andExpect(status().isBadRequest());

        List<Bons_de_livraison> bons_de_livraisonList = bons_de_livraisonRepository.findAll();
        assertThat(bons_de_livraisonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantite_commandeesIsRequired() throws Exception {
        int databaseSizeBeforeTest = bons_de_livraisonRepository.findAll().size();
        // set the field null
        bons_de_livraison.setQuantite_commandees(null);

        // Create the Bons_de_livraison, which fails.

        restBons_de_livraisonMockMvc.perform(post("/api/bons-de-livraisons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bons_de_livraison)))
            .andExpect(status().isBadRequest());

        List<Bons_de_livraison> bons_de_livraisonList = bons_de_livraisonRepository.findAll();
        assertThat(bons_de_livraisonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDate_livraisonIsRequired() throws Exception {
        int databaseSizeBeforeTest = bons_de_livraisonRepository.findAll().size();
        // set the field null
        bons_de_livraison.setDate_livraison(null);

        // Create the Bons_de_livraison, which fails.

        restBons_de_livraisonMockMvc.perform(post("/api/bons-de-livraisons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bons_de_livraison)))
            .andExpect(status().isBadRequest());

        List<Bons_de_livraison> bons_de_livraisonList = bons_de_livraisonRepository.findAll();
        assertThat(bons_de_livraisonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBons_de_livraisons() throws Exception {
        // Initialize the database
        bons_de_livraisonRepository.saveAndFlush(bons_de_livraison);

        // Get all the bons_de_livraisonList
        restBons_de_livraisonMockMvc.perform(get("/api/bons-de-livraisons?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bons_de_livraison.getId().intValue())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION.toString())))
            .andExpect(jsonPath("$.[*].quantite_commandees").value(hasItem(DEFAULT_QUANTITE_COMMANDEES)))
            .andExpect(jsonPath("$.[*].date_livraison").value(hasItem(DEFAULT_DATE_LIVRAISON.toString())))
            .andExpect(jsonPath("$.[*].objet").value(hasItem(DEFAULT_OBJET.toString())))
            .andExpect(jsonPath("$.[*].piece_jointeContentType").value(hasItem(DEFAULT_PIECE_JOINTE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].piece_jointe").value(hasItem(Base64Utils.encodeToString(DEFAULT_PIECE_JOINTE))))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE.toString())));
    }

    @Test
    @Transactional
    public void getBons_de_livraison() throws Exception {
        // Initialize the database
        bons_de_livraisonRepository.saveAndFlush(bons_de_livraison);

        // Get the bons_de_livraison
        restBons_de_livraisonMockMvc.perform(get("/api/bons-de-livraisons/{id}", bons_de_livraison.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bons_de_livraison.getId().intValue()))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION.toString()))
            .andExpect(jsonPath("$.quantite_commandees").value(DEFAULT_QUANTITE_COMMANDEES))
            .andExpect(jsonPath("$.date_livraison").value(DEFAULT_DATE_LIVRAISON.toString()))
            .andExpect(jsonPath("$.objet").value(DEFAULT_OBJET.toString()))
            .andExpect(jsonPath("$.piece_jointeContentType").value(DEFAULT_PIECE_JOINTE_CONTENT_TYPE))
            .andExpect(jsonPath("$.piece_jointe").value(Base64Utils.encodeToString(DEFAULT_PIECE_JOINTE)))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBons_de_livraison() throws Exception {
        // Get the bons_de_livraison
        restBons_de_livraisonMockMvc.perform(get("/api/bons-de-livraisons/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBons_de_livraison() throws Exception {
        // Initialize the database
        bons_de_livraisonRepository.saveAndFlush(bons_de_livraison);
        bons_de_livraisonSearchRepository.save(bons_de_livraison);
        int databaseSizeBeforeUpdate = bons_de_livraisonRepository.findAll().size();

        // Update the bons_de_livraison
        Bons_de_livraison updatedBons_de_livraison = bons_de_livraisonRepository.findOne(bons_de_livraison.getId());
        updatedBons_de_livraison
            .designation(UPDATED_DESIGNATION)
            .quantite_commandees(UPDATED_QUANTITE_COMMANDEES)
            .date_livraison(UPDATED_DATE_LIVRAISON)
            .objet(UPDATED_OBJET)
            .piece_jointe(UPDATED_PIECE_JOINTE)
            .piece_jointeContentType(UPDATED_PIECE_JOINTE_CONTENT_TYPE)
            .reference(UPDATED_REFERENCE);

        restBons_de_livraisonMockMvc.perform(put("/api/bons-de-livraisons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBons_de_livraison)))
            .andExpect(status().isOk());

        // Validate the Bons_de_livraison in the database
        List<Bons_de_livraison> bons_de_livraisonList = bons_de_livraisonRepository.findAll();
        assertThat(bons_de_livraisonList).hasSize(databaseSizeBeforeUpdate);
        Bons_de_livraison testBons_de_livraison = bons_de_livraisonList.get(bons_de_livraisonList.size() - 1);
        assertThat(testBons_de_livraison.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testBons_de_livraison.getQuantite_commandees()).isEqualTo(UPDATED_QUANTITE_COMMANDEES);
        assertThat(testBons_de_livraison.getDate_livraison()).isEqualTo(UPDATED_DATE_LIVRAISON);
        assertThat(testBons_de_livraison.getObjet()).isEqualTo(UPDATED_OBJET);
        assertThat(testBons_de_livraison.getPiece_jointe()).isEqualTo(UPDATED_PIECE_JOINTE);
        assertThat(testBons_de_livraison.getPiece_jointeContentType()).isEqualTo(UPDATED_PIECE_JOINTE_CONTENT_TYPE);
        assertThat(testBons_de_livraison.getReference()).isEqualTo(UPDATED_REFERENCE);

        // Validate the Bons_de_livraison in Elasticsearch
        Bons_de_livraison bons_de_livraisonEs = bons_de_livraisonSearchRepository.findOne(testBons_de_livraison.getId());
        assertThat(bons_de_livraisonEs).isEqualToComparingFieldByField(testBons_de_livraison);
    }

    @Test
    @Transactional
    public void updateNonExistingBons_de_livraison() throws Exception {
        int databaseSizeBeforeUpdate = bons_de_livraisonRepository.findAll().size();

        // Create the Bons_de_livraison

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBons_de_livraisonMockMvc.perform(put("/api/bons-de-livraisons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bons_de_livraison)))
            .andExpect(status().isCreated());

        // Validate the Bons_de_livraison in the database
        List<Bons_de_livraison> bons_de_livraisonList = bons_de_livraisonRepository.findAll();
        assertThat(bons_de_livraisonList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBons_de_livraison() throws Exception {
        // Initialize the database
        bons_de_livraisonRepository.saveAndFlush(bons_de_livraison);
        bons_de_livraisonSearchRepository.save(bons_de_livraison);
        int databaseSizeBeforeDelete = bons_de_livraisonRepository.findAll().size();

        // Get the bons_de_livraison
        restBons_de_livraisonMockMvc.perform(delete("/api/bons-de-livraisons/{id}", bons_de_livraison.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean bons_de_livraisonExistsInEs = bons_de_livraisonSearchRepository.exists(bons_de_livraison.getId());
        assertThat(bons_de_livraisonExistsInEs).isFalse();

        // Validate the database is empty
        List<Bons_de_livraison> bons_de_livraisonList = bons_de_livraisonRepository.findAll();
        assertThat(bons_de_livraisonList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBons_de_livraison() throws Exception {
        // Initialize the database
        bons_de_livraisonRepository.saveAndFlush(bons_de_livraison);
        bons_de_livraisonSearchRepository.save(bons_de_livraison);

        // Search the bons_de_livraison
        restBons_de_livraisonMockMvc.perform(get("/api/_search/bons-de-livraisons?query=id:" + bons_de_livraison.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bons_de_livraison.getId().intValue())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION.toString())))
            .andExpect(jsonPath("$.[*].quantite_commandees").value(hasItem(DEFAULT_QUANTITE_COMMANDEES)))
            .andExpect(jsonPath("$.[*].date_livraison").value(hasItem(DEFAULT_DATE_LIVRAISON.toString())))
            .andExpect(jsonPath("$.[*].objet").value(hasItem(DEFAULT_OBJET.toString())))
            .andExpect(jsonPath("$.[*].piece_jointeContentType").value(hasItem(DEFAULT_PIECE_JOINTE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].piece_jointe").value(hasItem(Base64Utils.encodeToString(DEFAULT_PIECE_JOINTE))))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bons_de_livraison.class);
    }
}

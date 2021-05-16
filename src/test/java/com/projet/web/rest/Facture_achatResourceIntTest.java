package com.projet.web.rest;

import com.projet.PfeApp;

import com.projet.domain.Facture_achat;
import com.projet.repository.Facture_achatRepository;
import com.projet.repository.search.Facture_achatSearchRepository;
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
 * Test class for the Facture_achatResource REST controller.
 *
 * @see Facture_achatResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PfeApp.class)
public class Facture_achatResourceIntTest {

    private static final String DEFAULT_REFERANCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERANCE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_FACTURE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FACTURE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_ECHEANCE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ECHEANCE = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_MONTANT = 1D;
    private static final Double UPDATED_MONTANT = 2D;

    private static final byte[] DEFAULT_PIECE_JOINTE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PIECE_JOINTE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_PIECE_JOINTE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PIECE_JOINTE_CONTENT_TYPE = "image/png";

    @Autowired
    private Facture_achatRepository facture_achatRepository;

    @Autowired
    private Facture_achatSearchRepository facture_achatSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFacture_achatMockMvc;

    private Facture_achat facture_achat;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Facture_achatResource facture_achatResource = new Facture_achatResource(facture_achatRepository, facture_achatSearchRepository);
        this.restFacture_achatMockMvc = MockMvcBuilders.standaloneSetup(facture_achatResource)
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
    public static Facture_achat createEntity(EntityManager em) {
        Facture_achat facture_achat = new Facture_achat()
            .referance(DEFAULT_REFERANCE)
            .date_facture(DEFAULT_DATE_FACTURE)
            .echeance(DEFAULT_ECHEANCE)
            .montant(DEFAULT_MONTANT)
            .piece_jointe(DEFAULT_PIECE_JOINTE)
            .piece_jointeContentType(DEFAULT_PIECE_JOINTE_CONTENT_TYPE);
        return facture_achat;
    }

    @Before
    public void initTest() {
        facture_achatSearchRepository.deleteAll();
        facture_achat = createEntity(em);
    }

    @Test
    @Transactional
    public void createFacture_achat() throws Exception {
        int databaseSizeBeforeCreate = facture_achatRepository.findAll().size();

        // Create the Facture_achat
        restFacture_achatMockMvc.perform(post("/api/facture-achats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facture_achat)))
            .andExpect(status().isCreated());

        // Validate the Facture_achat in the database
        List<Facture_achat> facture_achatList = facture_achatRepository.findAll();
        assertThat(facture_achatList).hasSize(databaseSizeBeforeCreate + 1);
        Facture_achat testFacture_achat = facture_achatList.get(facture_achatList.size() - 1);
        assertThat(testFacture_achat.getReferance()).isEqualTo(DEFAULT_REFERANCE);
        assertThat(testFacture_achat.getDate_facture()).isEqualTo(DEFAULT_DATE_FACTURE);
        assertThat(testFacture_achat.getEcheance()).isEqualTo(DEFAULT_ECHEANCE);
        assertThat(testFacture_achat.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testFacture_achat.getPiece_jointe()).isEqualTo(DEFAULT_PIECE_JOINTE);
        assertThat(testFacture_achat.getPiece_jointeContentType()).isEqualTo(DEFAULT_PIECE_JOINTE_CONTENT_TYPE);

        // Validate the Facture_achat in Elasticsearch
        Facture_achat facture_achatEs = facture_achatSearchRepository.findOne(testFacture_achat.getId());
        assertThat(facture_achatEs).isEqualToComparingFieldByField(testFacture_achat);
    }

    @Test
    @Transactional
    public void createFacture_achatWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = facture_achatRepository.findAll().size();

        // Create the Facture_achat with an existing ID
        facture_achat.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacture_achatMockMvc.perform(post("/api/facture-achats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facture_achat)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Facture_achat> facture_achatList = facture_achatRepository.findAll();
        assertThat(facture_achatList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkReferanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = facture_achatRepository.findAll().size();
        // set the field null
        facture_achat.setReferance(null);

        // Create the Facture_achat, which fails.

        restFacture_achatMockMvc.perform(post("/api/facture-achats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facture_achat)))
            .andExpect(status().isBadRequest());

        List<Facture_achat> facture_achatList = facture_achatRepository.findAll();
        assertThat(facture_achatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDate_factureIsRequired() throws Exception {
        int databaseSizeBeforeTest = facture_achatRepository.findAll().size();
        // set the field null
        facture_achat.setDate_facture(null);

        // Create the Facture_achat, which fails.

        restFacture_achatMockMvc.perform(post("/api/facture-achats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facture_achat)))
            .andExpect(status().isBadRequest());

        List<Facture_achat> facture_achatList = facture_achatRepository.findAll();
        assertThat(facture_achatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEcheanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = facture_achatRepository.findAll().size();
        // set the field null
        facture_achat.setEcheance(null);

        // Create the Facture_achat, which fails.

        restFacture_achatMockMvc.perform(post("/api/facture-achats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facture_achat)))
            .andExpect(status().isBadRequest());

        List<Facture_achat> facture_achatList = facture_achatRepository.findAll();
        assertThat(facture_achatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMontantIsRequired() throws Exception {
        int databaseSizeBeforeTest = facture_achatRepository.findAll().size();
        // set the field null
        facture_achat.setMontant(null);

        // Create the Facture_achat, which fails.

        restFacture_achatMockMvc.perform(post("/api/facture-achats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facture_achat)))
            .andExpect(status().isBadRequest());

        List<Facture_achat> facture_achatList = facture_achatRepository.findAll();
        assertThat(facture_achatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPiece_jointeIsRequired() throws Exception {
        int databaseSizeBeforeTest = facture_achatRepository.findAll().size();
        // set the field null
        facture_achat.setPiece_jointe(null);

        // Create the Facture_achat, which fails.

        restFacture_achatMockMvc.perform(post("/api/facture-achats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facture_achat)))
            .andExpect(status().isBadRequest());

        List<Facture_achat> facture_achatList = facture_achatRepository.findAll();
        assertThat(facture_achatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFacture_achats() throws Exception {
        // Initialize the database
        facture_achatRepository.saveAndFlush(facture_achat);

        // Get all the facture_achatList
        restFacture_achatMockMvc.perform(get("/api/facture-achats?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facture_achat.getId().intValue())))
            .andExpect(jsonPath("$.[*].referance").value(hasItem(DEFAULT_REFERANCE.toString())))
            .andExpect(jsonPath("$.[*].date_facture").value(hasItem(DEFAULT_DATE_FACTURE.toString())))
            .andExpect(jsonPath("$.[*].echeance").value(hasItem(DEFAULT_ECHEANCE.toString())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())))
            .andExpect(jsonPath("$.[*].piece_jointeContentType").value(hasItem(DEFAULT_PIECE_JOINTE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].piece_jointe").value(hasItem(Base64Utils.encodeToString(DEFAULT_PIECE_JOINTE))));
    }

    @Test
    @Transactional
    public void getFacture_achat() throws Exception {
        // Initialize the database
        facture_achatRepository.saveAndFlush(facture_achat);

        // Get the facture_achat
        restFacture_achatMockMvc.perform(get("/api/facture-achats/{id}", facture_achat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(facture_achat.getId().intValue()))
            .andExpect(jsonPath("$.referance").value(DEFAULT_REFERANCE.toString()))
            .andExpect(jsonPath("$.date_facture").value(DEFAULT_DATE_FACTURE.toString()))
            .andExpect(jsonPath("$.echeance").value(DEFAULT_ECHEANCE.toString()))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.doubleValue()))
            .andExpect(jsonPath("$.piece_jointeContentType").value(DEFAULT_PIECE_JOINTE_CONTENT_TYPE))
            .andExpect(jsonPath("$.piece_jointe").value(Base64Utils.encodeToString(DEFAULT_PIECE_JOINTE)));
    }

    @Test
    @Transactional
    public void getNonExistingFacture_achat() throws Exception {
        // Get the facture_achat
        restFacture_achatMockMvc.perform(get("/api/facture-achats/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFacture_achat() throws Exception {
        // Initialize the database
        facture_achatRepository.saveAndFlush(facture_achat);
        facture_achatSearchRepository.save(facture_achat);
        int databaseSizeBeforeUpdate = facture_achatRepository.findAll().size();

        // Update the facture_achat
        Facture_achat updatedFacture_achat = facture_achatRepository.findOne(facture_achat.getId());
        updatedFacture_achat
            .referance(UPDATED_REFERANCE)
            .date_facture(UPDATED_DATE_FACTURE)
            .echeance(UPDATED_ECHEANCE)
            .montant(UPDATED_MONTANT)
            .piece_jointe(UPDATED_PIECE_JOINTE)
            .piece_jointeContentType(UPDATED_PIECE_JOINTE_CONTENT_TYPE);

        restFacture_achatMockMvc.perform(put("/api/facture-achats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFacture_achat)))
            .andExpect(status().isOk());

        // Validate the Facture_achat in the database
        List<Facture_achat> facture_achatList = facture_achatRepository.findAll();
        assertThat(facture_achatList).hasSize(databaseSizeBeforeUpdate);
        Facture_achat testFacture_achat = facture_achatList.get(facture_achatList.size() - 1);
        assertThat(testFacture_achat.getReferance()).isEqualTo(UPDATED_REFERANCE);
        assertThat(testFacture_achat.getDate_facture()).isEqualTo(UPDATED_DATE_FACTURE);
        assertThat(testFacture_achat.getEcheance()).isEqualTo(UPDATED_ECHEANCE);
        assertThat(testFacture_achat.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testFacture_achat.getPiece_jointe()).isEqualTo(UPDATED_PIECE_JOINTE);
        assertThat(testFacture_achat.getPiece_jointeContentType()).isEqualTo(UPDATED_PIECE_JOINTE_CONTENT_TYPE);

        // Validate the Facture_achat in Elasticsearch
        Facture_achat facture_achatEs = facture_achatSearchRepository.findOne(testFacture_achat.getId());
        assertThat(facture_achatEs).isEqualToComparingFieldByField(testFacture_achat);
    }

    @Test
    @Transactional
    public void updateNonExistingFacture_achat() throws Exception {
        int databaseSizeBeforeUpdate = facture_achatRepository.findAll().size();

        // Create the Facture_achat

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFacture_achatMockMvc.perform(put("/api/facture-achats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facture_achat)))
            .andExpect(status().isCreated());

        // Validate the Facture_achat in the database
        List<Facture_achat> facture_achatList = facture_achatRepository.findAll();
        assertThat(facture_achatList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFacture_achat() throws Exception {
        // Initialize the database
        facture_achatRepository.saveAndFlush(facture_achat);
        facture_achatSearchRepository.save(facture_achat);
        int databaseSizeBeforeDelete = facture_achatRepository.findAll().size();

        // Get the facture_achat
        restFacture_achatMockMvc.perform(delete("/api/facture-achats/{id}", facture_achat.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean facture_achatExistsInEs = facture_achatSearchRepository.exists(facture_achat.getId());
        assertThat(facture_achatExistsInEs).isFalse();

        // Validate the database is empty
        List<Facture_achat> facture_achatList = facture_achatRepository.findAll();
        assertThat(facture_achatList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFacture_achat() throws Exception {
        // Initialize the database
        facture_achatRepository.saveAndFlush(facture_achat);
        facture_achatSearchRepository.save(facture_achat);

        // Search the facture_achat
        restFacture_achatMockMvc.perform(get("/api/_search/facture-achats?query=id:" + facture_achat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facture_achat.getId().intValue())))
            .andExpect(jsonPath("$.[*].referance").value(hasItem(DEFAULT_REFERANCE.toString())))
            .andExpect(jsonPath("$.[*].date_facture").value(hasItem(DEFAULT_DATE_FACTURE.toString())))
            .andExpect(jsonPath("$.[*].echeance").value(hasItem(DEFAULT_ECHEANCE.toString())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())))
            .andExpect(jsonPath("$.[*].piece_jointeContentType").value(hasItem(DEFAULT_PIECE_JOINTE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].piece_jointe").value(hasItem(Base64Utils.encodeToString(DEFAULT_PIECE_JOINTE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Facture_achat.class);
    }
}

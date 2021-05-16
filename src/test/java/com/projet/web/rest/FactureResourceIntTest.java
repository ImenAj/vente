package com.projet.web.rest;

import com.projet.PfeApp;

import com.projet.domain.Facture;
import com.projet.domain.Client;
import com.projet.repository.FactureRepository;
import com.projet.repository.search.FactureSearchRepository;
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
 * Test class for the FactureResource REST controller.
 *
 * @see FactureResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PfeApp.class)
public class FactureResourceIntTest {

    private static final Integer DEFAULT_NUMERO_FACTURE = 1;
    private static final Integer UPDATED_NUMERO_FACTURE = 2;

    private static final LocalDate DEFAULT_DATE_FACTURE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FACTURE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITE = 1;
    private static final Integer UPDATED_QUANTITE = 2;

    private static final Double DEFAULT_PRIX_UHT = 1D;
    private static final Double UPDATED_PRIX_UHT = 2D;

    private static final String DEFAULT_REFERANCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERANCE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PIECE_JOINTE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PIECE_JOINTE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_PIECE_JOINTE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PIECE_JOINTE_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_SORTIE = false;
    private static final Boolean UPDATED_SORTIE = true;

    @Autowired
    private FactureRepository factureRepository;

    @Autowired
    private FactureSearchRepository factureSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFactureMockMvc;

    private Facture facture;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FactureResource factureResource = new FactureResource(factureRepository, factureSearchRepository);
        this.restFactureMockMvc = MockMvcBuilders.standaloneSetup(factureResource)
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
    public static Facture createEntity(EntityManager em) {
        Facture facture = new Facture()
            .numeroFacture(DEFAULT_NUMERO_FACTURE)
            .dateFacture(DEFAULT_DATE_FACTURE)
            .designation(DEFAULT_DESIGNATION)
            .quantite(DEFAULT_QUANTITE)
            .prix_UHT(DEFAULT_PRIX_UHT)
            .referance(DEFAULT_REFERANCE)
            .piece_jointe(DEFAULT_PIECE_JOINTE)
            .piece_jointeContentType(DEFAULT_PIECE_JOINTE_CONTENT_TYPE)
            .sortie(DEFAULT_SORTIE);
        // Add required entity
        Client client = ClientResourceIntTest.createEntity(em);
        em.persist(client);
        em.flush();
        facture.setClient(client);
        return facture;
    }

    @Before
    public void initTest() {
        factureSearchRepository.deleteAll();
        facture = createEntity(em);
    }

    @Test
    @Transactional
    public void createFacture() throws Exception {
        int databaseSizeBeforeCreate = factureRepository.findAll().size();

        // Create the Facture
        restFactureMockMvc.perform(post("/api/factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facture)))
            .andExpect(status().isCreated());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeCreate + 1);
        Facture testFacture = factureList.get(factureList.size() - 1);
        assertThat(testFacture.getNumeroFacture()).isEqualTo(DEFAULT_NUMERO_FACTURE);
        assertThat(testFacture.getDateFacture()).isEqualTo(DEFAULT_DATE_FACTURE);
        assertThat(testFacture.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testFacture.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testFacture.getPrix_UHT()).isEqualTo(DEFAULT_PRIX_UHT);
        assertThat(testFacture.getReferance()).isEqualTo(DEFAULT_REFERANCE);
        assertThat(testFacture.getPiece_jointe()).isEqualTo(DEFAULT_PIECE_JOINTE);
        assertThat(testFacture.getPiece_jointeContentType()).isEqualTo(DEFAULT_PIECE_JOINTE_CONTENT_TYPE);
        assertThat(testFacture.isSortie()).isEqualTo(DEFAULT_SORTIE);

        // Validate the Facture in Elasticsearch
        Facture factureEs = factureSearchRepository.findOne(testFacture.getId());
        assertThat(factureEs).isEqualToComparingFieldByField(testFacture);
    }

    @Test
    @Transactional
    public void createFactureWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = factureRepository.findAll().size();

        // Create the Facture with an existing ID
        facture.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFactureMockMvc.perform(post("/api/factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facture)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNumeroFactureIsRequired() throws Exception {
        int databaseSizeBeforeTest = factureRepository.findAll().size();
        // set the field null
        facture.setNumeroFacture(null);

        // Create the Facture, which fails.

        restFactureMockMvc.perform(post("/api/factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facture)))
            .andExpect(status().isBadRequest());

        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateFactureIsRequired() throws Exception {
        int databaseSizeBeforeTest = factureRepository.findAll().size();
        // set the field null
        facture.setDateFacture(null);

        // Create the Facture, which fails.

        restFactureMockMvc.perform(post("/api/factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facture)))
            .andExpect(status().isBadRequest());

        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDesignationIsRequired() throws Exception {
        int databaseSizeBeforeTest = factureRepository.findAll().size();
        // set the field null
        facture.setDesignation(null);

        // Create the Facture, which fails.

        restFactureMockMvc.perform(post("/api/factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facture)))
            .andExpect(status().isBadRequest());

        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantiteIsRequired() throws Exception {
        int databaseSizeBeforeTest = factureRepository.findAll().size();
        // set the field null
        facture.setQuantite(null);

        // Create the Facture, which fails.

        restFactureMockMvc.perform(post("/api/factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facture)))
            .andExpect(status().isBadRequest());

        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrix_UHTIsRequired() throws Exception {
        int databaseSizeBeforeTest = factureRepository.findAll().size();
        // set the field null
        facture.setPrix_UHT(null);

        // Create the Facture, which fails.

        restFactureMockMvc.perform(post("/api/factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facture)))
            .andExpect(status().isBadRequest());

        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReferanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = factureRepository.findAll().size();
        // set the field null
        facture.setReferance(null);

        // Create the Facture, which fails.

        restFactureMockMvc.perform(post("/api/factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facture)))
            .andExpect(status().isBadRequest());

        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPiece_jointeIsRequired() throws Exception {
        int databaseSizeBeforeTest = factureRepository.findAll().size();
        // set the field null
        facture.setPiece_jointe(null);

        // Create the Facture, which fails.

        restFactureMockMvc.perform(post("/api/factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facture)))
            .andExpect(status().isBadRequest());

        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFactures() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList
        restFactureMockMvc.perform(get("/api/factures?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facture.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroFacture").value(hasItem(DEFAULT_NUMERO_FACTURE)))
            .andExpect(jsonPath("$.[*].dateFacture").value(hasItem(DEFAULT_DATE_FACTURE.toString())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION.toString())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.[*].prix_UHT").value(hasItem(DEFAULT_PRIX_UHT.doubleValue())))
            .andExpect(jsonPath("$.[*].referance").value(hasItem(DEFAULT_REFERANCE.toString())))
            .andExpect(jsonPath("$.[*].piece_jointeContentType").value(hasItem(DEFAULT_PIECE_JOINTE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].piece_jointe").value(hasItem(Base64Utils.encodeToString(DEFAULT_PIECE_JOINTE))))
            .andExpect(jsonPath("$.[*].sortie").value(hasItem(DEFAULT_SORTIE.booleanValue())));
    }

    @Test
    @Transactional
    public void getFacture() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get the facture
        restFactureMockMvc.perform(get("/api/factures/{id}", facture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(facture.getId().intValue()))
            .andExpect(jsonPath("$.numeroFacture").value(DEFAULT_NUMERO_FACTURE))
            .andExpect(jsonPath("$.dateFacture").value(DEFAULT_DATE_FACTURE.toString()))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION.toString()))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE))
            .andExpect(jsonPath("$.prix_UHT").value(DEFAULT_PRIX_UHT.doubleValue()))
            .andExpect(jsonPath("$.referance").value(DEFAULT_REFERANCE.toString()))
            .andExpect(jsonPath("$.piece_jointeContentType").value(DEFAULT_PIECE_JOINTE_CONTENT_TYPE))
            .andExpect(jsonPath("$.piece_jointe").value(Base64Utils.encodeToString(DEFAULT_PIECE_JOINTE)))
            .andExpect(jsonPath("$.sortie").value(DEFAULT_SORTIE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFacture() throws Exception {
        // Get the facture
        restFactureMockMvc.perform(get("/api/factures/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFacture() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);
        factureSearchRepository.save(facture);
        int databaseSizeBeforeUpdate = factureRepository.findAll().size();

        // Update the facture
        Facture updatedFacture = factureRepository.findOne(facture.getId());
        updatedFacture
            .numeroFacture(UPDATED_NUMERO_FACTURE)
            .dateFacture(UPDATED_DATE_FACTURE)
            .designation(UPDATED_DESIGNATION)
            .quantite(UPDATED_QUANTITE)
            .prix_UHT(UPDATED_PRIX_UHT)
            .referance(UPDATED_REFERANCE)
            .piece_jointe(UPDATED_PIECE_JOINTE)
            .piece_jointeContentType(UPDATED_PIECE_JOINTE_CONTENT_TYPE)
            .sortie(UPDATED_SORTIE);

        restFactureMockMvc.perform(put("/api/factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFacture)))
            .andExpect(status().isOk());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeUpdate);
        Facture testFacture = factureList.get(factureList.size() - 1);
        assertThat(testFacture.getNumeroFacture()).isEqualTo(UPDATED_NUMERO_FACTURE);
        assertThat(testFacture.getDateFacture()).isEqualTo(UPDATED_DATE_FACTURE);
        assertThat(testFacture.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testFacture.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testFacture.getPrix_UHT()).isEqualTo(UPDATED_PRIX_UHT);
        assertThat(testFacture.getReferance()).isEqualTo(UPDATED_REFERANCE);
        assertThat(testFacture.getPiece_jointe()).isEqualTo(UPDATED_PIECE_JOINTE);
        assertThat(testFacture.getPiece_jointeContentType()).isEqualTo(UPDATED_PIECE_JOINTE_CONTENT_TYPE);
        assertThat(testFacture.isSortie()).isEqualTo(UPDATED_SORTIE);

        // Validate the Facture in Elasticsearch
        Facture factureEs = factureSearchRepository.findOne(testFacture.getId());
        assertThat(factureEs).isEqualToComparingFieldByField(testFacture);
    }

    @Test
    @Transactional
    public void updateNonExistingFacture() throws Exception {
        int databaseSizeBeforeUpdate = factureRepository.findAll().size();

        // Create the Facture

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFactureMockMvc.perform(put("/api/factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facture)))
            .andExpect(status().isCreated());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFacture() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);
        factureSearchRepository.save(facture);
        int databaseSizeBeforeDelete = factureRepository.findAll().size();

        // Get the facture
        restFactureMockMvc.perform(delete("/api/factures/{id}", facture.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean factureExistsInEs = factureSearchRepository.exists(facture.getId());
        assertThat(factureExistsInEs).isFalse();

        // Validate the database is empty
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFacture() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);
        factureSearchRepository.save(facture);

        // Search the facture
        restFactureMockMvc.perform(get("/api/_search/factures?query=id:" + facture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facture.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroFacture").value(hasItem(DEFAULT_NUMERO_FACTURE)))
            .andExpect(jsonPath("$.[*].dateFacture").value(hasItem(DEFAULT_DATE_FACTURE.toString())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION.toString())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.[*].prix_UHT").value(hasItem(DEFAULT_PRIX_UHT.doubleValue())))
            .andExpect(jsonPath("$.[*].referance").value(hasItem(DEFAULT_REFERANCE.toString())))
            .andExpect(jsonPath("$.[*].piece_jointeContentType").value(hasItem(DEFAULT_PIECE_JOINTE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].piece_jointe").value(hasItem(Base64Utils.encodeToString(DEFAULT_PIECE_JOINTE))))
            .andExpect(jsonPath("$.[*].sortie").value(hasItem(DEFAULT_SORTIE.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Facture.class);
    }
}

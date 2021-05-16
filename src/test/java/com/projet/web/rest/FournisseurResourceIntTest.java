package com.projet.web.rest;

import com.projet.PfeApp;

import com.projet.domain.Fournisseur;
import com.projet.repository.FournisseurRepository;
import com.projet.repository.search.FournisseurSearchRepository;
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
 * Test class for the FournisseurResource REST controller.
 *
 * @see FournisseurResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PfeApp.class)
public class FournisseurResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_MATRICULE_FISCAL = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULE_FISCAL = "BBBBBBBBBB";

    private static final String DEFAULT_RAISON_SOCIAL = "AAAAAAAAAA";
    private static final String UPDATED_RAISON_SOCIAL = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADRESS = "BBBBBBBBBB";

    private static final String DEFAULT_VILLE = "AAAAAAAAAA";
    private static final String UPDATED_VILLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_CODE_POSTAL = 1;
    private static final Integer UPDATED_CODE_POSTAL = 2;

    private static final Integer DEFAULT_TEL = 1;
    private static final Integer UPDATED_TEL = 2;

    private static final Integer DEFAULT_FAX = 1;
    private static final Integer UPDATED_FAX = 2;

    private static final String DEFAULT_SITE_WEB = "AAAAAAAAAA";
    private static final String UPDATED_SITE_WEB = "BBBBBBBBBB";

    @Autowired
    private FournisseurRepository fournisseurRepository;

    @Autowired
    private FournisseurSearchRepository fournisseurSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFournisseurMockMvc;

    private Fournisseur fournisseur;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FournisseurResource fournisseurResource = new FournisseurResource(fournisseurRepository, fournisseurSearchRepository);
        this.restFournisseurMockMvc = MockMvcBuilders.standaloneSetup(fournisseurResource)
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
    public static Fournisseur createEntity(EntityManager em) {
        Fournisseur fournisseur = new Fournisseur()
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .matricule_fiscal(DEFAULT_MATRICULE_FISCAL)
            .raison_social(DEFAULT_RAISON_SOCIAL)
            .adress(DEFAULT_ADRESS)
            .ville(DEFAULT_VILLE)
            .code_postal(DEFAULT_CODE_POSTAL)
            .tel(DEFAULT_TEL)
            .fax(DEFAULT_FAX)
            .site_web(DEFAULT_SITE_WEB);
        return fournisseur;
    }

    @Before
    public void initTest() {
        fournisseurSearchRepository.deleteAll();
        fournisseur = createEntity(em);
    }

    @Test
    @Transactional
    public void createFournisseur() throws Exception {
        int databaseSizeBeforeCreate = fournisseurRepository.findAll().size();

        // Create the Fournisseur
        restFournisseurMockMvc.perform(post("/api/fournisseurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fournisseur)))
            .andExpect(status().isCreated());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeCreate + 1);
        Fournisseur testFournisseur = fournisseurList.get(fournisseurList.size() - 1);
        assertThat(testFournisseur.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testFournisseur.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testFournisseur.getMatricule_fiscal()).isEqualTo(DEFAULT_MATRICULE_FISCAL);
        assertThat(testFournisseur.getRaison_social()).isEqualTo(DEFAULT_RAISON_SOCIAL);
        assertThat(testFournisseur.getAdress()).isEqualTo(DEFAULT_ADRESS);
        assertThat(testFournisseur.getVille()).isEqualTo(DEFAULT_VILLE);
        assertThat(testFournisseur.getCode_postal()).isEqualTo(DEFAULT_CODE_POSTAL);
        assertThat(testFournisseur.getTel()).isEqualTo(DEFAULT_TEL);
        assertThat(testFournisseur.getFax()).isEqualTo(DEFAULT_FAX);
        assertThat(testFournisseur.getSite_web()).isEqualTo(DEFAULT_SITE_WEB);

        // Validate the Fournisseur in Elasticsearch
        Fournisseur fournisseurEs = fournisseurSearchRepository.findOne(testFournisseur.getId());
        assertThat(fournisseurEs).isEqualToComparingFieldByField(testFournisseur);
    }

    @Test
    @Transactional
    public void createFournisseurWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fournisseurRepository.findAll().size();

        // Create the Fournisseur with an existing ID
        fournisseur.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFournisseurMockMvc.perform(post("/api/fournisseurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fournisseur)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = fournisseurRepository.findAll().size();
        // set the field null
        fournisseur.setNom(null);

        // Create the Fournisseur, which fails.

        restFournisseurMockMvc.perform(post("/api/fournisseurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fournisseur)))
            .andExpect(status().isBadRequest());

        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrenomIsRequired() throws Exception {
        int databaseSizeBeforeTest = fournisseurRepository.findAll().size();
        // set the field null
        fournisseur.setPrenom(null);

        // Create the Fournisseur, which fails.

        restFournisseurMockMvc.perform(post("/api/fournisseurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fournisseur)))
            .andExpect(status().isBadRequest());

        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMatricule_fiscalIsRequired() throws Exception {
        int databaseSizeBeforeTest = fournisseurRepository.findAll().size();
        // set the field null
        fournisseur.setMatricule_fiscal(null);

        // Create the Fournisseur, which fails.

        restFournisseurMockMvc.perform(post("/api/fournisseurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fournisseur)))
            .andExpect(status().isBadRequest());

        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRaison_socialIsRequired() throws Exception {
        int databaseSizeBeforeTest = fournisseurRepository.findAll().size();
        // set the field null
        fournisseur.setRaison_social(null);

        // Create the Fournisseur, which fails.

        restFournisseurMockMvc.perform(post("/api/fournisseurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fournisseur)))
            .andExpect(status().isBadRequest());

        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAdressIsRequired() throws Exception {
        int databaseSizeBeforeTest = fournisseurRepository.findAll().size();
        // set the field null
        fournisseur.setAdress(null);

        // Create the Fournisseur, which fails.

        restFournisseurMockMvc.perform(post("/api/fournisseurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fournisseur)))
            .andExpect(status().isBadRequest());

        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVilleIsRequired() throws Exception {
        int databaseSizeBeforeTest = fournisseurRepository.findAll().size();
        // set the field null
        fournisseur.setVille(null);

        // Create the Fournisseur, which fails.

        restFournisseurMockMvc.perform(post("/api/fournisseurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fournisseur)))
            .andExpect(status().isBadRequest());

        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCode_postalIsRequired() throws Exception {
        int databaseSizeBeforeTest = fournisseurRepository.findAll().size();
        // set the field null
        fournisseur.setCode_postal(null);

        // Create the Fournisseur, which fails.

        restFournisseurMockMvc.perform(post("/api/fournisseurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fournisseur)))
            .andExpect(status().isBadRequest());

        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTelIsRequired() throws Exception {
        int databaseSizeBeforeTest = fournisseurRepository.findAll().size();
        // set the field null
        fournisseur.setTel(null);

        // Create the Fournisseur, which fails.

        restFournisseurMockMvc.perform(post("/api/fournisseurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fournisseur)))
            .andExpect(status().isBadRequest());

        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFournisseurs() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList
        restFournisseurMockMvc.perform(get("/api/fournisseurs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fournisseur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM.toString())))
            .andExpect(jsonPath("$.[*].matricule_fiscal").value(hasItem(DEFAULT_MATRICULE_FISCAL.toString())))
            .andExpect(jsonPath("$.[*].raison_social").value(hasItem(DEFAULT_RAISON_SOCIAL.toString())))
            .andExpect(jsonPath("$.[*].adress").value(hasItem(DEFAULT_ADRESS.toString())))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE.toString())))
            .andExpect(jsonPath("$.[*].code_postal").value(hasItem(DEFAULT_CODE_POSTAL)))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL)))
            .andExpect(jsonPath("$.[*].fax").value(hasItem(DEFAULT_FAX)))
            .andExpect(jsonPath("$.[*].site_web").value(hasItem(DEFAULT_SITE_WEB.toString())));
    }

    @Test
    @Transactional
    public void getFournisseur() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get the fournisseur
        restFournisseurMockMvc.perform(get("/api/fournisseurs/{id}", fournisseur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(fournisseur.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM.toString()))
            .andExpect(jsonPath("$.matricule_fiscal").value(DEFAULT_MATRICULE_FISCAL.toString()))
            .andExpect(jsonPath("$.raison_social").value(DEFAULT_RAISON_SOCIAL.toString()))
            .andExpect(jsonPath("$.adress").value(DEFAULT_ADRESS.toString()))
            .andExpect(jsonPath("$.ville").value(DEFAULT_VILLE.toString()))
            .andExpect(jsonPath("$.code_postal").value(DEFAULT_CODE_POSTAL))
            .andExpect(jsonPath("$.tel").value(DEFAULT_TEL))
            .andExpect(jsonPath("$.fax").value(DEFAULT_FAX))
            .andExpect(jsonPath("$.site_web").value(DEFAULT_SITE_WEB.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFournisseur() throws Exception {
        // Get the fournisseur
        restFournisseurMockMvc.perform(get("/api/fournisseurs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFournisseur() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);
        fournisseurSearchRepository.save(fournisseur);
        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().size();

        // Update the fournisseur
        Fournisseur updatedFournisseur = fournisseurRepository.findOne(fournisseur.getId());
        updatedFournisseur
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .matricule_fiscal(UPDATED_MATRICULE_FISCAL)
            .raison_social(UPDATED_RAISON_SOCIAL)
            .adress(UPDATED_ADRESS)
            .ville(UPDATED_VILLE)
            .code_postal(UPDATED_CODE_POSTAL)
            .tel(UPDATED_TEL)
            .fax(UPDATED_FAX)
            .site_web(UPDATED_SITE_WEB);

        restFournisseurMockMvc.perform(put("/api/fournisseurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFournisseur)))
            .andExpect(status().isOk());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeUpdate);
        Fournisseur testFournisseur = fournisseurList.get(fournisseurList.size() - 1);
        assertThat(testFournisseur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testFournisseur.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testFournisseur.getMatricule_fiscal()).isEqualTo(UPDATED_MATRICULE_FISCAL);
        assertThat(testFournisseur.getRaison_social()).isEqualTo(UPDATED_RAISON_SOCIAL);
        assertThat(testFournisseur.getAdress()).isEqualTo(UPDATED_ADRESS);
        assertThat(testFournisseur.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testFournisseur.getCode_postal()).isEqualTo(UPDATED_CODE_POSTAL);
        assertThat(testFournisseur.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testFournisseur.getFax()).isEqualTo(UPDATED_FAX);
        assertThat(testFournisseur.getSite_web()).isEqualTo(UPDATED_SITE_WEB);

        // Validate the Fournisseur in Elasticsearch
        Fournisseur fournisseurEs = fournisseurSearchRepository.findOne(testFournisseur.getId());
        assertThat(fournisseurEs).isEqualToComparingFieldByField(testFournisseur);
    }

    @Test
    @Transactional
    public void updateNonExistingFournisseur() throws Exception {
        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().size();

        // Create the Fournisseur

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFournisseurMockMvc.perform(put("/api/fournisseurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fournisseur)))
            .andExpect(status().isCreated());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFournisseur() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);
        fournisseurSearchRepository.save(fournisseur);
        int databaseSizeBeforeDelete = fournisseurRepository.findAll().size();

        // Get the fournisseur
        restFournisseurMockMvc.perform(delete("/api/fournisseurs/{id}", fournisseur.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean fournisseurExistsInEs = fournisseurSearchRepository.exists(fournisseur.getId());
        assertThat(fournisseurExistsInEs).isFalse();

        // Validate the database is empty
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFournisseur() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);
        fournisseurSearchRepository.save(fournisseur);

        // Search the fournisseur
        restFournisseurMockMvc.perform(get("/api/_search/fournisseurs?query=id:" + fournisseur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fournisseur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM.toString())))
            .andExpect(jsonPath("$.[*].matricule_fiscal").value(hasItem(DEFAULT_MATRICULE_FISCAL.toString())))
            .andExpect(jsonPath("$.[*].raison_social").value(hasItem(DEFAULT_RAISON_SOCIAL.toString())))
            .andExpect(jsonPath("$.[*].adress").value(hasItem(DEFAULT_ADRESS.toString())))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE.toString())))
            .andExpect(jsonPath("$.[*].code_postal").value(hasItem(DEFAULT_CODE_POSTAL)))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL)))
            .andExpect(jsonPath("$.[*].fax").value(hasItem(DEFAULT_FAX)))
            .andExpect(jsonPath("$.[*].site_web").value(hasItem(DEFAULT_SITE_WEB.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Fournisseur.class);
    }
}

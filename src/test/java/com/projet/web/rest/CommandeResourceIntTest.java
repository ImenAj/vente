package com.projet.web.rest;

import com.projet.PfeApp;

import com.projet.domain.Commande;
import com.projet.repository.CommandeRepository;
import com.projet.repository.search.CommandeSearchRepository;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.projet.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CommandeResource REST controller.
 *
 * @see CommandeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PfeApp.class)
public class CommandeResourceIntTest {

    private static final ZonedDateTime DEFAULT_DATE_COMMANDE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_COMMANDE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PIECE_JOINTE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PIECE_JOINTE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_PIECE_JOINTE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PIECE_JOINTE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    @Autowired
    private CommandeRepository commandeRepository;

    @Autowired
    private CommandeSearchRepository commandeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCommandeMockMvc;

    private Commande commande;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CommandeResource commandeResource = new CommandeResource(commandeRepository, commandeSearchRepository);
        this.restCommandeMockMvc = MockMvcBuilders.standaloneSetup(commandeResource)
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
    public static Commande createEntity(EntityManager em) {
        Commande commande = new Commande()
            .date_commande(DEFAULT_DATE_COMMANDE)
            .designation(DEFAULT_DESIGNATION)
            .piece_jointe(DEFAULT_PIECE_JOINTE)
            .piece_jointeContentType(DEFAULT_PIECE_JOINTE_CONTENT_TYPE)
            .reference(DEFAULT_REFERENCE);
        return commande;
    }

    @Before
    public void initTest() {
        commandeSearchRepository.deleteAll();
        commande = createEntity(em);
    }

    @Test
    @Transactional
    public void createCommande() throws Exception {
        int databaseSizeBeforeCreate = commandeRepository.findAll().size();

        // Create the Commande
        restCommandeMockMvc.perform(post("/api/commandes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commande)))
            .andExpect(status().isCreated());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeCreate + 1);
        Commande testCommande = commandeList.get(commandeList.size() - 1);
        assertThat(testCommande.getDate_commande()).isEqualTo(DEFAULT_DATE_COMMANDE);
        assertThat(testCommande.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testCommande.getPiece_jointe()).isEqualTo(DEFAULT_PIECE_JOINTE);
        assertThat(testCommande.getPiece_jointeContentType()).isEqualTo(DEFAULT_PIECE_JOINTE_CONTENT_TYPE);
        assertThat(testCommande.getReference()).isEqualTo(DEFAULT_REFERENCE);

        // Validate the Commande in Elasticsearch
        Commande commandeEs = commandeSearchRepository.findOne(testCommande.getId());
        assertThat(commandeEs).isEqualToComparingFieldByField(testCommande);
    }

    @Test
    @Transactional
    public void createCommandeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = commandeRepository.findAll().size();

        // Create the Commande with an existing ID
        commande.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommandeMockMvc.perform(post("/api/commandes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commande)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDate_commandeIsRequired() throws Exception {
        int databaseSizeBeforeTest = commandeRepository.findAll().size();
        // set the field null
        commande.setDate_commande(null);

        // Create the Commande, which fails.

        restCommandeMockMvc.perform(post("/api/commandes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commande)))
            .andExpect(status().isBadRequest());

        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCommandes() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList
        restCommandeMockMvc.perform(get("/api/commandes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commande.getId().intValue())))
            .andExpect(jsonPath("$.[*].date_commande").value(hasItem(sameInstant(DEFAULT_DATE_COMMANDE))))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION.toString())))
            .andExpect(jsonPath("$.[*].piece_jointeContentType").value(hasItem(DEFAULT_PIECE_JOINTE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].piece_jointe").value(hasItem(Base64Utils.encodeToString(DEFAULT_PIECE_JOINTE))))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE.toString())));
    }

    @Test
    @Transactional
    public void getCommande() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get the commande
        restCommandeMockMvc.perform(get("/api/commandes/{id}", commande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(commande.getId().intValue()))
            .andExpect(jsonPath("$.date_commande").value(sameInstant(DEFAULT_DATE_COMMANDE)))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION.toString()))
            .andExpect(jsonPath("$.piece_jointeContentType").value(DEFAULT_PIECE_JOINTE_CONTENT_TYPE))
            .andExpect(jsonPath("$.piece_jointe").value(Base64Utils.encodeToString(DEFAULT_PIECE_JOINTE)))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCommande() throws Exception {
        // Get the commande
        restCommandeMockMvc.perform(get("/api/commandes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCommande() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);
        commandeSearchRepository.save(commande);
        int databaseSizeBeforeUpdate = commandeRepository.findAll().size();

        // Update the commande
        Commande updatedCommande = commandeRepository.findOne(commande.getId());
        updatedCommande
            .date_commande(UPDATED_DATE_COMMANDE)
            .designation(UPDATED_DESIGNATION)
            .piece_jointe(UPDATED_PIECE_JOINTE)
            .piece_jointeContentType(UPDATED_PIECE_JOINTE_CONTENT_TYPE)
            .reference(UPDATED_REFERENCE);

        restCommandeMockMvc.perform(put("/api/commandes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCommande)))
            .andExpect(status().isOk());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
        Commande testCommande = commandeList.get(commandeList.size() - 1);
        assertThat(testCommande.getDate_commande()).isEqualTo(UPDATED_DATE_COMMANDE);
        assertThat(testCommande.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testCommande.getPiece_jointe()).isEqualTo(UPDATED_PIECE_JOINTE);
        assertThat(testCommande.getPiece_jointeContentType()).isEqualTo(UPDATED_PIECE_JOINTE_CONTENT_TYPE);
        assertThat(testCommande.getReference()).isEqualTo(UPDATED_REFERENCE);

        // Validate the Commande in Elasticsearch
        Commande commandeEs = commandeSearchRepository.findOne(testCommande.getId());
        assertThat(commandeEs).isEqualToComparingFieldByField(testCommande);
    }

    @Test
    @Transactional
    public void updateNonExistingCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().size();

        // Create the Commande

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCommandeMockMvc.perform(put("/api/commandes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commande)))
            .andExpect(status().isCreated());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCommande() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);
        commandeSearchRepository.save(commande);
        int databaseSizeBeforeDelete = commandeRepository.findAll().size();

        // Get the commande
        restCommandeMockMvc.perform(delete("/api/commandes/{id}", commande.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean commandeExistsInEs = commandeSearchRepository.exists(commande.getId());
        assertThat(commandeExistsInEs).isFalse();

        // Validate the database is empty
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCommande() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);
        commandeSearchRepository.save(commande);

        // Search the commande
        restCommandeMockMvc.perform(get("/api/_search/commandes?query=id:" + commande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commande.getId().intValue())))
            .andExpect(jsonPath("$.[*].date_commande").value(hasItem(sameInstant(DEFAULT_DATE_COMMANDE))))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION.toString())))
            .andExpect(jsonPath("$.[*].piece_jointeContentType").value(hasItem(DEFAULT_PIECE_JOINTE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].piece_jointe").value(hasItem(Base64Utils.encodeToString(DEFAULT_PIECE_JOINTE))))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Commande.class);
    }
}

package com.projet.web.rest;

import com.projet.PfeApp;

import com.projet.domain.CommandeClient;
import com.projet.repository.CommandeClientRepository;
import com.projet.repository.search.CommandeClientSearchRepository;
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
 * Test class for the CommandeClientResource REST controller.
 *
 * @see CommandeClientResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PfeApp.class)
public class CommandeClientResourceIntTest {

    private static final Integer DEFAULT_NUMERO_COMMANDE = 1;
    private static final Integer UPDATED_NUMERO_COMMANDE = 2;

    private static final LocalDate DEFAULT_DATE_COMMANDE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_COMMANDE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private CommandeClientRepository commandeClientRepository;

    @Autowired
    private CommandeClientSearchRepository commandeClientSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCommandeClientMockMvc;

    private CommandeClient commandeClient;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CommandeClientResource commandeClientResource = new CommandeClientResource(commandeClientRepository, commandeClientSearchRepository);
        this.restCommandeClientMockMvc = MockMvcBuilders.standaloneSetup(commandeClientResource)
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
    public static CommandeClient createEntity(EntityManager em) {
        CommandeClient commandeClient = new CommandeClient()
            .numeroCommande(DEFAULT_NUMERO_COMMANDE)
            .dateCommande(DEFAULT_DATE_COMMANDE);
        return commandeClient;
    }

    @Before
    public void initTest() {
        commandeClientSearchRepository.deleteAll();
        commandeClient = createEntity(em);
    }

    @Test
    @Transactional
    public void createCommandeClient() throws Exception {
        int databaseSizeBeforeCreate = commandeClientRepository.findAll().size();

        // Create the CommandeClient
        restCommandeClientMockMvc.perform(post("/api/commande-clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commandeClient)))
            .andExpect(status().isCreated());

        // Validate the CommandeClient in the database
        List<CommandeClient> commandeClientList = commandeClientRepository.findAll();
        assertThat(commandeClientList).hasSize(databaseSizeBeforeCreate + 1);
        CommandeClient testCommandeClient = commandeClientList.get(commandeClientList.size() - 1);
        assertThat(testCommandeClient.getNumeroCommande()).isEqualTo(DEFAULT_NUMERO_COMMANDE);
        assertThat(testCommandeClient.getDateCommande()).isEqualTo(DEFAULT_DATE_COMMANDE);

        // Validate the CommandeClient in Elasticsearch
        CommandeClient commandeClientEs = commandeClientSearchRepository.findOne(testCommandeClient.getId());
        assertThat(commandeClientEs).isEqualToComparingFieldByField(testCommandeClient);
    }

    @Test
    @Transactional
    public void createCommandeClientWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = commandeClientRepository.findAll().size();

        // Create the CommandeClient with an existing ID
        commandeClient.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommandeClientMockMvc.perform(post("/api/commande-clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commandeClient)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<CommandeClient> commandeClientList = commandeClientRepository.findAll();
        assertThat(commandeClientList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNumeroCommandeIsRequired() throws Exception {
        int databaseSizeBeforeTest = commandeClientRepository.findAll().size();
        // set the field null
        commandeClient.setNumeroCommande(null);

        // Create the CommandeClient, which fails.

        restCommandeClientMockMvc.perform(post("/api/commande-clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commandeClient)))
            .andExpect(status().isBadRequest());

        List<CommandeClient> commandeClientList = commandeClientRepository.findAll();
        assertThat(commandeClientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateCommandeIsRequired() throws Exception {
        int databaseSizeBeforeTest = commandeClientRepository.findAll().size();
        // set the field null
        commandeClient.setDateCommande(null);

        // Create the CommandeClient, which fails.

        restCommandeClientMockMvc.perform(post("/api/commande-clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commandeClient)))
            .andExpect(status().isBadRequest());

        List<CommandeClient> commandeClientList = commandeClientRepository.findAll();
        assertThat(commandeClientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCommandeClients() throws Exception {
        // Initialize the database
        commandeClientRepository.saveAndFlush(commandeClient);

        // Get all the commandeClientList
        restCommandeClientMockMvc.perform(get("/api/commande-clients?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commandeClient.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroCommande").value(hasItem(DEFAULT_NUMERO_COMMANDE)))
            .andExpect(jsonPath("$.[*].dateCommande").value(hasItem(DEFAULT_DATE_COMMANDE.toString())));
    }

    @Test
    @Transactional
    public void getCommandeClient() throws Exception {
        // Initialize the database
        commandeClientRepository.saveAndFlush(commandeClient);

        // Get the commandeClient
        restCommandeClientMockMvc.perform(get("/api/commande-clients/{id}", commandeClient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(commandeClient.getId().intValue()))
            .andExpect(jsonPath("$.numeroCommande").value(DEFAULT_NUMERO_COMMANDE))
            .andExpect(jsonPath("$.dateCommande").value(DEFAULT_DATE_COMMANDE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCommandeClient() throws Exception {
        // Get the commandeClient
        restCommandeClientMockMvc.perform(get("/api/commande-clients/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCommandeClient() throws Exception {
        // Initialize the database
        commandeClientRepository.saveAndFlush(commandeClient);
        commandeClientSearchRepository.save(commandeClient);
        int databaseSizeBeforeUpdate = commandeClientRepository.findAll().size();

        // Update the commandeClient
        CommandeClient updatedCommandeClient = commandeClientRepository.findOne(commandeClient.getId());
        updatedCommandeClient
            .numeroCommande(UPDATED_NUMERO_COMMANDE)
            .dateCommande(UPDATED_DATE_COMMANDE);

        restCommandeClientMockMvc.perform(put("/api/commande-clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCommandeClient)))
            .andExpect(status().isOk());

        // Validate the CommandeClient in the database
        List<CommandeClient> commandeClientList = commandeClientRepository.findAll();
        assertThat(commandeClientList).hasSize(databaseSizeBeforeUpdate);
        CommandeClient testCommandeClient = commandeClientList.get(commandeClientList.size() - 1);
        assertThat(testCommandeClient.getNumeroCommande()).isEqualTo(UPDATED_NUMERO_COMMANDE);
        assertThat(testCommandeClient.getDateCommande()).isEqualTo(UPDATED_DATE_COMMANDE);

        // Validate the CommandeClient in Elasticsearch
        CommandeClient commandeClientEs = commandeClientSearchRepository.findOne(testCommandeClient.getId());
        assertThat(commandeClientEs).isEqualToComparingFieldByField(testCommandeClient);
    }

    @Test
    @Transactional
    public void updateNonExistingCommandeClient() throws Exception {
        int databaseSizeBeforeUpdate = commandeClientRepository.findAll().size();

        // Create the CommandeClient

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCommandeClientMockMvc.perform(put("/api/commande-clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commandeClient)))
            .andExpect(status().isCreated());

        // Validate the CommandeClient in the database
        List<CommandeClient> commandeClientList = commandeClientRepository.findAll();
        assertThat(commandeClientList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCommandeClient() throws Exception {
        // Initialize the database
        commandeClientRepository.saveAndFlush(commandeClient);
        commandeClientSearchRepository.save(commandeClient);
        int databaseSizeBeforeDelete = commandeClientRepository.findAll().size();

        // Get the commandeClient
        restCommandeClientMockMvc.perform(delete("/api/commande-clients/{id}", commandeClient.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean commandeClientExistsInEs = commandeClientSearchRepository.exists(commandeClient.getId());
        assertThat(commandeClientExistsInEs).isFalse();

        // Validate the database is empty
        List<CommandeClient> commandeClientList = commandeClientRepository.findAll();
        assertThat(commandeClientList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCommandeClient() throws Exception {
        // Initialize the database
        commandeClientRepository.saveAndFlush(commandeClient);
        commandeClientSearchRepository.save(commandeClient);

        // Search the commandeClient
        restCommandeClientMockMvc.perform(get("/api/_search/commande-clients?query=id:" + commandeClient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commandeClient.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroCommande").value(hasItem(DEFAULT_NUMERO_COMMANDE)))
            .andExpect(jsonPath("$.[*].dateCommande").value(hasItem(DEFAULT_DATE_COMMANDE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommandeClient.class);
    }
}

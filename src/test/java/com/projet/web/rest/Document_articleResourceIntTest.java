package com.projet.web.rest;

import com.projet.PfeApp;

import com.projet.domain.Document_article;
import com.projet.repository.Document_articleRepository;
import com.projet.repository.search.Document_articleSearchRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the Document_articleResource REST controller.
 *
 * @see Document_articleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PfeApp.class)
public class Document_articleResourceIntTest {

    private static final byte[] DEFAULT_PATH = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PATH = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_PATH_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PATH_CONTENT_TYPE = "image/png";

    @Autowired
    private Document_articleRepository document_articleRepository;

    @Autowired
    private Document_articleSearchRepository document_articleSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDocument_articleMockMvc;

    private Document_article document_article;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Document_articleResource document_articleResource = new Document_articleResource(document_articleRepository, document_articleSearchRepository);
        this.restDocument_articleMockMvc = MockMvcBuilders.standaloneSetup(document_articleResource)
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
    public static Document_article createEntity(EntityManager em) {
        Document_article document_article = new Document_article()
            .path(DEFAULT_PATH)
            .pathContentType(DEFAULT_PATH_CONTENT_TYPE);
        return document_article;
    }

    @Before
    public void initTest() {
        document_articleSearchRepository.deleteAll();
        document_article = createEntity(em);
    }

    @Test
    @Transactional
    public void createDocument_article() throws Exception {
        int databaseSizeBeforeCreate = document_articleRepository.findAll().size();

        // Create the Document_article
        restDocument_articleMockMvc.perform(post("/api/document-articles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(document_article)))
            .andExpect(status().isCreated());

        // Validate the Document_article in the database
        List<Document_article> document_articleList = document_articleRepository.findAll();
        assertThat(document_articleList).hasSize(databaseSizeBeforeCreate + 1);
        Document_article testDocument_article = document_articleList.get(document_articleList.size() - 1);
        assertThat(testDocument_article.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testDocument_article.getPathContentType()).isEqualTo(DEFAULT_PATH_CONTENT_TYPE);

        // Validate the Document_article in Elasticsearch
        Document_article document_articleEs = document_articleSearchRepository.findOne(testDocument_article.getId());
        assertThat(document_articleEs).isEqualToComparingFieldByField(testDocument_article);
    }

    @Test
    @Transactional
    public void createDocument_articleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = document_articleRepository.findAll().size();

        // Create the Document_article with an existing ID
        document_article.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocument_articleMockMvc.perform(post("/api/document-articles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(document_article)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Document_article> document_articleList = document_articleRepository.findAll();
        assertThat(document_articleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDocument_articles() throws Exception {
        // Initialize the database
        document_articleRepository.saveAndFlush(document_article);

        // Get all the document_articleList
        restDocument_articleMockMvc.perform(get("/api/document-articles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(document_article.getId().intValue())))
            .andExpect(jsonPath("$.[*].pathContentType").value(hasItem(DEFAULT_PATH_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].path").value(hasItem(Base64Utils.encodeToString(DEFAULT_PATH))));
    }

    @Test
    @Transactional
    public void getDocument_article() throws Exception {
        // Initialize the database
        document_articleRepository.saveAndFlush(document_article);

        // Get the document_article
        restDocument_articleMockMvc.perform(get("/api/document-articles/{id}", document_article.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(document_article.getId().intValue()))
            .andExpect(jsonPath("$.pathContentType").value(DEFAULT_PATH_CONTENT_TYPE))
            .andExpect(jsonPath("$.path").value(Base64Utils.encodeToString(DEFAULT_PATH)));
    }

    @Test
    @Transactional
    public void getNonExistingDocument_article() throws Exception {
        // Get the document_article
        restDocument_articleMockMvc.perform(get("/api/document-articles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDocument_article() throws Exception {
        // Initialize the database
        document_articleRepository.saveAndFlush(document_article);
        document_articleSearchRepository.save(document_article);
        int databaseSizeBeforeUpdate = document_articleRepository.findAll().size();

        // Update the document_article
        Document_article updatedDocument_article = document_articleRepository.findOne(document_article.getId());
        updatedDocument_article
            .path(UPDATED_PATH)
            .pathContentType(UPDATED_PATH_CONTENT_TYPE);

        restDocument_articleMockMvc.perform(put("/api/document-articles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDocument_article)))
            .andExpect(status().isOk());

        // Validate the Document_article in the database
        List<Document_article> document_articleList = document_articleRepository.findAll();
        assertThat(document_articleList).hasSize(databaseSizeBeforeUpdate);
        Document_article testDocument_article = document_articleList.get(document_articleList.size() - 1);
        assertThat(testDocument_article.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testDocument_article.getPathContentType()).isEqualTo(UPDATED_PATH_CONTENT_TYPE);

        // Validate the Document_article in Elasticsearch
        Document_article document_articleEs = document_articleSearchRepository.findOne(testDocument_article.getId());
        assertThat(document_articleEs).isEqualToComparingFieldByField(testDocument_article);
    }

    @Test
    @Transactional
    public void updateNonExistingDocument_article() throws Exception {
        int databaseSizeBeforeUpdate = document_articleRepository.findAll().size();

        // Create the Document_article

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDocument_articleMockMvc.perform(put("/api/document-articles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(document_article)))
            .andExpect(status().isCreated());

        // Validate the Document_article in the database
        List<Document_article> document_articleList = document_articleRepository.findAll();
        assertThat(document_articleList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDocument_article() throws Exception {
        // Initialize the database
        document_articleRepository.saveAndFlush(document_article);
        document_articleSearchRepository.save(document_article);
        int databaseSizeBeforeDelete = document_articleRepository.findAll().size();

        // Get the document_article
        restDocument_articleMockMvc.perform(delete("/api/document-articles/{id}", document_article.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean document_articleExistsInEs = document_articleSearchRepository.exists(document_article.getId());
        assertThat(document_articleExistsInEs).isFalse();

        // Validate the database is empty
        List<Document_article> document_articleList = document_articleRepository.findAll();
        assertThat(document_articleList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDocument_article() throws Exception {
        // Initialize the database
        document_articleRepository.saveAndFlush(document_article);
        document_articleSearchRepository.save(document_article);

        // Search the document_article
        restDocument_articleMockMvc.perform(get("/api/_search/document-articles?query=id:" + document_article.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(document_article.getId().intValue())))
            .andExpect(jsonPath("$.[*].pathContentType").value(hasItem(DEFAULT_PATH_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].path").value(hasItem(Base64Utils.encodeToString(DEFAULT_PATH))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Document_article.class);
    }
}

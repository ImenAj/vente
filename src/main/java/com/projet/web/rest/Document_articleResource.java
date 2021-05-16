package com.projet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.projet.domain.Document_article;

import com.projet.repository.Document_articleRepository;
import com.projet.repository.search.Document_articleSearchRepository;
import com.projet.web.rest.util.HeaderUtil;
import com.projet.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Document_article.
 */
@RestController
@RequestMapping("/api")
public class Document_articleResource {

    private final Logger log = LoggerFactory.getLogger(Document_articleResource.class);

    private static final String ENTITY_NAME = "document_article";
        
    private final Document_articleRepository document_articleRepository;

    private final Document_articleSearchRepository document_articleSearchRepository;

    public Document_articleResource(Document_articleRepository document_articleRepository, Document_articleSearchRepository document_articleSearchRepository) {
        this.document_articleRepository = document_articleRepository;
        this.document_articleSearchRepository = document_articleSearchRepository;
    }

    /**
     * POST  /document-articles : Create a new document_article.
     *
     * @param document_article the document_article to create
     * @return the ResponseEntity with status 201 (Created) and with body the new document_article, or with status 400 (Bad Request) if the document_article has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/document-articles")
    @Timed
    public ResponseEntity<Document_article> createDocument_article(@RequestBody Document_article document_article) throws URISyntaxException {
        log.debug("REST request to save Document_article : {}", document_article);
        if (document_article.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new document_article cannot already have an ID")).body(null);
        }
        Document_article result = document_articleRepository.save(document_article);
        document_articleSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/document-articles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /document-articles : Updates an existing document_article.
     *
     * @param document_article the document_article to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated document_article,
     * or with status 400 (Bad Request) if the document_article is not valid,
     * or with status 500 (Internal Server Error) if the document_article couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/document-articles")
    @Timed
    public ResponseEntity<Document_article> updateDocument_article(@RequestBody Document_article document_article) throws URISyntaxException {
        log.debug("REST request to update Document_article : {}", document_article);
        if (document_article.getId() == null) {
            return createDocument_article(document_article);
        }
        Document_article result = document_articleRepository.save(document_article);
        document_articleSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, document_article.getId().toString()))
            .body(result);
    }

    /**
     * GET  /document-articles : get all the document_articles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of document_articles in body
     */
    @GetMapping("/document-articles")
    @Timed
    public ResponseEntity<List<Document_article>> getAllDocument_articles(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Document_articles");
        Page<Document_article> page = document_articleRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/document-articles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /document-articles/:id : get the "id" document_article.
     *
     * @param id the id of the document_article to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the document_article, or with status 404 (Not Found)
     */
    @GetMapping("/document-articles/{id}")
    @Timed
    public ResponseEntity<Document_article> getDocument_article(@PathVariable Long id) {
        log.debug("REST request to get Document_article : {}", id);
        Document_article document_article = document_articleRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(document_article));
    }

    /**
     * DELETE  /document-articles/:id : delete the "id" document_article.
     *
     * @param id the id of the document_article to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/document-articles/{id}")
    @Timed
    public ResponseEntity<Void> deleteDocument_article(@PathVariable Long id) {
        log.debug("REST request to delete Document_article : {}", id);
        document_articleRepository.delete(id);
        document_articleSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/document-articles?query=:query : search for the document_article corresponding
     * to the query.
     *
     * @param query the query of the document_article search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/document-articles")
    @Timed
    public ResponseEntity<List<Document_article>> searchDocument_articles(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Document_articles for query {}", query);
        Page<Document_article> page = document_articleSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/document-articles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}

package com.projet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.projet.domain.SousFamilleDepense;

import com.projet.repository.SousFamilleDepenseRepository;
import com.projet.repository.search.SousFamilleDepenseSearchRepository;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing SousFamilleDepense.
 */
@RestController
@RequestMapping("/api")
public class SousFamilleDepenseResource {

    private final Logger log = LoggerFactory.getLogger(SousFamilleDepenseResource.class);

    private static final String ENTITY_NAME = "sousFamilleDepense";
        
    private final SousFamilleDepenseRepository sousFamilleDepenseRepository;

    private final SousFamilleDepenseSearchRepository sousFamilleDepenseSearchRepository;

    public SousFamilleDepenseResource(SousFamilleDepenseRepository sousFamilleDepenseRepository, SousFamilleDepenseSearchRepository sousFamilleDepenseSearchRepository) {
        this.sousFamilleDepenseRepository = sousFamilleDepenseRepository;
        this.sousFamilleDepenseSearchRepository = sousFamilleDepenseSearchRepository;
    }

    /**
     * POST  /sous-famille-depenses : Create a new sousFamilleDepense.
     *
     * @param sousFamilleDepense the sousFamilleDepense to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sousFamilleDepense, or with status 400 (Bad Request) if the sousFamilleDepense has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sous-famille-depenses")
    @Timed
    public ResponseEntity<SousFamilleDepense> createSousFamilleDepense(@Valid @RequestBody SousFamilleDepense sousFamilleDepense) throws URISyntaxException {
        log.debug("REST request to save SousFamilleDepense : {}", sousFamilleDepense);
        if (sousFamilleDepense.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new sousFamilleDepense cannot already have an ID")).body(null);
        }
        SousFamilleDepense result = sousFamilleDepenseRepository.save(sousFamilleDepense);
        sousFamilleDepenseSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/sous-famille-depenses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sous-famille-depenses : Updates an existing sousFamilleDepense.
     *
     * @param sousFamilleDepense the sousFamilleDepense to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sousFamilleDepense,
     * or with status 400 (Bad Request) if the sousFamilleDepense is not valid,
     * or with status 500 (Internal Server Error) if the sousFamilleDepense couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sous-famille-depenses")
    @Timed
    public ResponseEntity<SousFamilleDepense> updateSousFamilleDepense(@Valid @RequestBody SousFamilleDepense sousFamilleDepense) throws URISyntaxException {
        log.debug("REST request to update SousFamilleDepense : {}", sousFamilleDepense);
        if (sousFamilleDepense.getId() == null) {
            return createSousFamilleDepense(sousFamilleDepense);
        }
        SousFamilleDepense result = sousFamilleDepenseRepository.save(sousFamilleDepense);
        sousFamilleDepenseSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sousFamilleDepense.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sous-famille-depenses : get all the sousFamilleDepenses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of sousFamilleDepenses in body
     */
    @GetMapping("/sous-famille-depenses")
    @Timed
    public ResponseEntity<List<SousFamilleDepense>> getAllSousFamilleDepenses(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of SousFamilleDepenses");
        Page<SousFamilleDepense> page = sousFamilleDepenseRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sous-famille-depenses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sous-famille-depenses/:id : get the "id" sousFamilleDepense.
     *
     * @param id the id of the sousFamilleDepense to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sousFamilleDepense, or with status 404 (Not Found)
     */
    @GetMapping("/sous-famille-depenses/{id}")
    @Timed
    public ResponseEntity<SousFamilleDepense> getSousFamilleDepense(@PathVariable Long id) {
        log.debug("REST request to get SousFamilleDepense : {}", id);
        SousFamilleDepense sousFamilleDepense = sousFamilleDepenseRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sousFamilleDepense));
    }

    /**
     * DELETE  /sous-famille-depenses/:id : delete the "id" sousFamilleDepense.
     *
     * @param id the id of the sousFamilleDepense to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sous-famille-depenses/{id}")
    @Timed
    public ResponseEntity<Void> deleteSousFamilleDepense(@PathVariable Long id) {
        log.debug("REST request to delete SousFamilleDepense : {}", id);
        sousFamilleDepenseRepository.delete(id);
        sousFamilleDepenseSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/sous-famille-depenses?query=:query : search for the sousFamilleDepense corresponding
     * to the query.
     *
     * @param query the query of the sousFamilleDepense search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/sous-famille-depenses")
    @Timed
    public ResponseEntity<List<SousFamilleDepense>> searchSousFamilleDepenses(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of SousFamilleDepenses for query {}", query);
        Page<SousFamilleDepense> page = sousFamilleDepenseSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/sous-famille-depenses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}

package com.projet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.projet.domain.FamilleDepense;

import com.projet.repository.FamilleDepenseRepository;
import com.projet.repository.search.FamilleDepenseSearchRepository;
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
 * REST controller for managing FamilleDepense.
 */
@RestController
@RequestMapping("/api")
public class FamilleDepenseResource {

    private final Logger log = LoggerFactory.getLogger(FamilleDepenseResource.class);

    private static final String ENTITY_NAME = "familleDepense";
        
    private final FamilleDepenseRepository familleDepenseRepository;

    private final FamilleDepenseSearchRepository familleDepenseSearchRepository;

    public FamilleDepenseResource(FamilleDepenseRepository familleDepenseRepository, FamilleDepenseSearchRepository familleDepenseSearchRepository) {
        this.familleDepenseRepository = familleDepenseRepository;
        this.familleDepenseSearchRepository = familleDepenseSearchRepository;
    }

    /**
     * POST  /famille-depenses : Create a new familleDepense.
     *
     * @param familleDepense the familleDepense to create
     * @return the ResponseEntity with status 201 (Created) and with body the new familleDepense, or with status 400 (Bad Request) if the familleDepense has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/famille-depenses")
    @Timed
    public ResponseEntity<FamilleDepense> createFamilleDepense(@Valid @RequestBody FamilleDepense familleDepense) throws URISyntaxException {
        log.debug("REST request to save FamilleDepense : {}", familleDepense);
        if (familleDepense.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new familleDepense cannot already have an ID")).body(null);
        }
        FamilleDepense result = familleDepenseRepository.save(familleDepense);
        familleDepenseSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/famille-depenses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /famille-depenses : Updates an existing familleDepense.
     *
     * @param familleDepense the familleDepense to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated familleDepense,
     * or with status 400 (Bad Request) if the familleDepense is not valid,
     * or with status 500 (Internal Server Error) if the familleDepense couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/famille-depenses")
    @Timed
    public ResponseEntity<FamilleDepense> updateFamilleDepense(@Valid @RequestBody FamilleDepense familleDepense) throws URISyntaxException {
        log.debug("REST request to update FamilleDepense : {}", familleDepense);
        if (familleDepense.getId() == null) {
            return createFamilleDepense(familleDepense);
        }
        FamilleDepense result = familleDepenseRepository.save(familleDepense);
        familleDepenseSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, familleDepense.getId().toString()))
            .body(result);
    }

    /**
     * GET  /famille-depenses : get all the familleDepenses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of familleDepenses in body
     */
    @GetMapping("/famille-depenses")
    @Timed
    public ResponseEntity<List<FamilleDepense>> getAllFamilleDepenses(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of FamilleDepenses");
        Page<FamilleDepense> page = familleDepenseRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/famille-depenses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /famille-depenses/:id : get the "id" familleDepense.
     *
     * @param id the id of the familleDepense to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the familleDepense, or with status 404 (Not Found)
     */
    @GetMapping("/famille-depenses/{id}")
    @Timed
    public ResponseEntity<FamilleDepense> getFamilleDepense(@PathVariable Long id) {
        log.debug("REST request to get FamilleDepense : {}", id);
        FamilleDepense familleDepense = familleDepenseRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(familleDepense));
    }

    /**
     * DELETE  /famille-depenses/:id : delete the "id" familleDepense.
     *
     * @param id the id of the familleDepense to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/famille-depenses/{id}")
    @Timed
    public ResponseEntity<Void> deleteFamilleDepense(@PathVariable Long id) {
        log.debug("REST request to delete FamilleDepense : {}", id);
        familleDepenseRepository.delete(id);
        familleDepenseSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/famille-depenses?query=:query : search for the familleDepense corresponding
     * to the query.
     *
     * @param query the query of the familleDepense search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/famille-depenses")
    @Timed
    public ResponseEntity<List<FamilleDepense>> searchFamilleDepenses(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of FamilleDepenses for query {}", query);
        Page<FamilleDepense> page = familleDepenseSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/famille-depenses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}

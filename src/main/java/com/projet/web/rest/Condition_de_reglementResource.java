package com.projet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.projet.domain.Condition_de_reglement;

import com.projet.repository.Condition_de_reglementRepository;
import com.projet.repository.search.Condition_de_reglementSearchRepository;
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
 * REST controller for managing Condition_de_reglement.
 */
@RestController
@RequestMapping("/api")
public class Condition_de_reglementResource {

    private final Logger log = LoggerFactory.getLogger(Condition_de_reglementResource.class);

    private static final String ENTITY_NAME = "condition_de_reglement";
        
    private final Condition_de_reglementRepository condition_de_reglementRepository;

    private final Condition_de_reglementSearchRepository condition_de_reglementSearchRepository;

    public Condition_de_reglementResource(Condition_de_reglementRepository condition_de_reglementRepository, Condition_de_reglementSearchRepository condition_de_reglementSearchRepository) {
        this.condition_de_reglementRepository = condition_de_reglementRepository;
        this.condition_de_reglementSearchRepository = condition_de_reglementSearchRepository;
    }

    /**
     * POST  /condition-de-reglements : Create a new condition_de_reglement.
     *
     * @param condition_de_reglement the condition_de_reglement to create
     * @return the ResponseEntity with status 201 (Created) and with body the new condition_de_reglement, or with status 400 (Bad Request) if the condition_de_reglement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/condition-de-reglements")
    @Timed
    public ResponseEntity<Condition_de_reglement> createCondition_de_reglement(@Valid @RequestBody Condition_de_reglement condition_de_reglement) throws URISyntaxException {
        log.debug("REST request to save Condition_de_reglement : {}", condition_de_reglement);
        if (condition_de_reglement.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new condition_de_reglement cannot already have an ID")).body(null);
        }
        Condition_de_reglement result = condition_de_reglementRepository.save(condition_de_reglement);
        condition_de_reglementSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/condition-de-reglements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /condition-de-reglements : Updates an existing condition_de_reglement.
     *
     * @param condition_de_reglement the condition_de_reglement to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated condition_de_reglement,
     * or with status 400 (Bad Request) if the condition_de_reglement is not valid,
     * or with status 500 (Internal Server Error) if the condition_de_reglement couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/condition-de-reglements")
    @Timed
    public ResponseEntity<Condition_de_reglement> updateCondition_de_reglement(@Valid @RequestBody Condition_de_reglement condition_de_reglement) throws URISyntaxException {
        log.debug("REST request to update Condition_de_reglement : {}", condition_de_reglement);
        if (condition_de_reglement.getId() == null) {
            return createCondition_de_reglement(condition_de_reglement);
        }
        Condition_de_reglement result = condition_de_reglementRepository.save(condition_de_reglement);
        condition_de_reglementSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, condition_de_reglement.getId().toString()))
            .body(result);
    }

    /**
     * GET  /condition-de-reglements : get all the condition_de_reglements.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of condition_de_reglements in body
     */
    @GetMapping("/condition-de-reglements")
    @Timed
    public ResponseEntity<List<Condition_de_reglement>> getAllCondition_de_reglements(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Condition_de_reglements");
        Page<Condition_de_reglement> page = condition_de_reglementRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/condition-de-reglements");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /condition-de-reglements/:id : get the "id" condition_de_reglement.
     *
     * @param id the id of the condition_de_reglement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the condition_de_reglement, or with status 404 (Not Found)
     */
    @GetMapping("/condition-de-reglements/{id}")
    @Timed
    public ResponseEntity<Condition_de_reglement> getCondition_de_reglement(@PathVariable Long id) {
        log.debug("REST request to get Condition_de_reglement : {}", id);
        Condition_de_reglement condition_de_reglement = condition_de_reglementRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(condition_de_reglement));
    }

    /**
     * DELETE  /condition-de-reglements/:id : delete the "id" condition_de_reglement.
     *
     * @param id the id of the condition_de_reglement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/condition-de-reglements/{id}")
    @Timed
    public ResponseEntity<Void> deleteCondition_de_reglement(@PathVariable Long id) {
        log.debug("REST request to delete Condition_de_reglement : {}", id);
        condition_de_reglementRepository.delete(id);
        condition_de_reglementSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/condition-de-reglements?query=:query : search for the condition_de_reglement corresponding
     * to the query.
     *
     * @param query the query of the condition_de_reglement search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/condition-de-reglements")
    @Timed
    public ResponseEntity<List<Condition_de_reglement>> searchCondition_de_reglements(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Condition_de_reglements for query {}", query);
        Page<Condition_de_reglement> page = condition_de_reglementSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/condition-de-reglements");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}

package com.projet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.projet.domain.Reglement;

import com.projet.repository.ReglementRepository;
import com.projet.repository.search.ReglementSearchRepository;
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
 * REST controller for managing Reglement.
 */
@RestController
@RequestMapping("/api")
public class ReglementResource {

    private final Logger log = LoggerFactory.getLogger(ReglementResource.class);

    private static final String ENTITY_NAME = "reglement";
        
    private final ReglementRepository reglementRepository;

    private final ReglementSearchRepository reglementSearchRepository;

    public ReglementResource(ReglementRepository reglementRepository, ReglementSearchRepository reglementSearchRepository) {
        this.reglementRepository = reglementRepository;
        this.reglementSearchRepository = reglementSearchRepository;
    }

    /**
     * POST  /reglements : Create a new reglement.
     *
     * @param reglement the reglement to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reglement, or with status 400 (Bad Request) if the reglement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/reglements")
    @Timed
    public ResponseEntity<Reglement> createReglement(@Valid @RequestBody Reglement reglement) throws URISyntaxException {
        log.debug("REST request to save Reglement : {}", reglement);
        if (reglement.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new reglement cannot already have an ID")).body(null);
        }
        Reglement result = reglementRepository.save(reglement);
        reglementSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/reglements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /reglements : Updates an existing reglement.
     *
     * @param reglement the reglement to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reglement,
     * or with status 400 (Bad Request) if the reglement is not valid,
     * or with status 500 (Internal Server Error) if the reglement couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/reglements")
    @Timed
    public ResponseEntity<Reglement> updateReglement(@Valid @RequestBody Reglement reglement) throws URISyntaxException {
        log.debug("REST request to update Reglement : {}", reglement);
        if (reglement.getId() == null) {
            return createReglement(reglement);
        }
        Reglement result = reglementRepository.save(reglement);
        reglementSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, reglement.getId().toString()))
            .body(result);
    }

    /**
     * GET  /reglements : get all the reglements.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of reglements in body
     */
    @GetMapping("/reglements")
    @Timed
    public ResponseEntity<List<Reglement>> getAllReglements(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Reglements");
        Page<Reglement> page = reglementRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/reglements");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /reglements/:id : get the "id" reglement.
     *
     * @param id the id of the reglement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reglement, or with status 404 (Not Found)
     */
    @GetMapping("/reglements/{id}")
    @Timed
    public ResponseEntity<Reglement> getReglement(@PathVariable Long id) {
        log.debug("REST request to get Reglement : {}", id);
        Reglement reglement = reglementRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(reglement));
    }

    /**
     * DELETE  /reglements/:id : delete the "id" reglement.
     *
     * @param id the id of the reglement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/reglements/{id}")
    @Timed
    public ResponseEntity<Void> deleteReglement(@PathVariable Long id) {
        log.debug("REST request to delete Reglement : {}", id);
        reglementRepository.delete(id);
        reglementSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/reglements?query=:query : search for the reglement corresponding
     * to the query.
     *
     * @param query the query of the reglement search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/reglements")
    @Timed
    public ResponseEntity<List<Reglement>> searchReglements(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Reglements for query {}", query);
        Page<Reglement> page = reglementSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/reglements");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}

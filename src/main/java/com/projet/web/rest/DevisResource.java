package com.projet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.projet.domain.Devis;

import com.projet.repository.DevisRepository;
import com.projet.repository.search.DevisSearchRepository;
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
 * REST controller for managing Devis.
 */
@RestController
@RequestMapping("/api")
public class DevisResource {

    private final Logger log = LoggerFactory.getLogger(DevisResource.class);

    private static final String ENTITY_NAME = "devis";
        
    private final DevisRepository devisRepository;

    private final DevisSearchRepository devisSearchRepository;

    public DevisResource(DevisRepository devisRepository, DevisSearchRepository devisSearchRepository) {
        this.devisRepository = devisRepository;
        this.devisSearchRepository = devisSearchRepository;
    }

    /**
     * POST  /devis : Create a new devis.
     *
     * @param devis the devis to create
     * @return the ResponseEntity with status 201 (Created) and with body the new devis, or with status 400 (Bad Request) if the devis has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/devis")
    @Timed
    public ResponseEntity<Devis> createDevis(@Valid @RequestBody Devis devis) throws URISyntaxException {
        log.debug("REST request to save Devis : {}", devis);
        if (devis.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new devis cannot already have an ID")).body(null);
        }
        Devis result = devisRepository.save(devis);
        devisSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/devis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /devis : Updates an existing devis.
     *
     * @param devis the devis to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated devis,
     * or with status 400 (Bad Request) if the devis is not valid,
     * or with status 500 (Internal Server Error) if the devis couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/devis")
    @Timed
    public ResponseEntity<Devis> updateDevis(@Valid @RequestBody Devis devis) throws URISyntaxException {
        log.debug("REST request to update Devis : {}", devis);
        if (devis.getId() == null) {
            return createDevis(devis);
        }
        Devis result = devisRepository.save(devis);
        devisSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, devis.getId().toString()))
            .body(result);
    }

    /**
     * GET  /devis : get all the devis.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of devis in body
     */
    @GetMapping("/devis")
    @Timed
    public ResponseEntity<List<Devis>> getAllDevis(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Devis");
        Page<Devis> page = devisRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/devis");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /devis/:id : get the "id" devis.
     *
     * @param id the id of the devis to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the devis, or with status 404 (Not Found)
     */
    @GetMapping("/devis/{id}")
    @Timed
    public ResponseEntity<Devis> getDevis(@PathVariable Long id) {
        log.debug("REST request to get Devis : {}", id);
        Devis devis = devisRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(devis));
    }

    /**
     * DELETE  /devis/:id : delete the "id" devis.
     *
     * @param id the id of the devis to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/devis/{id}")
    @Timed
    public ResponseEntity<Void> deleteDevis(@PathVariable Long id) {
        log.debug("REST request to delete Devis : {}", id);
        devisRepository.delete(id);
        devisSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/devis?query=:query : search for the devis corresponding
     * to the query.
     *
     * @param query the query of the devis search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/devis")
    @Timed
    public ResponseEntity<List<Devis>> searchDevis(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Devis for query {}", query);
        Page<Devis> page = devisSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/devis");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}

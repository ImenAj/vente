package com.projet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.projet.domain.Sous_famille;

import com.projet.repository.Sous_familleRepository;
import com.projet.repository.search.Sous_familleSearchRepository;
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
 * REST controller for managing Sous_famille.
 */
@RestController
@RequestMapping("/api")
public class Sous_familleResource {

    private final Logger log = LoggerFactory.getLogger(Sous_familleResource.class);

    private static final String ENTITY_NAME = "sous_famille";
        
    private final Sous_familleRepository sous_familleRepository;

    private final Sous_familleSearchRepository sous_familleSearchRepository;

    public Sous_familleResource(Sous_familleRepository sous_familleRepository, Sous_familleSearchRepository sous_familleSearchRepository) {
        this.sous_familleRepository = sous_familleRepository;
        this.sous_familleSearchRepository = sous_familleSearchRepository;
    }

    /**
     * POST  /sous-familles : Create a new sous_famille.
     *
     * @param sous_famille the sous_famille to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sous_famille, or with status 400 (Bad Request) if the sous_famille has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sous-familles")
    @Timed
    public ResponseEntity<Sous_famille> createSous_famille(@Valid @RequestBody Sous_famille sous_famille) throws URISyntaxException {
        log.debug("REST request to save Sous_famille : {}", sous_famille);
        if (sous_famille.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new sous_famille cannot already have an ID")).body(null);
        }
        Sous_famille result = sous_familleRepository.save(sous_famille);
        sous_familleSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/sous-familles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sous-familles : Updates an existing sous_famille.
     *
     * @param sous_famille the sous_famille to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sous_famille,
     * or with status 400 (Bad Request) if the sous_famille is not valid,
     * or with status 500 (Internal Server Error) if the sous_famille couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sous-familles")
    @Timed
    public ResponseEntity<Sous_famille> updateSous_famille(@Valid @RequestBody Sous_famille sous_famille) throws URISyntaxException {
        log.debug("REST request to update Sous_famille : {}", sous_famille);
        if (sous_famille.getId() == null) {
            return createSous_famille(sous_famille);
        }
        Sous_famille result = sous_familleRepository.save(sous_famille);
        sous_familleSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sous_famille.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sous-familles : get all the sous_familles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of sous_familles in body
     */
    @GetMapping("/sous-familles")
    @Timed
    public ResponseEntity<List<Sous_famille>> getAllSous_familles(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Sous_familles");
        Page<Sous_famille> page = sous_familleRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sous-familles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sous-familles/:id : get the "id" sous_famille.
     *
     * @param id the id of the sous_famille to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sous_famille, or with status 404 (Not Found)
     */
    @GetMapping("/sous-familles/{id}")
    @Timed
    public ResponseEntity<Sous_famille> getSous_famille(@PathVariable Long id) {
        log.debug("REST request to get Sous_famille : {}", id);
        Sous_famille sous_famille = sous_familleRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sous_famille));
    }

    /**
     * DELETE  /sous-familles/:id : delete the "id" sous_famille.
     *
     * @param id the id of the sous_famille to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sous-familles/{id}")
    @Timed
    public ResponseEntity<Void> deleteSous_famille(@PathVariable Long id) {
        log.debug("REST request to delete Sous_famille : {}", id);
        sous_familleRepository.delete(id);
        sous_familleSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/sous-familles?query=:query : search for the sous_famille corresponding
     * to the query.
     *
     * @param query the query of the sous_famille search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/sous-familles")
    @Timed
    public ResponseEntity<List<Sous_famille>> searchSous_familles(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Sous_familles for query {}", query);
        Page<Sous_famille> page = sous_familleSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/sous-familles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}

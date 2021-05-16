package com.projet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.projet.domain.Facture_achat;

import com.projet.repository.Facture_achatRepository;
import com.projet.repository.search.Facture_achatSearchRepository;
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
 * REST controller for managing Facture_achat.
 */
@RestController
@RequestMapping("/api")
public class Facture_achatResource {

    private final Logger log = LoggerFactory.getLogger(Facture_achatResource.class);

    private static final String ENTITY_NAME = "facture_achat";
        
    private final Facture_achatRepository facture_achatRepository;

    private final Facture_achatSearchRepository facture_achatSearchRepository;

    public Facture_achatResource(Facture_achatRepository facture_achatRepository, Facture_achatSearchRepository facture_achatSearchRepository) {
        this.facture_achatRepository = facture_achatRepository;
        this.facture_achatSearchRepository = facture_achatSearchRepository;
    }

    /**
     * POST  /facture-achats : Create a new facture_achat.
     *
     * @param facture_achat the facture_achat to create
     * @return the ResponseEntity with status 201 (Created) and with body the new facture_achat, or with status 400 (Bad Request) if the facture_achat has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/facture-achats")
    @Timed
    public ResponseEntity<Facture_achat> createFacture_achat(@Valid @RequestBody Facture_achat facture_achat) throws URISyntaxException {
        log.debug("REST request to save Facture_achat : {}", facture_achat);
        if (facture_achat.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new facture_achat cannot already have an ID")).body(null);
        }
        Facture_achat result = facture_achatRepository.save(facture_achat);
        facture_achatSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/facture-achats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /facture-achats : Updates an existing facture_achat.
     *
     * @param facture_achat the facture_achat to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated facture_achat,
     * or with status 400 (Bad Request) if the facture_achat is not valid,
     * or with status 500 (Internal Server Error) if the facture_achat couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/facture-achats")
    @Timed
    public ResponseEntity<Facture_achat> updateFacture_achat(@Valid @RequestBody Facture_achat facture_achat) throws URISyntaxException {
        log.debug("REST request to update Facture_achat : {}", facture_achat);
        if (facture_achat.getId() == null) {
            return createFacture_achat(facture_achat);
        }
        Facture_achat result = facture_achatRepository.save(facture_achat);
        facture_achatSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, facture_achat.getId().toString()))
            .body(result);
    }

    /**
     * GET  /facture-achats : get all the facture_achats.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of facture_achats in body
     */
    @GetMapping("/facture-achats")
    @Timed
    public ResponseEntity<List<Facture_achat>> getAllFacture_achats(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Facture_achats");
        Page<Facture_achat> page = facture_achatRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/facture-achats");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /facture-achats/:id : get the "id" facture_achat.
     *
     * @param id the id of the facture_achat to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the facture_achat, or with status 404 (Not Found)
     */
    @GetMapping("/facture-achats/{id}")
    @Timed
    public ResponseEntity<Facture_achat> getFacture_achat(@PathVariable Long id) {
        log.debug("REST request to get Facture_achat : {}", id);
        Facture_achat facture_achat = facture_achatRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(facture_achat));
    }

    /**
     * DELETE  /facture-achats/:id : delete the "id" facture_achat.
     *
     * @param id the id of the facture_achat to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/facture-achats/{id}")
    @Timed
    public ResponseEntity<Void> deleteFacture_achat(@PathVariable Long id) {
        log.debug("REST request to delete Facture_achat : {}", id);
        facture_achatRepository.delete(id);
        facture_achatSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/facture-achats?query=:query : search for the facture_achat corresponding
     * to the query.
     *
     * @param query the query of the facture_achat search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/facture-achats")
    @Timed
    public ResponseEntity<List<Facture_achat>> searchFacture_achats(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Facture_achats for query {}", query);
        Page<Facture_achat> page = facture_achatSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/facture-achats");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}

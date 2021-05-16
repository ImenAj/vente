package com.projet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.projet.domain.Bons_de_Reception;

import com.projet.repository.Bons_de_ReceptionRepository;
import com.projet.repository.search.Bons_de_ReceptionSearchRepository;
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
 * REST controller for managing Bons_de_Reception.
 */
@RestController
@RequestMapping("/api")
public class Bons_de_ReceptionResource {

    private final Logger log = LoggerFactory.getLogger(Bons_de_ReceptionResource.class);

    private static final String ENTITY_NAME = "bons_de_Reception";
        
    private final Bons_de_ReceptionRepository bons_de_ReceptionRepository;

    private final Bons_de_ReceptionSearchRepository bons_de_ReceptionSearchRepository;

    public Bons_de_ReceptionResource(Bons_de_ReceptionRepository bons_de_ReceptionRepository, Bons_de_ReceptionSearchRepository bons_de_ReceptionSearchRepository) {
        this.bons_de_ReceptionRepository = bons_de_ReceptionRepository;
        this.bons_de_ReceptionSearchRepository = bons_de_ReceptionSearchRepository;
    }

    /**
     * POST  /bons-de-receptions : Create a new bons_de_Reception.
     *
     * @param bons_de_Reception the bons_de_Reception to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bons_de_Reception, or with status 400 (Bad Request) if the bons_de_Reception has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bons-de-receptions")
    @Timed
    public ResponseEntity<Bons_de_Reception> createBons_de_Reception(@Valid @RequestBody Bons_de_Reception bons_de_Reception) throws URISyntaxException {
        log.debug("REST request to save Bons_de_Reception : {}", bons_de_Reception);
        if (bons_de_Reception.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new bons_de_Reception cannot already have an ID")).body(null);
        }
        Bons_de_Reception result = bons_de_ReceptionRepository.save(bons_de_Reception);
        bons_de_ReceptionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/bons-de-receptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bons-de-receptions : Updates an existing bons_de_Reception.
     *
     * @param bons_de_Reception the bons_de_Reception to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bons_de_Reception,
     * or with status 400 (Bad Request) if the bons_de_Reception is not valid,
     * or with status 500 (Internal Server Error) if the bons_de_Reception couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bons-de-receptions")
    @Timed
    public ResponseEntity<Bons_de_Reception> updateBons_de_Reception(@Valid @RequestBody Bons_de_Reception bons_de_Reception) throws URISyntaxException {
        log.debug("REST request to update Bons_de_Reception : {}", bons_de_Reception);
        if (bons_de_Reception.getId() == null) {
            return createBons_de_Reception(bons_de_Reception);
        }
        Bons_de_Reception result = bons_de_ReceptionRepository.save(bons_de_Reception);
        bons_de_ReceptionSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bons_de_Reception.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bons-de-receptions : get all the bons_de_Receptions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bons_de_Receptions in body
     */
    @GetMapping("/bons-de-receptions")
    @Timed
    public ResponseEntity<List<Bons_de_Reception>> getAllBons_de_Receptions(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Bons_de_Receptions");
        Page<Bons_de_Reception> page = bons_de_ReceptionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bons-de-receptions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bons-de-receptions/:id : get the "id" bons_de_Reception.
     *
     * @param id the id of the bons_de_Reception to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bons_de_Reception, or with status 404 (Not Found)
     */
    @GetMapping("/bons-de-receptions/{id}")
    @Timed
    public ResponseEntity<Bons_de_Reception> getBons_de_Reception(@PathVariable Long id) {
        log.debug("REST request to get Bons_de_Reception : {}", id);
        Bons_de_Reception bons_de_Reception = bons_de_ReceptionRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(bons_de_Reception));
    }

    /**
     * DELETE  /bons-de-receptions/:id : delete the "id" bons_de_Reception.
     *
     * @param id the id of the bons_de_Reception to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bons-de-receptions/{id}")
    @Timed
    public ResponseEntity<Void> deleteBons_de_Reception(@PathVariable Long id) {
        log.debug("REST request to delete Bons_de_Reception : {}", id);
        bons_de_ReceptionRepository.delete(id);
        bons_de_ReceptionSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/bons-de-receptions?query=:query : search for the bons_de_Reception corresponding
     * to the query.
     *
     * @param query the query of the bons_de_Reception search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/bons-de-receptions")
    @Timed
    public ResponseEntity<List<Bons_de_Reception>> searchBons_de_Receptions(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Bons_de_Receptions for query {}", query);
        Page<Bons_de_Reception> page = bons_de_ReceptionSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/bons-de-receptions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}

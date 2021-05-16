package com.projet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.projet.domain.Bons_de_commande;

import com.projet.repository.Bons_de_commandeRepository;
import com.projet.repository.search.Bons_de_commandeSearchRepository;
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
 * REST controller for managing Bons_de_commande.
 */
@RestController
@RequestMapping("/api")
public class Bons_de_commandeResource {

    private final Logger log = LoggerFactory.getLogger(Bons_de_commandeResource.class);

    private static final String ENTITY_NAME = "bons_de_commande";
        
    private final Bons_de_commandeRepository bons_de_commandeRepository;

    private final Bons_de_commandeSearchRepository bons_de_commandeSearchRepository;

    public Bons_de_commandeResource(Bons_de_commandeRepository bons_de_commandeRepository, Bons_de_commandeSearchRepository bons_de_commandeSearchRepository) {
        this.bons_de_commandeRepository = bons_de_commandeRepository;
        this.bons_de_commandeSearchRepository = bons_de_commandeSearchRepository;
    }

    /**
     * POST  /bons-de-commandes : Create a new bons_de_commande.
     *
     * @param bons_de_commande the bons_de_commande to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bons_de_commande, or with status 400 (Bad Request) if the bons_de_commande has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bons-de-commandes")
    @Timed
    public ResponseEntity<Bons_de_commande> createBons_de_commande(@Valid @RequestBody Bons_de_commande bons_de_commande) throws URISyntaxException {
        log.debug("REST request to save Bons_de_commande : {}", bons_de_commande);
        if (bons_de_commande.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new bons_de_commande cannot already have an ID")).body(null);
        }
        Bons_de_commande result = bons_de_commandeRepository.save(bons_de_commande);
        bons_de_commandeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/bons-de-commandes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bons-de-commandes : Updates an existing bons_de_commande.
     *
     * @param bons_de_commande the bons_de_commande to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bons_de_commande,
     * or with status 400 (Bad Request) if the bons_de_commande is not valid,
     * or with status 500 (Internal Server Error) if the bons_de_commande couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bons-de-commandes")
    @Timed
    public ResponseEntity<Bons_de_commande> updateBons_de_commande(@Valid @RequestBody Bons_de_commande bons_de_commande) throws URISyntaxException {
        log.debug("REST request to update Bons_de_commande : {}", bons_de_commande);
        if (bons_de_commande.getId() == null) {
            return createBons_de_commande(bons_de_commande);
        }
        Bons_de_commande result = bons_de_commandeRepository.save(bons_de_commande);
        bons_de_commandeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bons_de_commande.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bons-de-commandes : get all the bons_de_commandes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bons_de_commandes in body
     */
    @GetMapping("/bons-de-commandes")
    @Timed
    public ResponseEntity<List<Bons_de_commande>> getAllBons_de_commandes(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Bons_de_commandes");
        Page<Bons_de_commande> page = bons_de_commandeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bons-de-commandes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bons-de-commandes/:id : get the "id" bons_de_commande.
     *
     * @param id the id of the bons_de_commande to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bons_de_commande, or with status 404 (Not Found)
     */
    @GetMapping("/bons-de-commandes/{id}")
    @Timed
    public ResponseEntity<Bons_de_commande> getBons_de_commande(@PathVariable Long id) {
        log.debug("REST request to get Bons_de_commande : {}", id);
        Bons_de_commande bons_de_commande = bons_de_commandeRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(bons_de_commande));
    }

    /**
     * DELETE  /bons-de-commandes/:id : delete the "id" bons_de_commande.
     *
     * @param id the id of the bons_de_commande to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bons-de-commandes/{id}")
    @Timed
    public ResponseEntity<Void> deleteBons_de_commande(@PathVariable Long id) {
        log.debug("REST request to delete Bons_de_commande : {}", id);
        bons_de_commandeRepository.delete(id);
        bons_de_commandeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/bons-de-commandes?query=:query : search for the bons_de_commande corresponding
     * to the query.
     *
     * @param query the query of the bons_de_commande search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/bons-de-commandes")
    @Timed
    public ResponseEntity<List<Bons_de_commande>> searchBons_de_commandes(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Bons_de_commandes for query {}", query);
        Page<Bons_de_commande> page = bons_de_commandeSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/bons-de-commandes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}

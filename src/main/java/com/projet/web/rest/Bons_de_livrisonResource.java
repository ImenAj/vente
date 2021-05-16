package com.projet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.projet.domain.Bons_de_livrison;

import com.projet.repository.Bons_de_livrisonRepository;
import com.projet.repository.search.Bons_de_livrisonSearchRepository;
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
 * REST controller for managing Bons_de_livrison.
 */
@RestController
@RequestMapping("/api")
public class Bons_de_livrisonResource {

    private final Logger log = LoggerFactory.getLogger(Bons_de_livrisonResource.class);

    private static final String ENTITY_NAME = "bons_de_livrison";
        
    private final Bons_de_livrisonRepository bons_de_livrisonRepository;

    private final Bons_de_livrisonSearchRepository bons_de_livrisonSearchRepository;

    public Bons_de_livrisonResource(Bons_de_livrisonRepository bons_de_livrisonRepository, Bons_de_livrisonSearchRepository bons_de_livrisonSearchRepository) {
        this.bons_de_livrisonRepository = bons_de_livrisonRepository;
        this.bons_de_livrisonSearchRepository = bons_de_livrisonSearchRepository;
    }

    /**
     * POST  /bons-de-livrisons : Create a new bons_de_livrison.
     *
     * @param bons_de_livrison the bons_de_livrison to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bons_de_livrison, or with status 400 (Bad Request) if the bons_de_livrison has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bons-de-livrisons")
    @Timed
    public ResponseEntity<Bons_de_livrison> createBons_de_livrison(@Valid @RequestBody Bons_de_livrison bons_de_livrison) throws URISyntaxException {
        log.debug("REST request to save Bons_de_livrison : {}", bons_de_livrison);
        if (bons_de_livrison.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new bons_de_livrison cannot already have an ID")).body(null);
        }
        Bons_de_livrison result = bons_de_livrisonRepository.save(bons_de_livrison);
        bons_de_livrisonSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/bons-de-livrisons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bons-de-livrisons : Updates an existing bons_de_livrison.
     *
     * @param bons_de_livrison the bons_de_livrison to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bons_de_livrison,
     * or with status 400 (Bad Request) if the bons_de_livrison is not valid,
     * or with status 500 (Internal Server Error) if the bons_de_livrison couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bons-de-livrisons")
    @Timed
    public ResponseEntity<Bons_de_livrison> updateBons_de_livrison(@Valid @RequestBody Bons_de_livrison bons_de_livrison) throws URISyntaxException {
        log.debug("REST request to update Bons_de_livrison : {}", bons_de_livrison);
        if (bons_de_livrison.getId() == null) {
            return createBons_de_livrison(bons_de_livrison);
        }
        Bons_de_livrison result = bons_de_livrisonRepository.save(bons_de_livrison);
        bons_de_livrisonSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bons_de_livrison.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bons-de-livrisons : get all the bons_de_livrisons.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bons_de_livrisons in body
     */
    @GetMapping("/bons-de-livrisons")
    @Timed
    public ResponseEntity<List<Bons_de_livrison>> getAllBons_de_livrisons(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Bons_de_livrisons");
        Page<Bons_de_livrison> page = bons_de_livrisonRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bons-de-livrisons");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bons-de-livrisons/:id : get the "id" bons_de_livrison.
     *
     * @param id the id of the bons_de_livrison to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bons_de_livrison, or with status 404 (Not Found)
     */
    @GetMapping("/bons-de-livrisons/{id}")
    @Timed
    public ResponseEntity<Bons_de_livrison> getBons_de_livrison(@PathVariable Long id) {
        log.debug("REST request to get Bons_de_livrison : {}", id);
        Bons_de_livrison bons_de_livrison = bons_de_livrisonRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(bons_de_livrison));
    }

    /**
     * DELETE  /bons-de-livrisons/:id : delete the "id" bons_de_livrison.
     *
     * @param id the id of the bons_de_livrison to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bons-de-livrisons/{id}")
    @Timed
    public ResponseEntity<Void> deleteBons_de_livrison(@PathVariable Long id) {
        log.debug("REST request to delete Bons_de_livrison : {}", id);
        bons_de_livrisonRepository.delete(id);
        bons_de_livrisonSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/bons-de-livrisons?query=:query : search for the bons_de_livrison corresponding
     * to the query.
     *
     * @param query the query of the bons_de_livrison search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/bons-de-livrisons")
    @Timed
    public ResponseEntity<List<Bons_de_livrison>> searchBons_de_livrisons(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Bons_de_livrisons for query {}", query);
        Page<Bons_de_livrison> page = bons_de_livrisonSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/bons-de-livrisons");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}

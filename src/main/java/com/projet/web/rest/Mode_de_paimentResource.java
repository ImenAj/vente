package com.projet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.projet.domain.Mode_de_paiment;

import com.projet.repository.Mode_de_paimentRepository;
import com.projet.repository.search.Mode_de_paimentSearchRepository;
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
 * REST controller for managing Mode_de_paiment.
 */
@RestController
@RequestMapping("/api")
public class Mode_de_paimentResource {

    private final Logger log = LoggerFactory.getLogger(Mode_de_paimentResource.class);

    private static final String ENTITY_NAME = "mode_de_paiment";
        
    private final Mode_de_paimentRepository mode_de_paimentRepository;

    private final Mode_de_paimentSearchRepository mode_de_paimentSearchRepository;

    public Mode_de_paimentResource(Mode_de_paimentRepository mode_de_paimentRepository, Mode_de_paimentSearchRepository mode_de_paimentSearchRepository) {
        this.mode_de_paimentRepository = mode_de_paimentRepository;
        this.mode_de_paimentSearchRepository = mode_de_paimentSearchRepository;
    }

    /**
     * POST  /mode-de-paiments : Create a new mode_de_paiment.
     *
     * @param mode_de_paiment the mode_de_paiment to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mode_de_paiment, or with status 400 (Bad Request) if the mode_de_paiment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/mode-de-paiments")
    @Timed
    public ResponseEntity<Mode_de_paiment> createMode_de_paiment(@Valid @RequestBody Mode_de_paiment mode_de_paiment) throws URISyntaxException {
        log.debug("REST request to save Mode_de_paiment : {}", mode_de_paiment);
        if (mode_de_paiment.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new mode_de_paiment cannot already have an ID")).body(null);
        }
        Mode_de_paiment result = mode_de_paimentRepository.save(mode_de_paiment);
        mode_de_paimentSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/mode-de-paiments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mode-de-paiments : Updates an existing mode_de_paiment.
     *
     * @param mode_de_paiment the mode_de_paiment to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mode_de_paiment,
     * or with status 400 (Bad Request) if the mode_de_paiment is not valid,
     * or with status 500 (Internal Server Error) if the mode_de_paiment couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/mode-de-paiments")
    @Timed
    public ResponseEntity<Mode_de_paiment> updateMode_de_paiment(@Valid @RequestBody Mode_de_paiment mode_de_paiment) throws URISyntaxException {
        log.debug("REST request to update Mode_de_paiment : {}", mode_de_paiment);
        if (mode_de_paiment.getId() == null) {
            return createMode_de_paiment(mode_de_paiment);
        }
        Mode_de_paiment result = mode_de_paimentRepository.save(mode_de_paiment);
        mode_de_paimentSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mode_de_paiment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mode-de-paiments : get all the mode_de_paiments.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mode_de_paiments in body
     */
    @GetMapping("/mode-de-paiments")
    @Timed
    public ResponseEntity<List<Mode_de_paiment>> getAllMode_de_paiments(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Mode_de_paiments");
        Page<Mode_de_paiment> page = mode_de_paimentRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mode-de-paiments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /mode-de-paiments/:id : get the "id" mode_de_paiment.
     *
     * @param id the id of the mode_de_paiment to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mode_de_paiment, or with status 404 (Not Found)
     */
    @GetMapping("/mode-de-paiments/{id}")
    @Timed
    public ResponseEntity<Mode_de_paiment> getMode_de_paiment(@PathVariable Long id) {
        log.debug("REST request to get Mode_de_paiment : {}", id);
        Mode_de_paiment mode_de_paiment = mode_de_paimentRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(mode_de_paiment));
    }

    /**
     * DELETE  /mode-de-paiments/:id : delete the "id" mode_de_paiment.
     *
     * @param id the id of the mode_de_paiment to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/mode-de-paiments/{id}")
    @Timed
    public ResponseEntity<Void> deleteMode_de_paiment(@PathVariable Long id) {
        log.debug("REST request to delete Mode_de_paiment : {}", id);
        mode_de_paimentRepository.delete(id);
        mode_de_paimentSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/mode-de-paiments?query=:query : search for the mode_de_paiment corresponding
     * to the query.
     *
     * @param query the query of the mode_de_paiment search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/mode-de-paiments")
    @Timed
    public ResponseEntity<List<Mode_de_paiment>> searchMode_de_paiments(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Mode_de_paiments for query {}", query);
        Page<Mode_de_paiment> page = mode_de_paimentSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/mode-de-paiments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}

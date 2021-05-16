package com.projet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.projet.domain.Avoirs;

import com.projet.repository.AvoirsRepository;
import com.projet.repository.search.AvoirsSearchRepository;
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
 * REST controller for managing Avoirs.
 */
@RestController
@RequestMapping("/api")
public class AvoirsResource {

    private final Logger log = LoggerFactory.getLogger(AvoirsResource.class);

    private static final String ENTITY_NAME = "avoirs";
        
    private final AvoirsRepository avoirsRepository;

    private final AvoirsSearchRepository avoirsSearchRepository;

    public AvoirsResource(AvoirsRepository avoirsRepository, AvoirsSearchRepository avoirsSearchRepository) {
        this.avoirsRepository = avoirsRepository;
        this.avoirsSearchRepository = avoirsSearchRepository;
    }

    /**
     * POST  /avoirs : Create a new avoirs.
     *
     * @param avoirs the avoirs to create
     * @return the ResponseEntity with status 201 (Created) and with body the new avoirs, or with status 400 (Bad Request) if the avoirs has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/avoirs")
    @Timed
    public ResponseEntity<Avoirs> createAvoirs(@Valid @RequestBody Avoirs avoirs) throws URISyntaxException {
        log.debug("REST request to save Avoirs : {}", avoirs);
        if (avoirs.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new avoirs cannot already have an ID")).body(null);
        }
        Avoirs result = avoirsRepository.save(avoirs);
        avoirsSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/avoirs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /avoirs : Updates an existing avoirs.
     *
     * @param avoirs the avoirs to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated avoirs,
     * or with status 400 (Bad Request) if the avoirs is not valid,
     * or with status 500 (Internal Server Error) if the avoirs couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/avoirs")
    @Timed
    public ResponseEntity<Avoirs> updateAvoirs(@Valid @RequestBody Avoirs avoirs) throws URISyntaxException {
        log.debug("REST request to update Avoirs : {}", avoirs);
        if (avoirs.getId() == null) {
            return createAvoirs(avoirs);
        }
        Avoirs result = avoirsRepository.save(avoirs);
        avoirsSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, avoirs.getId().toString()))
            .body(result);
    }

    /**
     * GET  /avoirs : get all the avoirs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of avoirs in body
     */
    @GetMapping("/avoirs")
    @Timed
    public ResponseEntity<List<Avoirs>> getAllAvoirs(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Avoirs");
        Page<Avoirs> page = avoirsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/avoirs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /avoirs/:id : get the "id" avoirs.
     *
     * @param id the id of the avoirs to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the avoirs, or with status 404 (Not Found)
     */
    @GetMapping("/avoirs/{id}")
    @Timed
    public ResponseEntity<Avoirs> getAvoirs(@PathVariable Long id) {
        log.debug("REST request to get Avoirs : {}", id);
        Avoirs avoirs = avoirsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(avoirs));
    }

    /**
     * DELETE  /avoirs/:id : delete the "id" avoirs.
     *
     * @param id the id of the avoirs to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/avoirs/{id}")
    @Timed
    public ResponseEntity<Void> deleteAvoirs(@PathVariable Long id) {
        log.debug("REST request to delete Avoirs : {}", id);
        avoirsRepository.delete(id);
        avoirsSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/avoirs?query=:query : search for the avoirs corresponding
     * to the query.
     *
     * @param query the query of the avoirs search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/avoirs")
    @Timed
    public ResponseEntity<List<Avoirs>> searchAvoirs(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Avoirs for query {}", query);
        Page<Avoirs> page = avoirsSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/avoirs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}

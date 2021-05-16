package com.projet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.projet.domain.Bons_de_livraison;

import com.projet.repository.Bons_de_livraisonRepository;
import com.projet.repository.search.Bons_de_livraisonSearchRepository;
import com.projet.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Bons_de_livraison.
 */
@RestController
@RequestMapping("/api")
public class Bons_de_livraisonResource {

    private final Logger log = LoggerFactory.getLogger(Bons_de_livraisonResource.class);

    private static final String ENTITY_NAME = "bons_de_livraison";
        
    private final Bons_de_livraisonRepository bons_de_livraisonRepository;

    private final Bons_de_livraisonSearchRepository bons_de_livraisonSearchRepository;

    public Bons_de_livraisonResource(Bons_de_livraisonRepository bons_de_livraisonRepository, Bons_de_livraisonSearchRepository bons_de_livraisonSearchRepository) {
        this.bons_de_livraisonRepository = bons_de_livraisonRepository;
        this.bons_de_livraisonSearchRepository = bons_de_livraisonSearchRepository;
    }

    /**
     * POST  /bons-de-livraisons : Create a new bons_de_livraison.
     *
     * @param bons_de_livraison the bons_de_livraison to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bons_de_livraison, or with status 400 (Bad Request) if the bons_de_livraison has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bons-de-livraisons")
    @Timed
    public ResponseEntity<Bons_de_livraison> createBons_de_livraison(@Valid @RequestBody Bons_de_livraison bons_de_livraison) throws URISyntaxException {
        log.debug("REST request to save Bons_de_livraison : {}", bons_de_livraison);
        if (bons_de_livraison.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new bons_de_livraison cannot already have an ID")).body(null);
        }
        Bons_de_livraison result = bons_de_livraisonRepository.save(bons_de_livraison);
        bons_de_livraisonSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/bons-de-livraisons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bons-de-livraisons : Updates an existing bons_de_livraison.
     *
     * @param bons_de_livraison the bons_de_livraison to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bons_de_livraison,
     * or with status 400 (Bad Request) if the bons_de_livraison is not valid,
     * or with status 500 (Internal Server Error) if the bons_de_livraison couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bons-de-livraisons")
    @Timed
    public ResponseEntity<Bons_de_livraison> updateBons_de_livraison(@Valid @RequestBody Bons_de_livraison bons_de_livraison) throws URISyntaxException {
        log.debug("REST request to update Bons_de_livraison : {}", bons_de_livraison);
        if (bons_de_livraison.getId() == null) {
            return createBons_de_livraison(bons_de_livraison);
        }
        Bons_de_livraison result = bons_de_livraisonRepository.save(bons_de_livraison);
        bons_de_livraisonSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bons_de_livraison.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bons-de-livraisons : get all the bons_de_livraisons.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of bons_de_livraisons in body
     */
    @GetMapping("/bons-de-livraisons")
    @Timed
    public List<Bons_de_livraison> getAllBons_de_livraisons() {
        log.debug("REST request to get all Bons_de_livraisons");
        List<Bons_de_livraison> bons_de_livraisons = bons_de_livraisonRepository.findAll();
        return bons_de_livraisons;
    }

    /**
     * GET  /bons-de-livraisons/:id : get the "id" bons_de_livraison.
     *
     * @param id the id of the bons_de_livraison to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bons_de_livraison, or with status 404 (Not Found)
     */
    @GetMapping("/bons-de-livraisons/{id}")
    @Timed
    public ResponseEntity<Bons_de_livraison> getBons_de_livraison(@PathVariable Long id) {
        log.debug("REST request to get Bons_de_livraison : {}", id);
        Bons_de_livraison bons_de_livraison = bons_de_livraisonRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(bons_de_livraison));
    }

    /**
     * DELETE  /bons-de-livraisons/:id : delete the "id" bons_de_livraison.
     *
     * @param id the id of the bons_de_livraison to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bons-de-livraisons/{id}")
    @Timed
    public ResponseEntity<Void> deleteBons_de_livraison(@PathVariable Long id) {
        log.debug("REST request to delete Bons_de_livraison : {}", id);
        bons_de_livraisonRepository.delete(id);
        bons_de_livraisonSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/bons-de-livraisons?query=:query : search for the bons_de_livraison corresponding
     * to the query.
     *
     * @param query the query of the bons_de_livraison search 
     * @return the result of the search
     */
    @GetMapping("/_search/bons-de-livraisons")
    @Timed
    public List<Bons_de_livraison> searchBons_de_livraisons(@RequestParam String query) {
        log.debug("REST request to search Bons_de_livraisons for query {}", query);
        return StreamSupport
            .stream(bons_de_livraisonSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}

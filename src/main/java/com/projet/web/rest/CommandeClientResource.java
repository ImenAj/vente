package com.projet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.projet.domain.CommandeClient;

import com.projet.repository.CommandeClientRepository;
import com.projet.repository.search.CommandeClientSearchRepository;
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
 * REST controller for managing CommandeClient.
 */
@RestController
@RequestMapping("/api")
public class CommandeClientResource {

    private final Logger log = LoggerFactory.getLogger(CommandeClientResource.class);

    private static final String ENTITY_NAME = "commandeClient";
        
    private final CommandeClientRepository commandeClientRepository;

    private final CommandeClientSearchRepository commandeClientSearchRepository;

    public CommandeClientResource(CommandeClientRepository commandeClientRepository, CommandeClientSearchRepository commandeClientSearchRepository) {
        this.commandeClientRepository = commandeClientRepository;
        this.commandeClientSearchRepository = commandeClientSearchRepository;
    }

    /**
     * POST  /commande-clients : Create a new commandeClient.
     *
     * @param commandeClient the commandeClient to create
     * @return the ResponseEntity with status 201 (Created) and with body the new commandeClient, or with status 400 (Bad Request) if the commandeClient has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/commande-clients")
    @Timed
    public ResponseEntity<CommandeClient> createCommandeClient(@Valid @RequestBody CommandeClient commandeClient) throws URISyntaxException {
        log.debug("REST request to save CommandeClient : {}", commandeClient);
        if (commandeClient.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new commandeClient cannot already have an ID")).body(null);
        }
        CommandeClient result = commandeClientRepository.save(commandeClient);
        commandeClientSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/commande-clients/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /commande-clients : Updates an existing commandeClient.
     *
     * @param commandeClient the commandeClient to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated commandeClient,
     * or with status 400 (Bad Request) if the commandeClient is not valid,
     * or with status 500 (Internal Server Error) if the commandeClient couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/commande-clients")
    @Timed
    public ResponseEntity<CommandeClient> updateCommandeClient(@Valid @RequestBody CommandeClient commandeClient) throws URISyntaxException {
        log.debug("REST request to update CommandeClient : {}", commandeClient);
        if (commandeClient.getId() == null) {
            return createCommandeClient(commandeClient);
        }
        CommandeClient result = commandeClientRepository.save(commandeClient);
        commandeClientSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, commandeClient.getId().toString()))
            .body(result);
    }

    /**
     * GET  /commande-clients : get all the commandeClients.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of commandeClients in body
     */
    @GetMapping("/commande-clients")
    @Timed
    public ResponseEntity<List<CommandeClient>> getAllCommandeClients(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of CommandeClients");
        Page<CommandeClient> page = commandeClientRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/commande-clients");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /commande-clients/:id : get the "id" commandeClient.
     *
     * @param id the id of the commandeClient to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the commandeClient, or with status 404 (Not Found)
     */
    @GetMapping("/commande-clients/{id}")
    @Timed
    public ResponseEntity<CommandeClient> getCommandeClient(@PathVariable Long id) {
        log.debug("REST request to get CommandeClient : {}", id);
        CommandeClient commandeClient = commandeClientRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(commandeClient));
    }

    /**
     * DELETE  /commande-clients/:id : delete the "id" commandeClient.
     *
     * @param id the id of the commandeClient to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/commande-clients/{id}")
    @Timed
    public ResponseEntity<Void> deleteCommandeClient(@PathVariable Long id) {
        log.debug("REST request to delete CommandeClient : {}", id);
        commandeClientRepository.delete(id);
        commandeClientSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/commande-clients?query=:query : search for the commandeClient corresponding
     * to the query.
     *
     * @param query the query of the commandeClient search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/commande-clients")
    @Timed
    public ResponseEntity<List<CommandeClient>> searchCommandeClients(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of CommandeClients for query {}", query);
        Page<CommandeClient> page = commandeClientSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/commande-clients");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}

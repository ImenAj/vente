package com.projet.repository.search;

import com.projet.domain.Bons_de_commande;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Bons_de_commande entity.
 */
public interface Bons_de_commandeSearchRepository extends ElasticsearchRepository<Bons_de_commande, Long> {
}

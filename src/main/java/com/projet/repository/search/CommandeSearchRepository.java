package com.projet.repository.search;

import com.projet.domain.Commande;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Commande entity.
 */
public interface CommandeSearchRepository extends ElasticsearchRepository<Commande, Long> {
}

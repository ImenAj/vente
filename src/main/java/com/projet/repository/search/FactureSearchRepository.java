package com.projet.repository.search;

import com.projet.domain.Facture;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Facture entity.
 */
public interface FactureSearchRepository extends ElasticsearchRepository<Facture, Long> {
}

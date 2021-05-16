package com.projet.repository.search;

import com.projet.domain.Reglement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Reglement entity.
 */
public interface ReglementSearchRepository extends ElasticsearchRepository<Reglement, Long> {
}

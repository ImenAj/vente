package com.projet.repository.search;

import com.projet.domain.Condition_de_reglement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Condition_de_reglement entity.
 */
public interface Condition_de_reglementSearchRepository extends ElasticsearchRepository<Condition_de_reglement, Long> {
}

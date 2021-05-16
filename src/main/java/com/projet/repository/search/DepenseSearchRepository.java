package com.projet.repository.search;

import com.projet.domain.Depense;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Depense entity.
 */
public interface DepenseSearchRepository extends ElasticsearchRepository<Depense, Long> {
}

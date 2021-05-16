package com.projet.repository.search;

import com.projet.domain.FamilleDepense;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the FamilleDepense entity.
 */
public interface FamilleDepenseSearchRepository extends ElasticsearchRepository<FamilleDepense, Long> {
}

package com.projet.repository.search;

import com.projet.domain.SousFamilleDepense;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SousFamilleDepense entity.
 */
public interface SousFamilleDepenseSearchRepository extends ElasticsearchRepository<SousFamilleDepense, Long> {
}

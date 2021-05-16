package com.projet.repository.search;

import com.projet.domain.Compte;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Compte entity.
 */
public interface CompteSearchRepository extends ElasticsearchRepository<Compte, Long> {
}

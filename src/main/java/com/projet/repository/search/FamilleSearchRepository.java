package com.projet.repository.search;

import com.projet.domain.Famille;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Famille entity.
 */
public interface FamilleSearchRepository extends ElasticsearchRepository<Famille, Long> {
}

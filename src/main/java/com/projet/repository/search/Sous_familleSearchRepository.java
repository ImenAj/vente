package com.projet.repository.search;

import com.projet.domain.Sous_famille;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Sous_famille entity.
 */
public interface Sous_familleSearchRepository extends ElasticsearchRepository<Sous_famille, Long> {
}

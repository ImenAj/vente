package com.projet.repository.search;

import com.projet.domain.Avoirs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Avoirs entity.
 */
public interface AvoirsSearchRepository extends ElasticsearchRepository<Avoirs, Long> {
}

package com.projet.repository.search;

import com.projet.domain.Mode_de_paiment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Mode_de_paiment entity.
 */
public interface Mode_de_paimentSearchRepository extends ElasticsearchRepository<Mode_de_paiment, Long> {
}

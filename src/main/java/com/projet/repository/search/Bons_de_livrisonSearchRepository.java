package com.projet.repository.search;

import com.projet.domain.Bons_de_livrison;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Bons_de_livrison entity.
 */
public interface Bons_de_livrisonSearchRepository extends ElasticsearchRepository<Bons_de_livrison, Long> {
}

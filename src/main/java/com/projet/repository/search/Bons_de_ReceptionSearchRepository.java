package com.projet.repository.search;

import com.projet.domain.Bons_de_Reception;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Bons_de_Reception entity.
 */
public interface Bons_de_ReceptionSearchRepository extends ElasticsearchRepository<Bons_de_Reception, Long> {
}

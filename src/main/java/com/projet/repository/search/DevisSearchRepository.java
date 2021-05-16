package com.projet.repository.search;

import com.projet.domain.Devis;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Devis entity.
 */
public interface DevisSearchRepository extends ElasticsearchRepository<Devis, Long> {
}

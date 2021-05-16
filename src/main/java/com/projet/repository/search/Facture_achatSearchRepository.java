package com.projet.repository.search;

import com.projet.domain.Facture_achat;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Facture_achat entity.
 */
public interface Facture_achatSearchRepository extends ElasticsearchRepository<Facture_achat, Long> {
}

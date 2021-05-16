package com.projet.repository.search;

import com.projet.domain.Fournisseur;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Fournisseur entity.
 */
public interface FournisseurSearchRepository extends ElasticsearchRepository<Fournisseur, Long> {
}

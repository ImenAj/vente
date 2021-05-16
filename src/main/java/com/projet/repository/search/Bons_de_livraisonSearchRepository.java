package com.projet.repository.search;

import com.projet.domain.Bons_de_livraison;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Bons_de_livraison entity.
 */
public interface Bons_de_livraisonSearchRepository extends ElasticsearchRepository<Bons_de_livraison, Long> {
}

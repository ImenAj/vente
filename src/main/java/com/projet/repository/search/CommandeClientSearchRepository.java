package com.projet.repository.search;

import com.projet.domain.CommandeClient;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CommandeClient entity.
 */
public interface CommandeClientSearchRepository extends ElasticsearchRepository<CommandeClient, Long> {
}

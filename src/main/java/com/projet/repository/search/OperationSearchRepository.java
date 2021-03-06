package com.projet.repository.search;

import com.projet.domain.Operation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Operation entity.
 */
public interface OperationSearchRepository extends ElasticsearchRepository<Operation, Long> {
}

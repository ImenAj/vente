package com.projet.repository.search;

import com.projet.domain.Document_article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Document_article entity.
 */
public interface Document_articleSearchRepository extends ElasticsearchRepository<Document_article, Long> {
}

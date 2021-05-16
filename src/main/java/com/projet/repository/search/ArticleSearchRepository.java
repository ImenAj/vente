package com.projet.repository.search;

import com.projet.domain.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Article entity.
 */
public interface ArticleSearchRepository extends ElasticsearchRepository<Article, Long> {
}

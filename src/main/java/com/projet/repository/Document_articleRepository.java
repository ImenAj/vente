package com.projet.repository;

import com.projet.domain.Document_article;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Document_article entity.
 */
@SuppressWarnings("unused")
public interface Document_articleRepository extends JpaRepository<Document_article,Long> {

}

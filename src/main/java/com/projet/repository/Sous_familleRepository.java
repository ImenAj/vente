package com.projet.repository;

import com.projet.domain.Sous_famille;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Sous_famille entity.
 */
@SuppressWarnings("unused")
public interface Sous_familleRepository extends JpaRepository<Sous_famille,Long> {

}

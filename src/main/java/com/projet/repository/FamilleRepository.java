package com.projet.repository;

import com.projet.domain.Famille;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Famille entity.
 */
@SuppressWarnings("unused")
public interface FamilleRepository extends JpaRepository<Famille,Long> {

}

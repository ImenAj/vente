package com.projet.repository;

import com.projet.domain.Condition_de_reglement;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Condition_de_reglement entity.
 */
@SuppressWarnings("unused")
public interface Condition_de_reglementRepository extends JpaRepository<Condition_de_reglement,Long> {

}

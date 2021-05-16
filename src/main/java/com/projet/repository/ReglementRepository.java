package com.projet.repository;

import com.projet.domain.Reglement;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Reglement entity.
 */
@SuppressWarnings("unused")
public interface ReglementRepository extends JpaRepository<Reglement,Long> {

}

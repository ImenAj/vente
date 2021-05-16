package com.projet.repository;

import com.projet.domain.Facture;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Facture entity.
 */
@SuppressWarnings("unused")
public interface FactureRepository extends JpaRepository<Facture,Long> {

}

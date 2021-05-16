package com.projet.repository;

import com.projet.domain.Fournisseur;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Fournisseur entity.
 */
@SuppressWarnings("unused")
public interface FournisseurRepository extends JpaRepository<Fournisseur,Long> {

}

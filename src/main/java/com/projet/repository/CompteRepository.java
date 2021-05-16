package com.projet.repository;

import com.projet.domain.Compte;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Compte entity.
 */
@SuppressWarnings("unused")
public interface CompteRepository extends JpaRepository<Compte,Long> {

}

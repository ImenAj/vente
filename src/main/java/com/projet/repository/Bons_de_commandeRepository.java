package com.projet.repository;

import com.projet.domain.Bons_de_commande;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Bons_de_commande entity.
 */
@SuppressWarnings("unused")
public interface Bons_de_commandeRepository extends JpaRepository<Bons_de_commande,Long> {

}

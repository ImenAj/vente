package com.projet.repository;

import com.projet.domain.Bons_de_livraison;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Bons_de_livraison entity.
 */
@SuppressWarnings("unused")
public interface Bons_de_livraisonRepository extends JpaRepository<Bons_de_livraison,Long> {

}

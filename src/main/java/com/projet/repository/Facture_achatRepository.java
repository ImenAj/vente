package com.projet.repository;

import com.projet.domain.Facture_achat;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Facture_achat entity.
 */
@SuppressWarnings("unused")
public interface Facture_achatRepository extends JpaRepository<Facture_achat,Long> {

}

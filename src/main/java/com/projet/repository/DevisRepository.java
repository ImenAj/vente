package com.projet.repository;

import com.projet.domain.Devis;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Devis entity.
 */
@SuppressWarnings("unused")
public interface DevisRepository extends JpaRepository<Devis,Long> {

}

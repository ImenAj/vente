package com.projet.repository;

import com.projet.domain.SousFamilleDepense;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SousFamilleDepense entity.
 */
@SuppressWarnings("unused")
public interface SousFamilleDepenseRepository extends JpaRepository<SousFamilleDepense,Long> {

}

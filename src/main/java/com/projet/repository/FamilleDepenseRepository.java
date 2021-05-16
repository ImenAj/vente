package com.projet.repository;

import com.projet.domain.FamilleDepense;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FamilleDepense entity.
 */
@SuppressWarnings("unused")
public interface FamilleDepenseRepository extends JpaRepository<FamilleDepense,Long> {

}

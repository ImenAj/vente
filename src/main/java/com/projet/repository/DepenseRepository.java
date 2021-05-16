package com.projet.repository;

import com.projet.domain.Depense;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Depense entity.
 */
@SuppressWarnings("unused")
public interface DepenseRepository extends JpaRepository<Depense,Long> {

}

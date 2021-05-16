package com.projet.repository;

import com.projet.domain.Bons_de_Reception;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Bons_de_Reception entity.
 */
@SuppressWarnings("unused")
public interface Bons_de_ReceptionRepository extends JpaRepository<Bons_de_Reception,Long> {

}

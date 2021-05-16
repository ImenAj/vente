package com.projet.repository;

import com.projet.domain.Bons_de_livrison;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Bons_de_livrison entity.
 */
@SuppressWarnings("unused")
public interface Bons_de_livrisonRepository extends JpaRepository<Bons_de_livrison,Long> {

}

package com.projet.repository;

import com.projet.domain.Avoirs;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Avoirs entity.
 */
@SuppressWarnings("unused")
public interface AvoirsRepository extends JpaRepository<Avoirs,Long> {

}

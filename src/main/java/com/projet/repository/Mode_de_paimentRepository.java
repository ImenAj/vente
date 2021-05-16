package com.projet.repository;

import com.projet.domain.Mode_de_paiment;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Mode_de_paiment entity.
 */
@SuppressWarnings("unused")
public interface Mode_de_paimentRepository extends JpaRepository<Mode_de_paiment,Long> {

}

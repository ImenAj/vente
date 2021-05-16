package com.projet.repository;

import com.projet.domain.CommandeClient;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CommandeClient entity.
 */
@SuppressWarnings("unused")
public interface CommandeClientRepository extends JpaRepository<CommandeClient,Long> {

}

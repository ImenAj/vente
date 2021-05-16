package com.projet.repository;

import com.projet.domain.Operation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Operation entity.
 */
@SuppressWarnings("unused")
public interface OperationRepository extends JpaRepository<Operation,Long> {

}

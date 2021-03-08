package com.ss.utopia.repositories;

import java.util.List;
import java.util.Optional;

import com.ss.utopia.models.Route;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends JpaRepository<Route, Integer> {

	@Query(value = "SELECT * FROM route", nativeQuery = true)
	List<Route> findAllRoutes();

	@Query(value = "SELECT * FROM route WHERE destination_id = ?1", nativeQuery = true)
	List<Route> findByDestination(String destination);

	@Query(value = "SELECT * FROM route WHERE origin_id = ?1", nativeQuery = true)
	List<Route> findByOrigin(String origin);

	@Query(value = "SELECT * FROM route WHERE destination_id = ?1 AND origin_id = ?2", nativeQuery = true)
	Optional<Route> findByDestinationAndOrigin(String destination, String origin);	
}
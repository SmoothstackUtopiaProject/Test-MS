package com.ss.utopia.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ss.utopia.models.Flight;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Integer> {

	@Query(value="SELECT * FROM flight WHERE airplane_id = ?1", nativeQuery=true)
	List<Flight> findFlightsByAirplaneId(Integer airplaneId);

}

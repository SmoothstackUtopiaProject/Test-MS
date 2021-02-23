package com.ss.utopia.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ss.utopia.models.Flight;

@Repository
public interface FlightRespository extends JpaRepository<Flight, Integer> {
	
	@Query(value="SELECT * from flight_status", nativeQuery=true)
	List<Flight> findAllFlights();
	
	@Query(value="SELECT * FROM flight_status WHERE id = ?1", nativeQuery=true)
	Optional<Flight> findById(Integer id);
	
	@Query(value="SELECT * from flight_status where route_id = ?1 and departure_date = ?2", nativeQuery=true)
	List<Flight> searchFlightByRouteIdAndDate(Integer routeId, LocalDate date);
	
	@Query(value="SELECT * from flight_status where route_id = ?1", nativeQuery=true)
	List<Flight> searchFlightByRouteId(Integer routeId);
	

}

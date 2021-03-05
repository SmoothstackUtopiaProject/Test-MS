package com.ss.utopia.repositories;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ss.utopia.models.Flight;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Integer> {
	
	@Query(value="SELECT * from flight where route_id = ?1 and departure_date = ?2", nativeQuery=true)
	List<Flight> searchFlightByRouteIdAndDate(Integer routeId, LocalDate date);
	
	@Query(value="SELECT * from flight where route_id = ?1", nativeQuery=true)
	List<Flight> searchFlightByRouteId(Integer routeId);
	
	@Query(value="SELECT flight.* FROM flight, route WHERE flight.route_id = route.id AND route.origin_id = ?1 AND route.destination_id = ?2", nativeQuery=true)
	List<Flight> searchFlightByOrigDest(String origin, String destination);
	
	@Query(value="SELECT flight.* FROM flight, route WHERE flight.route_id = route.id AND route.origin_id = ?1 AND route.destination_id = ?2 AND flight.departure_date >= ?3 AND flight.available_seats >= ?4", nativeQuery = true)
	List<Flight> searchFlightByOrigDestDateSeat(String origin, String destination, Date date, Integer travelers);
	
	@Query(value="SELECT * FROM flight WHERE airplane_id = ?1", nativeQuery=true)
	List<Flight> findFlightsByAirplaneId(Integer airplaneId);
}
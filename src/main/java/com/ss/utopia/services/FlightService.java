package com.ss.utopia.services;

import java.net.ConnectException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ss.utopia.exceptions.AirplaneAlreadyInUseException;
import com.ss.utopia.exceptions.FlightNotFoundException;
import com.ss.utopia.models.Flight;
import com.ss.utopia.repositories.FlightRepository;

@Service
public class FlightService {

	@Autowired
	FlightRepository flightRepository;
	
	@Autowired
	RouteService routeService;

	public List<Flight> findAll() {
		return flightRepository.findAll();		
	}
	
	public Flight findById(Integer id) throws FlightNotFoundException, ConnectException, IllegalArgumentException, SQLException {
		Optional<Flight> optionalFlight = flightRepository.findById(id);
		if(!optionalFlight.isPresent()) throw new FlightNotFoundException("No Flight with ID: " + id + " exist.");
		return optionalFlight.get();
	}
	
	public List<Flight> findBySearchAndFilter(HashMap<String, String> filterMap) {

		List<Flight> flights = findAll();
		// List<Flight> searchedFlights = applySearch(flights, filterMap);
		// if(searchedFlights.isEmpty()) return searchedFlights;
		return applyFilters(flights, filterMap);
	}

	public List<Flight> applySearch(List<Flight> flights, HashMap<String, String> filterMap) {
		List<Flight> flightsWithSearchTerms = new ArrayList<Flight>();
		
		String searchTerms = "searchTerms";
		if(filterMap.keySet().contains(searchTerms)) {
			String[] splitTerms = filterMap.get(searchTerms).split("c");
			ObjectMapper mapper = new ObjectMapper();
			
			for(Flight flight : flights) {
				boolean containsSearchTerms = true;
				
				try {
					String flightAsString = mapper.writeValueAsString(flight);
					for(String term : splitTerms) {
						if(!flightAsString.contains(term)) {
							containsSearchTerms = false;
							break;
						}
					}
				} catch(JsonProcessingException err){
					containsSearchTerms = false;
				}

				if(containsSearchTerms) {
					flightsWithSearchTerms.add(flight);
				}
			}
		}
		return flightsWithSearchTerms;
	}

	public List<Flight> applyFilters(List<Flight> flights, HashMap<String, String> filterMap) {
		List<Flight> filteredFlights = flights;

		// ID
		String flightId = "flightId";
		if(filterMap.keySet().contains(flightId)) {
			try {
				Integer parsedFlightId = Integer.parseInt(filterMap.get(flightId));
				filteredFlights = filteredFlights.stream()
				.filter(i -> i.getId().equals(parsedFlightId))
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}
		
		// Origin
		String origin = "origin";
		if(filterMap.keySet().contains(origin)) {
			try {
				List<Integer> routeIdList = routeService.findByOrigin(filterMap.get(origin))
						.stream().map(i -> i.getId())
						.collect(Collectors.toList());
				System.out.println(routeIdList);
				System.out.println("======================================================================");
				filteredFlights = filteredFlights.stream()
				.filter(i -> routeIdList.contains(i.getRouteId()))
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}
		
		// Destination
		String destination = "destination";
		if(filterMap.keySet().contains(destination)) {
			try {
				List<Integer> routeIdList = routeService.findByDestination(filterMap.get(destination))
						.stream().map(i -> i.getId())
						.collect(Collectors.toList());
				System.out.println(routeIdList);
				System.out.println("======================================================================");
				filteredFlights = filteredFlights.stream()
				.filter(i -> routeIdList.contains(i.getRouteId()))
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}

		// Origin Date
		String originDate = "originDate";
		if(filterMap.keySet().contains(originDate)) {
			try {
				Timestamp parsedOriginDate = Timestamp.valueOf(filterMap.get(originDate));
				filteredFlights = filteredFlights.stream()
				.filter(i -> i.getDateTime().equals(parsedOriginDate))
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}
		return filteredFlights;
	}

	public Flight insert(Integer routeId ,Integer airplaneId , String dateTime, Integer seatingId, Integer duration, String status) 
			throws ConnectException, IllegalArgumentException, SQLException, AirplaneAlreadyInUseException {
		// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		// List<Flight> flightsWithAirplaneId = flightRepository.findFlightsByAirplaneId(airplaneId)
		// 		.stream().filter(i -> 
		// 		Math.abs(Duration.between(
		// 				LocalDateTime.parse(dateTime,formatter), 
		// 				LocalDateTime.parse(i.getDateTime(),formatter)
		// 				).toHours()) < 2
		// 		)
		// 		.collect(Collectors.toList());
		// if(!flightsWithAirplaneId.isEmpty())
		// 	throw new AirplaneAlreadyInUseException("Airplane with id: " + airplaneId +" already has flights within two hours of what you are trying to create");
		return flightRepository.save(new Flight(routeId, airplaneId, dateTime, seatingId, duration, status));
	}
	
	public Flight update(Integer id, Integer routeId, Integer airplaneId, String dateTime, Integer seatingId,
			Integer duration, String status) 
			throws ConnectException, IllegalArgumentException, SQLException, AirplaneAlreadyInUseException, FlightNotFoundException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		Optional<Flight> optionalFlight = flightRepository.findById(id);
		if(!optionalFlight.isPresent()) throw new FlightNotFoundException("No flight with the id: " + id + " exists!");
		
		List<Flight> flightsWithAirplaneId = flightRepository.findFlightsByAirplaneId(airplaneId)
				.stream().filter(i -> 
				Math.abs(Duration.between(
						LocalDateTime.parse(dateTime, formatter), 
						LocalDateTime.parse(i.getDateTime(),formatter)
						).toHours()) < 2
				)
				.collect(Collectors.toList());
		
		if(!flightsWithAirplaneId.isEmpty())
			throw new AirplaneAlreadyInUseException("Airplane with id: " + airplaneId +" already has flights within two hours of what you are trying to create");
		return flightRepository.save(new Flight(id, routeId, airplaneId, dateTime, seatingId, duration, status));
		
	}	

	public void deleteById(Integer id) 
			throws FlightNotFoundException, ConnectException, IllegalArgumentException, SQLException {
		Optional<Flight> optionalFlight = flightRepository.findById(id);
		if(!optionalFlight.isPresent()) throw new FlightNotFoundException("No flight with the id: " + id + " exists!");
		flightRepository.deleteById(id);
	}
}
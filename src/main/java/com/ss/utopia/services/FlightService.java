package com.ss.utopia.services;

import java.net.ConnectException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ss.utopia.exceptions.AirplaneAlreadyInUseException;
import com.ss.utopia.exceptions.FlightNotFoundException;
import com.ss.utopia.models.Airplane;
import com.ss.utopia.models.AirplaneType;
import com.ss.utopia.models.Airport;
import com.ss.utopia.models.Flight;
import com.ss.utopia.models.FlightWithReferenceData;
import com.ss.utopia.models.Route;
import com.ss.utopia.repositories.FlightRepository;

@Service
public class FlightService {

	@Autowired
	AirplaneService airplaneService;

	@Autowired
	AirportService airportService;

	@Autowired
	RouteService routeService;

	@Autowired
	FlightRepository flightRepository;

	public List<Flight> findAll() {
		return flightRepository.findAll();		
	}

	public List<FlightWithReferenceData> findFlightReferenceData(List<Flight> flights) {
		List<Airplane> airplanes = airplaneService.findAll();
		List<AirplaneType> airplaneTypes = airplaneService.findAllAirplaneTypes();
		List<Airport> airports = airportService.findAll();
		List<Route> routes = routeService.findAll();

		List<FlightWithReferenceData> flightsWithReferenceData = new ArrayList<FlightWithReferenceData>();
		for(Flight flight : flights) {
			Integer flightId = flight.getFlightId();
			Integer flightRouteId = flight.getFlightRouteId();
			Integer flightAirplaneId = flight.getFlightAirplaneId();
			String flightDepartureTime = flight.getFlightDepartureTime();
			Integer flightSeatingId = flight.getFlightSeatingId();
			Integer flightDuration = flight.getFlightDuration();
			String flightStatus = flight.getFlightStatus();
			
			// Origin & Destination
			String flightRouteOriginIataId = "ERR";
			String flightRouteDestinationIataId = "ERR";
			String flightRouteOriginCityName = "ERROR";
			String flightRouteDestinationCityName = "ERROR";

			Route route = routes.stream()
			.filter(i -> i.getRouteId().equals(flightRouteId))
			.collect(Collectors.toList()).get(0);

			flightRouteOriginIataId = route.getRouteOriginIataId();
			flightRouteDestinationIataId = route.getRouteDestinationIataId();

			Airport origin = airports.stream()
			.filter(i -> i.getAirportIataId().equals(route.getRouteOriginIataId()))
			.collect(Collectors.toList()).get(0);

			Airport destination = airports.stream()
			.filter(i -> i.getAirportIataId().equals(route.getRouteDestinationIataId()))
			.collect(Collectors.toList()).get(0);

			flightRouteOriginCityName = origin.getAirportCityName();
			flightRouteDestinationCityName = destination.getAirportCityName();

			// Airplane Type Name
			String flightAirplaneTypeName = "ERROR";
			Airplane airplane = airplanes.stream()
			.filter(i -> i.getAirplaneId().equals(flightAirplaneId))
			.collect(Collectors.toList()).get(0);

			AirplaneType airplaneType = airplaneTypes.stream()
			.filter(i -> i.getAirplaneTypeId().equals(airplane.getAirplaneTypeId()))
			.collect(Collectors.toList()).get(0);
			
			flightAirplaneTypeName = airplaneType.getAirplaneTypeName();

			// New FlightWithReferenceData
			flightsWithReferenceData.add(new FlightWithReferenceData(
				flightId, 
				flightRouteId, 
				flightRouteOriginIataId, 
				flightRouteDestinationIataId, 
				flightRouteOriginCityName, 
				flightRouteDestinationCityName, 
				flightAirplaneId, 
				flightAirplaneTypeName, 
				flightDepartureTime, 
				flightSeatingId, 
				flightDuration, 
				flightStatus
			));
		}
		return flightsWithReferenceData;
	}
	
	public Flight findById(Integer id) throws FlightNotFoundException, ConnectException, IllegalArgumentException, SQLException {
		Optional<Flight> optionalFlight = flightRepository.findById(id);
		if(!optionalFlight.isPresent()) throw new FlightNotFoundException("No Flight with ID: " + id + " exist.");
		return optionalFlight.get();
	}
	
	public List<FlightWithReferenceData> findBySearchAndFilter(HashMap<String, String> filterMap) {

		List<Flight> flights = findAll();
		if(!filterMap.keySet().isEmpty()) flights = applyFilters(flights, filterMap);
		return findFlightReferenceData(flights);
	}

	public List<Flight> applyFilters(List<Flight> flights, HashMap<String, String> filterMap) {

		// ID
		String flightId = "flightId";
		if(filterMap.keySet().contains(flightId)) {
			try {
				Integer parsedFlightId = Integer.parseInt(filterMap.get(flightId));
				flights = flights.stream()
				.filter(i -> i.getFlightId().equals(parsedFlightId))
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}

		// Origin & Destination
		String origin = "flightRouteOriginIataId";
		String destination = "flightRouteDestinationIataId";
		if(filterMap.keySet().contains(origin) || filterMap.keySet().contains(destination)) {

			HashMap<String, String> routeIdFilterMap = new HashMap<String, String>();
			if(filterMap.keySet().contains(origin)) routeIdFilterMap.put("routeOriginIataId", filterMap.get(origin));
			if(filterMap.keySet().contains(destination)) routeIdFilterMap.put("routeDestinationIataId", filterMap.get(destination));
			
			List<Integer> routeIdList = routeService.findBySearchAndFilter(routeIdFilterMap).stream()
				.map(i -> i.getRouteId()).collect(Collectors.toList());

			flights = flights.stream()
				.filter(i -> routeIdList.contains(i.getFlightRouteId()))
				.collect(Collectors.toList());
		}

		// Origin Date
		String originDate = "originDate";
		if(filterMap.keySet().contains(originDate)) {
			try {
				Timestamp parsedOriginDate = Timestamp.valueOf(filterMap.get(originDate));
				flights = flights.stream()
				.filter(i -> i.getFlightDepartureTime().equals(parsedOriginDate))
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}

		// Search - (applied last due to save CPU usage)
		return applySearch(flights, filterMap);
	}

	public List<Flight> applySearch(List<Flight> flights, HashMap<String, String> filterMap) {
		List<Flight> flightsWithSearchTerms = new ArrayList<Flight>();
		
		String searchTerms = "searchTerms";
		if(filterMap.keySet().contains(searchTerms)) {
			String formattedSearch = filterMap.get(searchTerms)
			.toLowerCase()
			.replace(", ", ",");
			String[] splitTerms = formattedSearch.split(",");
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
		} else {
			return flights;
		}
		return flightsWithSearchTerms;
	}

	public Flight insert(Integer routeId ,Integer airplaneId , String dateTime, Integer seatingId, Integer duration, String status) 
			throws ConnectException, IllegalArgumentException, SQLException, AirplaneAlreadyInUseException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		List<Flight> flightsWithAirplaneId = flightRepository.findFlightsByAirplaneId(airplaneId)
				.stream().filter(i -> 
				Math.abs(Duration.between(
						LocalDateTime.parse(dateTime,formatter), 
						LocalDateTime.parse(i.getFlightDepartureTime(),formatter)
						).toHours()) < 2
				)
				.collect(Collectors.toList());
		if(!flightsWithAirplaneId.isEmpty())
			throw new AirplaneAlreadyInUseException("Airplane with id: " + airplaneId +" already has flights within two hours of what you are trying to create");
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
				(Math.abs(Duration.between(
						LocalDateTime.parse(dateTime, formatter), 
						LocalDateTime.parse(i.getFlightDepartureTime(),formatter)
						).toHours()) < 2
				) && !i.getFlightId().equals(id))
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
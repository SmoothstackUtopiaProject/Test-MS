package com.ss.utopia.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ss.utopia.exceptions.AirplaneAlreadyInUseException;
import com.ss.utopia.exceptions.FlightNotFoundException;
import com.ss.utopia.filters.FlightFilters;
import com.ss.utopia.models.Airplane;
import com.ss.utopia.models.AirplaneType;
import com.ss.utopia.models.Airport;
import com.ss.utopia.models.Flight;
import com.ss.utopia.models.FlightWithReferenceData;
import com.ss.utopia.models.Route;
import com.ss.utopia.repositories.FlightRepository;
import com.ss.utopia.timeformatting.FlightTimeFormatter;

@Service
public class FlightService {

	@Autowired
	private AirplaneService airplaneService;

	@Autowired
	private AirportService airportService;

	@Autowired
	private RouteService routeService;

	@Autowired
	private FlightRepository flightRepository;

	private static final Integer MINIMUM_AIRPLANE_NOFLIGHT_HOURS = 2;

	// Find All
	public List<Flight> findAll() {
		return flightRepository.findAll();		
	}
	
	// Find By ID
	public Flight findById(Integer id) throws FlightNotFoundException {
		Optional<Flight> optionalFlight = flightRepository.findById(id);
		if(!optionalFlight.isPresent()) {
			throw new FlightNotFoundException("No Flight with ID: " + id + " exist.");
		}
		return optionalFlight.get();
	}
	
	// Flights with Reference Data
	public List<FlightWithReferenceData> findFlightReferenceData(@Valid Iterable<Flight> flights) {
		List<Airplane> airplanes = airplaneService.findAll();
		List<AirplaneType> airplaneTypes = airplaneService.findAllAirplaneTypes();
		List<Airport> airports = airportService.findAll();
		List<Route> routes = routeService.findAll();

		List<FlightWithReferenceData> flightsWithReferenceData = new ArrayList<>();
		for(Flight flight : flights) {
			Integer flightId = flight.getFlightId();
			Integer flightRouteId = flight.getFlightRouteId();
			Integer flightAirplaneId = flight.getFlightAirplaneId();
			String flightDepartureTime = flight.getFlightDepartureTime();
			Integer flightSeatingId = flight.getFlightSeatingId();
			Integer flightDuration = flight.getFlightDuration();
			String flightStatus = flight.getFlightStatus();

			Route route = routes.stream()
			.filter(i -> i.getRouteId().equals(flightRouteId))
			.collect(Collectors.toList()).get(0);

			String flightRouteOriginIataId = route.getRouteOriginIataId();
			String flightRouteDestinationIataId = route.getRouteDestinationIataId();

			Airport origin = airports.stream()
			.filter(i -> i.getAirportIataId().equals(route.getRouteOriginIataId()))
			.collect(Collectors.toList()).get(0);

			Airport destination = airports.stream()
			.filter(i -> i.getAirportIataId().equals(route.getRouteDestinationIataId()))
			.collect(Collectors.toList()).get(0);

			String flightRouteOriginCityName = origin.getAirportCityName();
			String flightRouteDestinationCityName = destination.getAirportCityName();

			// Airplane Type Name
			Airplane airplane = airplanes.stream()
			.filter(i -> i.getAirplaneId().equals(flightAirplaneId))
			.collect(Collectors.toList()).get(0);

			AirplaneType airplaneType = airplaneTypes.stream()
			.filter(i -> i.getAirplaneTypeId().equals(airplane.getAirplaneTypeId()))
			.collect(Collectors.toList()).get(0);
			
			String flightAirplaneTypeName = airplaneType.getAirplaneTypeName();

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
	
	// Search & Filter
	public List<FlightWithReferenceData> findBySearchAndFilter(Map<String, String> filterMap) {
		List<Flight> flights = findAll();
		List<FlightWithReferenceData> flightsWithReferenceDatas = findFlightReferenceData(flights);
		if(!filterMap.keySet().isEmpty()) {
			flightsWithReferenceDatas = FlightFilters.apply(flightsWithReferenceDatas, filterMap);
		}
		return flightsWithReferenceDatas;
	}

	// Insert
	public Flight insert(Integer routeId ,Integer airplaneId , String dateTime, 
	Integer seatingId, Integer duration, String status) throws AirplaneAlreadyInUseException {
		
		List<Flight> flightsWithAirplaneId = flightRepository.findFlightsByAirplaneId(airplaneId)
			.stream().filter(i -> 
			Math.abs(Duration.between(
					LocalDateTime.parse(dateTime, FlightTimeFormatter.getInstance()), 
					LocalDateTime.parse(i.getFlightDepartureTime(), FlightTimeFormatter.getInstance())
				).toHours()) < MINIMUM_AIRPLANE_NOFLIGHT_HOURS
			)
			.collect(Collectors.toList());

		if(!flightsWithAirplaneId.isEmpty()) {
			throw new AirplaneAlreadyInUseException(
				"Airplane with id: " + airplaneId +" already has flights within two hours of what you are trying to create"
			);
		}
		return flightRepository.save(new Flight(routeId, airplaneId, dateTime, seatingId, duration, status));
	}
	
	// Update
	public Flight update(Integer id, Integer routeId, Integer airplaneId, String dateTime, Integer seatingId,
	Integer duration, String status) throws AirplaneAlreadyInUseException, FlightNotFoundException {

		Optional<Flight> optionalFlight = flightRepository.findById(id);
		if(!optionalFlight.isPresent()) {
			throw new FlightNotFoundException("No flight with the id: " + id + " exists!");
		}
		
		List<Flight> flightsWithAirplaneId = flightRepository.findFlightsByAirplaneId(airplaneId)
				.stream().filter(i -> 
				(Math.abs(Duration.between(
						LocalDateTime.parse(dateTime, FlightTimeFormatter.getInstance()), 
						LocalDateTime.parse(i.getFlightDepartureTime(), FlightTimeFormatter.getInstance())
					).toHours()) < MINIMUM_AIRPLANE_NOFLIGHT_HOURS
				) && !i.getFlightId().equals(id))
				.collect(Collectors.toList());
		
		if(!flightsWithAirplaneId.isEmpty()) {
			throw new AirplaneAlreadyInUseException(
				"Airplane with id: " + airplaneId +" already has flights within two hours of what you are trying to create"
			);
		}
		return flightRepository.save(new Flight(id, routeId, airplaneId, dateTime, seatingId, duration, status));
	}	

	// Delete by ID
	public void deleteById(Integer id) throws FlightNotFoundException {
		Optional<Flight> optionalFlight = flightRepository.findById(id);
		if(!optionalFlight.isPresent()) {
			throw new FlightNotFoundException("No flight with the id: " + id + " exists!");
		}
		flightRepository.deleteById(id);
	}
}
package com.ss.utopia.services;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ss.utopia.exceptions.AirportNotFoundException;
import com.ss.utopia.exceptions.RouteAlreadyExistsException;
import com.ss.utopia.exceptions.RouteNotFoundException;
import com.ss.utopia.filters.RouteFilters;
import com.ss.utopia.models.Airport;
import com.ss.utopia.models.Route;
import com.ss.utopia.repositories.RouteRepository;


@Service
public class RouteService {
	
	@Autowired
	private AirportService airportService;

	@Autowired
	private RouteRepository routeRepository;

	// Find All
	public List<Route> findAll() {
		return routeRepository.findAll();
	}
	
	// Find By ID
	public Route findById(Integer id) throws RouteNotFoundException {
		Optional<Route> optionalRoute = routeRepository.findById(id);
		if(!optionalRoute.isPresent()) {
			throw new RouteNotFoundException("No Route with ID: " + id + " exists.");
		}
		return optionalRoute.get();
	}

	// Search & Filter
	public List<Route> findBySearchAndFilter(Map<String, String> filterMap) {
		List<Route> routes = findAll();
		if(!filterMap.keySet().isEmpty()) {
			routes = RouteFilters.apply(routes, filterMap);
		}
		return routes;
	}

	// Insert
	public Route insert(String originIataId, String destinationIataId) 
	throws AirportNotFoundException, RouteAlreadyExistsException {

		// Verify the Origin & Destination exists
		Airport dest = airportService.findByIataId(destinationIataId);
		Airport orig = airportService.findByIataId(originIataId);

		// Verify origin & destination are not the same airport
		if(orig.equals(dest)) {
			throw new RouteAlreadyExistsException("Route Origin must be different from Route Destination.");
		}
			
		// Verify no route with this origin & destination already exists
		Map<String, String> filterMap = new HashMap<>();
		filterMap.put("routeOriginIataId", originIataId);
		filterMap.put("routeDestinationIataId", destinationIataId);
		List<Route> existingRouteList = findBySearchAndFilter(filterMap);
		if(!existingRouteList.isEmpty()) {
			throw new RouteAlreadyExistsException(
				"A Route already exist for origin: " + originIataId + 
				" to destination: " + destinationIataId + "."
			);
		} 

		// Create the new Route
		return routeRepository.save(new Route(originIataId, destinationIataId));
	}

	// Update
	public Route update(Integer routeId, String originIataId, String destinationIataId)
	throws AirportNotFoundException, RouteAlreadyExistsException, RouteNotFoundException {
		
		// NOLINT - (Intentionally placed here to always 1st-step verify the route exists)
		Route route = findById(routeId); 

		// Verify the Origin & Destination exists
		Airport dest = airportService.findByIataId(destinationIataId);
		Airport orig = airportService.findByIataId(originIataId);

		// Verify origin & destination are not the same airport
		if(orig.equals(dest)) {
			throw new RouteAlreadyExistsException("Route Origin must be different from Route Destination.");
		}

		// Verify no Route with this Origin & Destination already exists
		Map<String, String> filterMap = new HashMap<>();
		filterMap.put("routeOriginIataId", originIataId);
		filterMap.put("routeDestinationIataId", destinationIataId);
		List<Route> existingRouteList = findBySearchAndFilter(filterMap);
		if(!existingRouteList.isEmpty()) {
			throw new RouteAlreadyExistsException(
				"A Route already exist for origin: " + originIataId + " to destination: " + destinationIataId + "."
			);
		}

		// Update the Route
		route.setRouteId(routeId);
		route.setRouteOriginIataId(originIataId);
		route.setRouteDestinationIataId(destinationIataId);
		return routeRepository.save(route);
	}

	// Dete By ID
	public void deleteById(Integer id) throws RouteNotFoundException {
		findById(id);
		routeRepository.deleteById(id);
	}
}
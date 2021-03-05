package com.ss.utopia.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ss.utopia.exceptions.AirportNotFoundException;
import com.ss.utopia.exceptions.RouteAlreadyExistsException;
import com.ss.utopia.exceptions.RouteNotFoundException;
import com.ss.utopia.models.Airport;
import com.ss.utopia.models.Route;
import com.ss.utopia.repositories.RouteRepository;


@Service
public class RouteService {
	
	@Autowired
	private RouteRepository routeRepository;

	public List<Route> findAllRoutes() {
		return routeRepository.findAllRoutes();
	}
	
	public Route findById(Integer id) {
		Optional<Route> optionalRoute = routeRepository.findById(id);
		return optionalRoute.isPresent()
		? optionalRoute.get()
		: null;
	}
	
	public List<Route> findByDestination(String destination) {
		return routeRepository.findByDestination(destination);
	}

	public List<Route> findByOrigin(String destination) {
		return routeRepository.findByOrigin(destination);
	}
	
	public Route findByDestinationAndOrigin(String destination, String origin) {
		Optional<Route> optionalRoute = routeRepository.findByDestinationAndOrigin(destination, origin);
		return optionalRoute.isPresent()
			? optionalRoute.get()
			: null;
	}

	public Route insert(Route route) throws RouteAlreadyExistsException, AirportNotFoundException {
		Optional<Airport> optionalDest = routeRepository.findAirportByIataId(route.getDestination().getIataId());
		Optional<Airport> optionalOrig = routeRepository.findAirportByIataId(route.getOrigin().getIataId());
		
		if(!optionalDest.isPresent()) throw new AirportNotFoundException("No Airport with IATA Code: " + route.getDestination() + " exist.");
		if(!optionalOrig.isPresent()) throw new AirportNotFoundException("No Airport with IATA Code: " + route.getOrigin() + " exist.");
		
		Airport dest = optionalDest.get();
		Airport orig = optionalOrig.get();

		route.setDestination(dest);
		route.setOrigin(orig);
			
		Optional<Route> existingRoute = routeRepository.findByDestinationAndOrigin(dest.getIataId(), orig.getIataId());
		if(existingRoute.isPresent()) throw new RouteAlreadyExistsException("A Route already exist for origin: " + orig.getIataId() + " to destination: " + dest.getIataId() + ".");
		return routeRepository.save(route);
	}

	public void deleteById(Integer id) throws RouteNotFoundException {
		if(findById(id) == null) 
			throw new RouteNotFoundException("This route does not exist.");
		routeRepository.deleteById(id);
	}

	public Route update(Route route) throws RouteAlreadyExistsException, AirportNotFoundException {
		try {
			return insert(route);
		} catch (RouteAlreadyExistsException err) {
			throw new RouteAlreadyExistsException("This route already exists.");
		} catch (AirportNotFoundException err) {
			throw new AirportNotFoundException("Airport(s) does not exist");
		}
	}
}
package com.ss.utopia.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ss.utopia.exceptions.FlightNotFoundException;
import com.ss.utopia.models.Flight;
import com.ss.utopia.repositories.FlightRespository;

@Service
public class FlightService {

	@Autowired
	FlightRespository flightRespository;

	public List<Flight> getAll() {
		return flightRespository.findAllFlights();
	}
	
	public Flight findById(Integer id) throws FlightNotFoundException {
		Optional<Flight> optionalFlight = flightRespository.findById(id);
		if(!optionalFlight.isPresent()) throw new FlightNotFoundException("No Flight with ID: " + id + " exists.");
		return optionalFlight.get();
	}

	public Flight insert(Flight flight) {
		return flightRespository.save(flight);
	}

	public Flight update(Flight flight) {
		return flightRespository.save(flight);
	}

	public void delete(Integer id) {
		flightRespository.deleteById(id);
	}

	// search for flights, given route id and date
	public List<Flight> search(String routeId, String date) {
		System.out.println(date);
		Integer routeIdToInt = Integer.parseInt(routeId);
		LocalDate stingToDate = LocalDate.parse(date);
		return flightRespository.searchFlightByRouteIdAndDate(routeIdToInt, stingToDate);
	}
}

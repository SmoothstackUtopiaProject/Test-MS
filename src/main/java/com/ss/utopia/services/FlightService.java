package com.ss.utopia.services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ss.utopia.exceptions.FlightNotFoundException;
import com.ss.utopia.models.Flight;
import com.ss.utopia.repositories.FlightRespository;

@Service
public class FlightService {

	@Autowired
	FlightRespository flightRespository;

	public List<Flight> findAll() {
		return flightRespository.findAll();
	}
	
	public Flight findById(Integer id) throws FlightNotFoundException {
		Optional<Flight> optionalFlight = flightRespository.findById(id);
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

		// Origin Date
		String originDate = "originDate";
		if(filterMap.keySet().contains(originDate)) {
			try {
				Date parsedOriginDate = Date.valueOf(filterMap.get(originDate));
				filteredFlights = filteredFlights.stream()
				.filter(i -> i.getDate().equals(parsedOriginDate))
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}
		return filteredFlights;
	}

	public Flight insert(Flight flight) {
		return flightRespository.save(flight);
	}

	public Flight update(Flight flight) {
		return flightRespository.save(flight);
	}

	public void deleteById(Integer id) {
		flightRespository.deleteById(id);
	}	
}
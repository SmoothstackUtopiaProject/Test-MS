package com.ss.utopia.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ss.utopia.models.Flight;
import com.ss.utopia.repositories.FlightRespository;

// some random comment
// this is a comment

@Service
public class FlightService {

	@Autowired
	FlightRespository flightRespository;

	public List<Flight> findAll() {
		return flightRespository.findAll();
	}
	
	public Optional<Flight> findById(Integer id) {
		return flightRespository.findById(id);
	}

	public List<Flight> findBySearchAndFilter(HashMap<String, String> filterMap) {
		List<Flight> flights = findAll();
		List<Flight> searchedFlights = applySearch(flights, filterMap);
		return applyFilters(searchedFlights, filterMap);
	}

	public List<Flight> applySearch(List<Flight> flights, HashMap<String, String> filterMap) {
		List<Flight> flightsWithSearchTerms = new ArrayList<Flight>();
		
		String searchTerms = "searchTerms";
		if(filterMap.keySet().contains(searchTerms)) {
			String[] splitTerms = filterMap.get(searchTerms).split("+");
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
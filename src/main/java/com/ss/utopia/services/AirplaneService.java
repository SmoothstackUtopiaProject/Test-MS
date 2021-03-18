package com.ss.utopia.services;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ss.utopia.exceptions.AirplaneNotFoundException;
import com.ss.utopia.exceptions.AirplaneTypeNotFoundException;
import com.ss.utopia.models.Airplane;
import com.ss.utopia.models.AirplaneType;
import com.ss.utopia.repositories.AirplaneRepository;

@Service
public class AirplaneService {
	
	@Autowired
	private AirplaneRepository airplaneRepository;

	public List<Airplane> findAll() {
		return airplaneRepository.findAll();
	}

	public List<AirplaneType> findAllAirplaneTypes() {
		return airplaneRepository.findAllAirplaneTypes();
	}
	
	public Airplane findById(Integer airplaneId) throws AirplaneNotFoundException {
		Optional<Airplane> optionalAirplane = airplaneRepository.findById(airplaneId);
		if(!optionalAirplane.isPresent()) {
			throw new AirplaneNotFoundException("No Airplane with ID: " + airplaneId + " exists.");
		}
		return optionalAirplane.get();
	}

	public List<Airplane> findBySearchAndFilter(Map<String, String> filterMap) {
		List<Airplane> airplanes = findAll();
		if(!filterMap.keySet().isEmpty()) {
			airplanes = applyFilters(airplanes, filterMap);
		}
		return airplanes;
	}

	public List<Airplane> applyFilters(@Valid List<Airplane> airplanes, Map<String, String> filterMap) {
		// ID
		String airplaneId = "airplaneId";
		if(filterMap.keySet().contains(airplaneId)) {
			try {
				Integer parsedAirplaneId = Integer.parseInt(filterMap.get(airplaneId));
				airplanes = airplanes.stream()
				.filter(i -> i.getAirplaneId().equals(parsedAirplaneId))
				.collect(Collectors.toList());
			} 
			catch(IllegalArgumentException err){/*Do nothing*/}
		}

		// Type ID
		String airplaneTypeId = "airplaneTypeId";
		if(filterMap.keySet().contains(airplaneTypeId)) {
			try {
				Integer parsedTypeId = Integer.parseInt(filterMap.get(airplaneTypeId));
				airplanes = airplanes.stream()
				.filter(i -> i.getAirplaneTypeId().equals(parsedTypeId))
				.collect(Collectors.toList());
			}
			catch(IllegalArgumentException err){/*Do nothing*/}
		}

		// Search - (applied last due to save CPU usage
		return applySearch(airplanes, filterMap);
	}

	public List<Airplane> applySearch(@Valid List<Airplane> airplanes, Map<String, String> filterMap) {
		
		String searchTerms = "searchTerms";
		if(!filterMap.keySet().contains(searchTerms)) {
			return airplanes;
		}

		String formattedSearch = filterMap.get(searchTerms)
			.toLowerCase(Locale.getDefault())
			.replace(", ", ",");

		String[] splitTerms = formattedSearch.split(",");
		List<Airplane> airplanesWithSearchTerms = new ArrayList<>();
		for(Airplane airplane : airplanes) {
			boolean containsSearchTerms = true;
			
			String airplaneAsString = 
			airplane.getAirplaneId().toString() + 
			airplane.getAirplaneTypeId().toString()
			.toLowerCase(Locale.getDefault());
			
			for(String term : splitTerms) {
				if(!airplaneAsString.contains(term)) {
					containsSearchTerms = false;
					break;
				}
			}

			if(containsSearchTerms) {
				airplanesWithSearchTerms.add(airplane);
			}
		}
		return airplanesWithSearchTerms;
	}

	public Airplane insert(Integer airplaneTypeId) throws AirplaneTypeNotFoundException {
		Optional<AirplaneType> optionalType = airplaneRepository.findAirplaneTypeByAirplaneTypeId(airplaneTypeId);
		if(!optionalType.isPresent()) {
			throw new AirplaneTypeNotFoundException("No AirplaneType with ID: " + airplaneTypeId + " exist.");
		}
		return airplaneRepository.save(new Airplane(airplaneTypeId));
	}

	public void delete(Integer airplaneId) throws AirplaneNotFoundException {
		Optional<Airplane> optionalAirplane = airplaneRepository.findById(airplaneId);
		if(!optionalAirplane.isPresent()) {
			throw new AirplaneNotFoundException("No Airplane with ID: " + airplaneId + " exists.");
		}
		airplaneRepository.deleteById(airplaneId);
	}

	public Airplane update(Integer airplaneId, Integer airplaneTypeId) 
	throws AirplaneNotFoundException, AirplaneTypeNotFoundException {

		Optional<AirplaneType> optionalAirplaneType = airplaneRepository.findAirplaneTypeByAirplaneTypeId(airplaneTypeId);
		if(!optionalAirplaneType.isPresent()) {
			throw new AirplaneTypeNotFoundException("No AirplaneType with ID: " + airplaneTypeId + " exists.");
		}
		
		Airplane airplane = findById(airplaneId);
		airplane.setAirplaneTypeId(airplaneTypeId);
		return airplaneRepository.save(airplane);
	}
}
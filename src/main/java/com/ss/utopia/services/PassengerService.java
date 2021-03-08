package com.ss.utopia.services;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ss.utopia.exceptions.PassengerAlreadyExistsException;
import com.ss.utopia.exceptions.PassengerNotFoundException;
import com.ss.utopia.models.Passenger;
import com.ss.utopia.repositories.PassengerRepository;

@Service
public class PassengerService {

	@Autowired
	PassengerRepository passengerRepository;

	public List<Passenger> findAll() {
		return passengerRepository.findAll();
	}

	public Passenger findById(Integer id) throws PassengerNotFoundException {
		Optional<Passenger> optionalPassenger = passengerRepository.findById(id);
		if(!optionalPassenger.isPresent()) throw new PassengerNotFoundException("No Passenger with ID: " + id + " exist!");
		return optionalPassenger.get();
	}

	public Passenger findByBookingId(Integer passengerBookingId) throws PassengerNotFoundException {
		Optional<Passenger> optionalPassenger = passengerRepository.findByBookingId(passengerBookingId);
		if(!optionalPassenger.isPresent()) throw new PassengerNotFoundException("No Passenger with Booking ID: " + passengerBookingId + " exist!");
		return optionalPassenger.get();
	}

	public List<Passenger> findByPassportId(String passengerPassportId) {
		return passengerRepository.findByPassportId(passengerPassportId);
	}

	public List<Passenger> findBySearchAndFilter(HashMap<String, String> filterMap) {

		List<Passenger> passengers = findAll();
		if(!filterMap.keySet().isEmpty()) passengers = applyFilters(passengers, filterMap);
		return passengers;
	}

	public List<Passenger> applyFilters(List<Passenger> passengers, HashMap<String, String> filterMap) {
		// ID
		String passengerId = "passengerId";
		if(filterMap.keySet().contains(passengerId)) {
			try {
				Integer parsedPassengerId = Integer.parseInt(filterMap.get(passengerId));
				passengers = passengers.stream()
				.filter(i -> i.getPassengerId().equals(parsedPassengerId))
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}

		// Booking ID
		String passengerBookingId = "passengerBookingId";
		if(filterMap.keySet().contains(passengerBookingId)) {
			try {
				Integer parsedPassengerBookingId = Integer.parseInt(filterMap.get(passengerBookingId));
				passengers = passengers.stream()
				.filter(i -> i.getPassengerBookingId().equals(parsedPassengerBookingId))
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}

		// Passport ID
		String passengerPassportId = "passengerPassportId";
		if(filterMap.keySet().contains(passengerPassportId)) {
			try {
				String parsedPassengerPassportId = filterMap.get(passengerPassportId);
				passengers = passengers.stream()
				.filter(i -> i.getPassengerPassportId().equals(parsedPassengerPassportId))
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}

		// First Name
		String passengerFirstName = "passengerFirstName";
		if(filterMap.keySet().contains(passengerFirstName)) {
			try {
				String parsedPassengerFirstName = filterMap.get(passengerFirstName);
				passengers = passengers.stream()
				.filter(i -> i.getPassengerFirstName().equals(parsedPassengerFirstName))
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}

		// Last Name
		String passengerLastName = "passengerLastName";
		if(filterMap.keySet().contains(passengerLastName)) {
			try {
				String parsedPassengerLastName = filterMap.get(passengerLastName);
				passengers = passengers.stream()
				.filter(i -> i.getPassengerLastName().equals(parsedPassengerLastName))
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}

		// DateOfBirth
		String passengerDateOfBirth = "passengerDateOfBirth";
		if(filterMap.keySet().contains(passengerDateOfBirth)) {
			try {
				Date parsedPassengerDateOfBirth = Date.valueOf(filterMap.get(passengerDateOfBirth));
				passengers = passengers.stream()
				.filter(i -> i.getPassengerDateOfBirth().equals(parsedPassengerDateOfBirth))
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}

		// Sex
		String passengerSex = "passengerSex";
		if(filterMap.keySet().contains(passengerSex)) {
			try {
				String parsedPassengerSex = filterMap.get(passengerSex);
				passengers = passengers.stream()
				.filter(i -> i.getPassengerSex().equals(parsedPassengerSex))
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}

		// Address
		String passengerAddress = "passengerAddress";
		if(filterMap.keySet().contains(passengerAddress)) {
			try {
				String parsedPassengerAddress = filterMap.get(passengerAddress);
				passengers = passengers.stream()
				.filter(i -> i.getPassengerAddress().equals(parsedPassengerAddress))
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}

		// Veteran
		String passengerIsVeteran = "passengerIsVeteran";
		if(filterMap.keySet().contains(passengerIsVeteran)) {
			try {
				Boolean parsedPassengerIsVeteran = Boolean.valueOf(filterMap.get(passengerIsVeteran));
				passengers = passengers.stream()
				.filter(i -> i.getPassengerIsVeteran().equals(parsedPassengerIsVeteran))
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}

		// Age - Exact Match
		String passengerAge = "passengerAge";
		if(filterMap.keySet().contains(passengerAge)) {
			try {
				Integer parsedPassengerAge = Integer.parseInt(filterMap.get(passengerAge));
				passengers = passengers.stream()
				.filter(i -> Period.between(i.getPassengerDateOfBirth().toLocalDate(), LocalDate.now()).getYears() == parsedPassengerAge)
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}	

		// Age - Greater Than
		String passengerAgeGreaterThan = "passengerAgeGreaterThan";
		if(filterMap.keySet().contains(passengerAgeGreaterThan)) {
			try {
				Integer parsedPassengerAgeGreaterThan = Integer.parseInt(filterMap.get(passengerAgeGreaterThan));
				passengers = passengers.stream()
				.filter(i -> Period.between(i.getPassengerDateOfBirth().toLocalDate(), LocalDate.now()).getYears() > parsedPassengerAgeGreaterThan)
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}	

		// Age - Less Than
		String passengerAgeLessThan = "passengerAgeLessThan";
		if(filterMap.keySet().contains(passengerAgeLessThan)) {
			try {
				Integer parsedPassengerAgeLessThan = Integer.parseInt(filterMap.get(passengerAgeLessThan));
				passengers = passengers.stream()
				.filter(i -> Period.between(i.getPassengerDateOfBirth().toLocalDate(), LocalDate.now()).getYears() < parsedPassengerAgeLessThan)
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}

		// Search - (applied last due to save CPU uspassengerAge
		return applySearch(passengers, filterMap);
	}

	public List<Passenger> applySearch(List<Passenger> passengers, HashMap<String, String> filterMap) {
		List<Passenger> passengersWithSearchTerms = new ArrayList<Passenger>();
		
		String searchTerms = "searchTerms";
		if(filterMap.keySet().contains(searchTerms)) {
			String formattedSearch = filterMap.get(searchTerms)
			.toLowerCase()
			.replace(", ", ",");
			String[] splitTerms = formattedSearch.split(",");
			ObjectMapper mapper = new ObjectMapper();

			for(Passenger passenger : passengers) {
				boolean containsSearchTerms = true;
				
				try {
					String passengerAsString = mapper.writeValueAsString(passenger)
					.toLowerCase()
					.replace("passenderid", "")
					.replace("passengerbookingid", "")
					.replace("passengerpassportid", "")
					.replace("passengerfirstname", "")
					.replace("passengerlastname", "")
					.replace("passengerdateofbirth", "")
					.replace("passengersex", "")
					.replace("passengeraddress", "")
					.replace("passengerisveteran", "");
					
					for(String term : splitTerms) {
						if(!passengerAsString.contains(term)) {
							containsSearchTerms = false;
							break;
						}
					}
				} catch(JsonProcessingException err){
					containsSearchTerms = false;
				}

				if(containsSearchTerms) {
					passengersWithSearchTerms.add(passenger);
				}
			}
		}
		return passengersWithSearchTerms;
	}

	public Passenger insert(Integer passengerBookingId, String passengerPassportId, String passengerFirstName, 
		String passengerLastName, Date passengerDateOfBirth, String passengerSex, String passengerAddress,Boolean passengerIsVeteran) 
		throws PassengerAlreadyExistsException {

		List<Passenger> passengerExistCheck = findByPassportId(passengerPassportId);
		if(!passengerExistCheck.isEmpty()) throw new PassengerAlreadyExistsException(
			"A Passenger with the Passport ID: " + passengerPassportId + " already exists.");

		return passengerRepository.save(new Passenger(
			passengerBookingId, passengerPassportId, passengerFirstName, passengerLastName, passengerDateOfBirth, passengerSex, passengerAddress, passengerIsVeteran
		));
	}

	public Passenger update(Integer id, Integer passengerBookingId, String passengerPassportId, String passengerFirstName, 
	String passengerLastName, Date passengerDateOfBirth, String passengerSex, String passengerAddress,Boolean passengerIsVeteran) 
	throws PassengerNotFoundException {	

		findById(id);
		return passengerRepository.save(
			new Passenger(id, passengerBookingId, passengerPassportId, passengerFirstName, passengerLastName, passengerDateOfBirth, passengerSex, passengerAddress, passengerIsVeteran)
		);
	}

	public void delete(Integer id) throws IllegalArgumentException, PassengerNotFoundException {
		findById(id);
		passengerRepository.deleteById(id);
	}
}
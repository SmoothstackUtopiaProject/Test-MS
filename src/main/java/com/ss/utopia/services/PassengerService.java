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

	public List<Passenger> findByBookingId(Integer bookingId) {
		return passengerRepository.findByBookingId(bookingId);
	}

	public List<Passenger> findByPassportId(String passportId) {
		return passengerRepository.findByPassportId(passportId);
	}

	public List<Passenger> findBySearchAndFilter(HashMap<String, String> filterMap) {
		List<Passenger> passengers = findAll();
		List<Passenger> searchedPassengers = applySearch(passengers, filterMap);
		return applyFilters(searchedPassengers, filterMap);
	}

	public List<Passenger> applySearch(List<Passenger> passengers, HashMap<String, String> filterMap) {
		List<Passenger> passengersWithSearchTerms = new ArrayList<Passenger>();
		
		String searchTerms = "searchTerms";
		if(filterMap.keySet().contains(searchTerms)) {
			String formattedSearch = filterMap.get(searchTerms)
			.toLowerCase()
			.replace(", ", ",");
			System.out.println(formattedSearch);
			String[] splitTerms = formattedSearch.split(",");
			ObjectMapper mapper = new ObjectMapper();
			
			for(Passenger passenger : passengers) {
				boolean containsSearchTerms = true;
				
				try {
					String passengerAsString = mapper.writeValueAsString(passenger)
					.toLowerCase()
					.replace("bookingid", "")
					.replace("passportid", "")
					.replace("firstname", "")
					.replace("lastname", "")
					.replace("dateofbirth", "")
					.replace("sex", "")
					.replace("address", "")
					.replace("isveteran", "");
					
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

	public List<Passenger> applyFilters(List<Passenger> passengers, HashMap<String, String> filterMap) {
		List<Passenger> filteredPassengers = passengers;

		// ID
		String passengerId = "passengerId";
		if(filterMap.keySet().contains(passengerId)) {
			try {
				Integer parsedPassengerId = Integer.parseInt(filterMap.get(passengerId));
				filteredPassengers = filteredPassengers.stream()
				.filter(i -> i.getId().equals(parsedPassengerId))
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}

		// Booking ID
		String bookingId = "bookingId";
		if(filterMap.keySet().contains(bookingId)) {
			try {
				Integer parsedBookingId = Integer.parseInt(filterMap.get(bookingId));
				filteredPassengers = filteredPassengers.stream()
				.filter(i -> i.getBookingId().equals(parsedBookingId))
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}

		// Passport ID
		String passportId = "passportId";
		if(filterMap.keySet().contains(passportId)) {
			try {
				String parsedPassportId = filterMap.get(passportId);
				filteredPassengers = filteredPassengers.stream()
				.filter(i -> i.getPassportId().equals(parsedPassportId))
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}

		// First Name
		String firstName = "firstName";
		if(filterMap.keySet().contains(firstName)) {
			try {
				String parsedFirstName = filterMap.get(firstName);
				filteredPassengers = filteredPassengers.stream()
				.filter(i -> i.getFirstName().equals(parsedFirstName))
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}

		// Last Name
		String lastName = "lastName";
		if(filterMap.keySet().contains(lastName)) {
			try {
				String parsedLastName = filterMap.get(lastName);
				filteredPassengers = filteredPassengers.stream()
				.filter(i -> i.getLastName().equals(parsedLastName))
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}

		// DateOfBirth
		String dateOfBirth = "dateOfBirth";
		if(filterMap.keySet().contains(dateOfBirth)) {
			try {
				Date parsedDateOfBirth = Date.valueOf(filterMap.get(dateOfBirth));
				filteredPassengers = filteredPassengers.stream()
				.filter(i -> i.getDateOfBirth().equals(parsedDateOfBirth))
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}

		// Sex
		String sex = "sex";
		if(filterMap.keySet().contains(sex)) {
			try {
				String parsedSex = filterMap.get(sex);
				filteredPassengers = filteredPassengers.stream()
				.filter(i -> i.getSex().equals(parsedSex))
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}

		// Address
		String address = "address";
		if(filterMap.keySet().contains(address)) {
			try {
				String parsedAddress = filterMap.get(address);
				filteredPassengers = filteredPassengers.stream()
				.filter(i -> i.getAddress().equals(parsedAddress))
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}

		// Veteran
		String isVeteran = "isVeteran";
		if(filterMap.keySet().contains(isVeteran)) {
			try {
				Boolean parsedVeteran = Boolean.valueOf(filterMap.get(isVeteran));
				filteredPassengers = filteredPassengers.stream()
				.filter(i -> i.getIsVeteran().equals(parsedVeteran))
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}

		// Age - Exact Match
		String age = "age";
		if(filterMap.keySet().contains(age)) {
			try {
				Integer parsedAge = Integer.parseInt(filterMap.get(age));
				filteredPassengers = filteredPassengers.stream()
				.filter(i -> Period.between(i.getDateOfBirth().toLocalDate(), LocalDate.now()).getYears() == parsedAge)
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}	

		// Age - Greater Than
		String ageGreaterThan = "ageGreaterThan";
		if(filterMap.keySet().contains(ageGreaterThan)) {
			try {
				Integer parsedAgeGreaterThan = Integer.parseInt(filterMap.get(ageGreaterThan));
				filteredPassengers = filteredPassengers.stream()
				.filter(i -> Period.between(i.getDateOfBirth().toLocalDate(), LocalDate.now()).getYears() > parsedAgeGreaterThan)
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}	

		// Age - Less Than
		String ageLessThan = "ageLessThan";
		if(filterMap.keySet().contains(ageLessThan)) {
			try {
				Integer parsedAgeLessThan = Integer.parseInt(filterMap.get(ageLessThan));
				filteredPassengers = filteredPassengers.stream()
				.filter(i -> Period.between(i.getDateOfBirth().toLocalDate(), LocalDate.now()).getYears() < parsedAgeLessThan)
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}	
		return filteredPassengers;
	}

	public Passenger insert(Integer bookingId, String passportId, String firstName, 
		String lastName, Date dateOfBirth, String sex, String address,Boolean isVeteran) 
		throws PassengerAlreadyExistsException {

		List<Passenger> passengerExistCheck = findByPassportId(passportId);
		if(!passengerExistCheck.isEmpty()) throw new PassengerAlreadyExistsException(
			"A Passenger with the Passport ID: " + passportId + " already exists.");

		return passengerRepository.save(new Passenger(
			bookingId, passportId, firstName, lastName, dateOfBirth, sex, address, isVeteran
		));
	}

	public Passenger update(Integer id, Integer bookingId, String passportId, String firstName, 
	String lastName, Date dateOfBirth, String sex, String address,Boolean isVeteran) 
	throws PassengerNotFoundException {	

		findById(id);
		return passengerRepository.save(
			new Passenger(bookingId, passportId, firstName, lastName, dateOfBirth, sex, address, isVeteran)
		);
	}

	public void delete(Integer id) throws IllegalArgumentException, PassengerNotFoundException {
		findById(id);
		passengerRepository.deleteById(id);
	}
}
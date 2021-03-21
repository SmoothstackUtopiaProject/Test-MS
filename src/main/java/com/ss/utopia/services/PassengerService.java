package com.ss.utopia.services;

import java.sql.Date;
import java.util.Map;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ss.utopia.exceptions.PassengerAlreadyExistsException;
import com.ss.utopia.exceptions.PassengerNotFoundException;
import com.ss.utopia.filters.PassengerFilters;
import com.ss.utopia.models.Passenger;
import com.ss.utopia.repositories.PassengerRepository;

@Service
public class PassengerService {

	@Autowired
	private PassengerRepository passengerRepository;

	public List<Passenger> findAll() {
		return passengerRepository.findAll();
	}

	public Passenger findById(Integer id) throws PassengerNotFoundException {
		Optional<Passenger> optionalPassenger = passengerRepository.findById(id);
		if(!optionalPassenger.isPresent()) {
			throw new PassengerNotFoundException("No Passenger with ID: " + id + " exist!");
		}
		return optionalPassenger.get();
	}

	public Passenger findByBookingId(Integer passengerBookingId) throws PassengerNotFoundException {
		Optional<Passenger> optionalPassenger = passengerRepository.findByBookingId(passengerBookingId);
		if(!optionalPassenger.isPresent()) {
			throw new PassengerNotFoundException("No Passenger with Booking ID: " + passengerBookingId + " exist!");
		}
		return optionalPassenger.get();
	}

	public List<Passenger> findByPassportId(String passengerPassportId) {
		return passengerRepository.findByPassportId(passengerPassportId);
	}

	public List<Passenger> findByFilter(Map<String, String> filterMap) {
		List<Passenger> passengers = findAll();
		if(!filterMap.keySet().isEmpty()) {
			passengers = PassengerFilters.apply(passengers, filterMap);
		}
		return passengers;
	}

	public Passenger insert(Integer passengerBookingId, String passengerPassportId, String passengerFirstName, 
		String passengerLastName, Date passengerDateOfBirth, String passengerSex, String passengerAddress,
		Boolean passengerIsVeteran) throws PassengerAlreadyExistsException {

		List<Passenger> passengerExistCheck = findByPassportId(passengerPassportId);
		if(!passengerExistCheck.isEmpty()) {
			throw new PassengerAlreadyExistsException(
				"A Passenger with the Passport ID: " + 
				passengerPassportId + " already exists."
			);
		}

		return passengerRepository.save(new Passenger(
			passengerBookingId, passengerPassportId, passengerFirstName, passengerLastName, 
			passengerDateOfBirth, passengerSex, passengerAddress, passengerIsVeteran
		));
	}

	public Passenger update(Integer passengerId, Integer passengerBookingId, String passengerPassportId, 
	String passengerFirstName, String passengerLastName, Date passengerDateOfBirth, String passengerSex, 
	String passengerAddress, Boolean passengerIsVeteran) throws PassengerNotFoundException {	

		findById(passengerId);
		return passengerRepository.save(new Passenger(
			passengerId, passengerBookingId, passengerPassportId, passengerFirstName, passengerLastName,
			passengerDateOfBirth, passengerSex, passengerAddress, passengerIsVeteran
		));
	}

	public void delete(Integer id) throws IllegalArgumentException, PassengerNotFoundException {
		findById(id);
		passengerRepository.deleteById(id);
	}
}
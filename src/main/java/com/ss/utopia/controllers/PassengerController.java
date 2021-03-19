package com.ss.utopia.controllers;

import java.net.ConnectException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ss.utopia.exceptions.PassengerAlreadyExistsException;
import com.ss.utopia.exceptions.PassengerNotFoundException;
import com.ss.utopia.models.Passenger;
import com.ss.utopia.models.HttpError;
import com.ss.utopia.services.PassengerService;

@RestController
@RequestMapping(
	value = "/passengers",
	produces = { "application/json", "application/xml", "text/xml" },
	consumes = MediaType.ALL_VALUE
)
public class PassengerController {

	@Autowired
	PassengerService passengerService;
	
	@GetMapping
	public ResponseEntity<Object> findAll()
	throws ConnectException, SQLException {
		
		List<Passenger> passengers = passengerService.findAll();
		return !passengers.isEmpty()
			? new ResponseEntity<>(passengers, HttpStatus.OK)
			: new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}

	@GetMapping("{passengerId}")
	public ResponseEntity<Object> findById(@PathVariable String passengerId)
	throws ConnectException, SQLException {

		try {
			Integer formattedId = Integer.parseInt(passengerId);
			Passenger passenger = passengerService.findById(formattedId);
			return new ResponseEntity<>(passenger, HttpStatus.OK);

		} catch(PassengerNotFoundException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 404), HttpStatus.NOT_FOUND);

		} catch(IllegalArgumentException | NullPointerException err) {
			String errorMessage = "Cannot process Passenger ID " + err.getMessage()
			.substring(0, 1).toLowerCase() + err.getMessage()
			.substring(1, err.getMessage().length()).replaceAll("[\"]", "");
			return new ResponseEntity<>(new HttpError(errorMessage, 400), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/booking/{id}")
	public ResponseEntity<Object> findByBookingId(@PathVariable String bookingId) {

		try {
			Integer formattedBookingId = Integer.parseInt(bookingId);
			Passenger passenger = passengerService.findByBookingId(formattedBookingId);
			return new ResponseEntity<>(passenger, HttpStatus.OK);
			// return !passengerList.isEmpty()
			// 	? new ResponseEntity<>(passengerList, HttpStatus.OK)
			// 	: new ResponseEntity<>(new HttpError("No Passenger(s) with Booking ID: " + bookingId + " exists.", 404), HttpStatus.NOT_FOUND);

		} catch (PassengerNotFoundException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 404), HttpStatus.NOT_FOUND);
		
		} catch(IllegalArgumentException | NullPointerException err) {
			String errorMessage = "Cannot process Passenger Booking ID " + err.getMessage()
			.substring(0, 1).toLowerCase() + err.getMessage()
			.substring(1, err.getMessage().length()).replaceAll("[\"]", "");
			return new ResponseEntity<>(new HttpError(errorMessage, 400), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/passport/{id}")
	public ResponseEntity<Object> findByPassportId(@PathVariable String id) {
		try {
			List<Passenger> passengerList = passengerService.findByPassportId(id);
			return passengerList.isEmpty()
				? new ResponseEntity<>(passengerList, HttpStatus.OK)
				: new ResponseEntity<>(new HttpError("No Passenger with Passport ID: " + id + " exists.", 404), HttpStatus.NOT_FOUND);
			
		} catch(IllegalArgumentException | NullPointerException err) {
			String errorMessage = "Cannot process Passenger Passport ID " + err.getMessage()
			.substring(0, 1).toLowerCase() + err.getMessage()
			.substring(1, err.getMessage().length()).replaceAll("[\"]", "");
			return new ResponseEntity<>(new HttpError(errorMessage, 400), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/search")
	public ResponseEntity<Object> findBySearchAndFilter(@RequestBody Map<String, String> filterMap)
	throws ConnectException, SQLException {

		List<Passenger> passengers = passengerService.findBySearchAndFilter(filterMap);
		return !passengers.isEmpty()
			? new ResponseEntity<>(passengers, HttpStatus.OK)
			: new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}

	@PostMapping
	public ResponseEntity<Object> insert(@RequestBody Map<String, String> passengerMap) {

		try {
			Integer passengerBookingId = Integer.parseInt(passengerMap.get("passengerBookingId"));
			String passengerPassportId = passengerMap.get("passengerPassportId");
			String passengerFirstName = passengerMap.get("passengerFirstName");
			String passengerLastName = passengerMap.get("passengerLastName");
			Date passengerDateOfBirth = Date.valueOf(passengerMap.get("passengerDateOfBirth"));
			String passengerSex = passengerMap.get("passengerSex");
			String passengerAddress = passengerMap.get("passengerAddress");
			Boolean passengerIsVeteran = Boolean.parseBoolean(passengerMap.get("passengerIsVeteran"));

			Passenger newPassenger = passengerService.insert(
				passengerBookingId, passengerPassportId, passengerFirstName, passengerLastName, passengerDateOfBirth, passengerSex, passengerAddress, passengerIsVeteran
			);
			return new ResponseEntity<>(newPassenger, HttpStatus.CREATED);

		} catch(PassengerAlreadyExistsException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 409), HttpStatus.CONFLICT);

		} catch(IllegalArgumentException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 400), HttpStatus.BAD_REQUEST);

		} catch(NullPointerException err) {
			String errorMessage = "Cannot process Passenger, " + err.getMessage()
			.substring(0, 1).toLowerCase() + err.getMessage()
			.substring(1, err.getMessage().length());
			return new ResponseEntity<>(new HttpError(errorMessage, 400), HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping
	public ResponseEntity<Object> update(@RequestBody Map<String, String> passengerMap) {

		try {
			Integer passengerId = Integer.parseInt(passengerMap.get("passengerId"));
			Integer passengerBookingId = Integer.parseInt(passengerMap.get("passengerBookingId"));
			String passengerPassportId = passengerMap.get("passengerPassportId");
			String passengerFirstName = passengerMap.get("passengerFirstName");
			String passengerLastName = passengerMap.get("passengerLastName");
			Date passengerDateOfBirth = Date.valueOf(passengerMap.get("passengerDateOfBirth"));
			String passengerSex = passengerMap.get("passengerSex");
			String passengerAddress = passengerMap.get("passengerAddress");
			Boolean passengerIsVeteran = Boolean.parseBoolean(passengerMap.get("passengerIsVeteran"));

			Passenger newPassenger = passengerService.update(
				passengerId, passengerBookingId, passengerPassportId, passengerFirstName, passengerLastName, passengerDateOfBirth, passengerSex, passengerAddress, passengerIsVeteran
			);
			return new ResponseEntity<>(newPassenger, HttpStatus.ACCEPTED);

		} catch(PassengerNotFoundException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 404), HttpStatus.NOT_FOUND);

		} catch(IllegalArgumentException | NullPointerException err) {
			String errorMessage = "Cannot process Passenger, " + err.getMessage()
			.substring(0, 1).toLowerCase() + err.getMessage()
			.substring(1, err.getMessage().length());
			return new ResponseEntity<>(new HttpError(errorMessage, 400), HttpStatus.BAD_REQUEST);
		} 
	}

	@DeleteMapping("{passengerId}")
	public ResponseEntity<Object> delete(@PathVariable String passengerId) 

	throws ConnectException, SQLException {

		try {
			Integer formattedId = Integer.parseInt(passengerId);
			passengerService.delete(formattedId);
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

		} catch(PassengerNotFoundException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 404), HttpStatus.NOT_FOUND);
		
		} catch(IllegalArgumentException | NullPointerException err) {
			String errorMessage = "Cannot process Passenger ID " + err.getMessage()
			.substring(0, 1).toLowerCase() + err.getMessage()
			.substring(1, err.getMessage().length()).replaceAll("[\"]", "");
			return new ResponseEntity<>(new HttpError(errorMessage, 400), HttpStatus.BAD_REQUEST);
		}
	}

	@ExceptionHandler(ConnectException.class)
	public ResponseEntity<Object> invalidConnection() {
		return new ResponseEntity<>(new HttpError("Service temporarily unavailabe.", 500), HttpStatus.SERVICE_UNAVAILABLE);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Object> invalidMessage() {
		return new ResponseEntity<>(new HttpError("Invalid HTTP message content.", 400), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(SQLException.class)
	public ResponseEntity<Object> invalidSQL() {
		return new ResponseEntity<>(new HttpError("Service temporarily unavailabe.", 500), HttpStatus.SERVICE_UNAVAILABLE);
	}
}
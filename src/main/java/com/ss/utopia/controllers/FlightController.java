package com.ss.utopia.controllers;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.Map;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ss.utopia.exceptions.AirplaneAlreadyInUseException;
import com.ss.utopia.exceptions.FlightNotFoundException;
import com.ss.utopia.models.Flight;
import com.ss.utopia.models.FlightWithReferenceData;
import com.ss.utopia.models.ErrorMessage;
import com.ss.utopia.services.FlightService;

@RestController
@RequestMapping("/flights")
public class FlightController {
	
	@Autowired
	private FlightService flightService;

	@GetMapping("/health")
	public ResponseEntity<Object> health() {
		return new ResponseEntity<>("\"status\": \"up\"", HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<Object> findAll() {
		List<Flight> allFlights = flightService.findAll();
		return !allFlights.isEmpty() 
			? new ResponseEntity<>(allFlights, HttpStatus.OK)
			: new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("{path}")
	public ResponseEntity<Object> findById(@PathVariable String path) throws FlightNotFoundException {
		Integer flightId = Integer.parseInt(path);
		Flight flight = flightService.findById(flightId);
		return new ResponseEntity<>(flight, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Object> create(@RequestBody Map<String, String> flightMap) throws AirplaneAlreadyInUseException {
		Integer routeId = Integer.parseInt(flightMap.get("flightRouteId"));
		Integer airplaneId = Integer.parseInt(flightMap.get("flightAirplaneId"));
		String dateTime = flightMap.get("flightDepartureTime");
		Integer seatingId = Integer.parseInt(flightMap.get("flightSeatingId"));
		Integer duration = Integer.parseInt(flightMap.get("flightDuration"));
		String status = flightMap.get("flightStatus");
		
		return new ResponseEntity<>(
			flightService.insert(
				routeId, airplaneId, dateTime, seatingId, duration, status
			), HttpStatus.CREATED
		);
	}

	@PostMapping("/search")
	public ResponseEntity<Object> findBySearchAndFilter(@RequestBody Map<String, String> filterMap) {
		List<FlightWithReferenceData> flights = flightService.findBySearchAndFilter(filterMap);
		return !flights.isEmpty() 
			? new ResponseEntity<>(flights, HttpStatus.OK)
			: new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@PutMapping
	public ResponseEntity<Object> update(@RequestBody Map<String, String> flightMap) 
	throws AirplaneAlreadyInUseException, FlightNotFoundException {
		Integer id = Integer.parseInt(flightMap.get("flightId"));
		Integer routeId = Integer.parseInt(flightMap.get("flightRouteId"));
		Integer airplaneId = Integer.parseInt(flightMap.get("flightAirplaneId"));
		String dateTime = flightMap.get("flightDepartureTime");
		Integer seatingId = Integer.parseInt(flightMap.get("flightSeatingId"));
		Integer duration = Integer.parseInt(flightMap.get("flightDuration"));
		String status = flightMap.get("flightStatus");
		return new ResponseEntity<>(
			flightService.update(
				id, routeId, airplaneId, dateTime, seatingId, duration, status
			), HttpStatus.CREATED
		);
	}
	
	@DeleteMapping("{flightId}")
	public ResponseEntity<Object> deleteById(@PathVariable String flightId) throws FlightNotFoundException {
		flightService.deleteById(Integer.parseInt(flightId));
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	
	// Exception Handling
	// ========================================================================
	@ExceptionHandler(AirplaneAlreadyInUseException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ResponseEntity<Object> airplaneAlreadyInUseException(Throwable err) {
		return new ResponseEntity<>(
			new ErrorMessage(err.getMessage()), 
			HttpStatus.CONFLICT
		);
	}

	@ExceptionHandler(FlightNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<Object> flightNotFoundException(Throwable err) {
		return new ResponseEntity<>(
			new ErrorMessage(err.getMessage()), 
			HttpStatus.NOT_FOUND
		);
	}

	@ExceptionHandler(ConnectException.class)
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	public ResponseEntity<Object> invalidConnection() {
		return new ResponseEntity<>(
			new ErrorMessage("Service temporarily unavailabe."), 
			HttpStatus.SERVICE_UNAVAILABLE
		);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Object> invalidMessage() {
		return new ResponseEntity<>(
			new ErrorMessage("Invalid HTTP message content."), 
			HttpStatus.BAD_REQUEST
		);
	}

	@ExceptionHandler(SQLException.class)
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	public ResponseEntity<Object> invalidSQL() {
		return new ResponseEntity<>(
			new ErrorMessage("Service temporarily unavailabe."), 
			HttpStatus.SERVICE_UNAVAILABLE
		);
	}
}

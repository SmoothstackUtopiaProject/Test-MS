package com.ss.utopia.controllers;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.RestController;

import com.ss.utopia.exceptions.FlightNotFoundException;
import com.ss.utopia.models.Flight;
import com.ss.utopia.models.HttpError;
import com.ss.utopia.services.FlightService;

@RestController
@RequestMapping(value = "/flights")
public class FlightController {
	
	@Autowired
	FlightService flightService;

	@GetMapping
	public ResponseEntity<Object> findAll() throws ConnectException, SQLException{
		List<Flight> allFlights = flightService.findAll();
		return !allFlights.isEmpty() ? new ResponseEntity<>(allFlights, HttpStatus.OK)
				: new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}
	

	@GetMapping("{path}")
	public ResponseEntity<Object> findById(@PathVariable String path)
	throws ConnectException, SQLException {

		try {
			Integer flightId = Integer.parseInt(path);
			Flight flight = flightService.findById(flightId);
			return new ResponseEntity<>(flight, HttpStatus.OK);

		} catch(IllegalArgumentException | NullPointerException err) {
			String errorMessage = "Cannot process Flight ID " + err.getMessage()
			.substring(0, 1).toLowerCase() + err.getMessage()
			.substring(1, err.getMessage().length()).replaceAll("[\"]", "");
			return new ResponseEntity<>(new HttpError(errorMessage, 400), HttpStatus.BAD_REQUEST);
			
		} catch(FlightNotFoundException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 404), HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping
	public ResponseEntity<Flight> create(@Valid @RequestBody Flight flight ) {
		return new ResponseEntity<>(flightService.insert(flight), HttpStatus.CREATED);
	}
	
	// @PutMapping
	// public ResponseEntity<Flight> update(@Valid @RequestBody Flight flight){
	// 	return flightService.findById(flightId)
	// 			.map(flightObj -> {
	// 				flight.setId(flightObj.getId());
	// 				return ResponseEntity.ok(flightService.update(flight));
	// 			})
	// 			.orElseGet(() -> ResponseEntity.notFound().build());
	// }
	
	// @DeleteMapping("{flightId}")
	// public ResponseEntity<Flight> deleteById(@PathVariable Integer flightId) {
	// 	return flightService.findById(flightId)
	// 			.map(flight -> {
	// 				flightService.deleteById(flightId);
	// 				return ResponseEntity.ok(flight);
	// 			})
	// 			.orElseGet(() -> ResponseEntity.notFound().build());
	// }
	
	@PostMapping("/search")
	public ResponseEntity<Object> findBySearchAndFilter(@RequestBody HashMap<String, String> filterMap) {

		List<Flight> flights = flightService.findBySearchAndFilter(filterMap);
		return !flights.isEmpty() 
			? new ResponseEntity<>(flights, HttpStatus.OK)
			: new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}
	
	@ExceptionHandler(ConnectException.class)
	public ResponseEntity<Object> invalidConnection() {
		return new ResponseEntity<>(null, HttpStatus.SERVICE_UNAVAILABLE);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Object> invalidMessage() {
		return new ResponseEntity<>("Invalid Message Content!", HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(SQLException.class)
	public ResponseEntity<Object> invalidSQL() {
		return new ResponseEntity<>(null, HttpStatus.SERVICE_UNAVAILABLE);
	}
}
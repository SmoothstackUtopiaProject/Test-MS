package com.ss.utopia.controllers;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RestController;

import com.ss.utopia.exceptions.AirplaneAlreadyInUseException;
import com.ss.utopia.exceptions.FlightNotFoundException;
import com.ss.utopia.models.Flight;
import com.ss.utopia.models.FlightWithReferenceData;
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
		return !allFlights.isEmpty() 
			? new ResponseEntity<>(allFlights, HttpStatus.OK)
			: new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("{path}")
	public ResponseEntity<Object> findById(@PathVariable String path)
	throws ConnectException, SQLException {

		try {
			Integer flightId = Integer.parseInt(path);
			Flight flight = flightService.findById(flightId);
			return new ResponseEntity<>(flight, HttpStatus.OK);

		} catch(IllegalArgumentException err) {
			String errorMessage = "Cannot process Flight ID " + err.getMessage()
			.substring(0, 1).toLowerCase() + err.getMessage()
			.substring(1, err.getMessage().length()).replaceAll("[\"]", "");
			return new ResponseEntity<>(new HttpError(errorMessage, 400), HttpStatus.BAD_REQUEST);
			
		} catch(FlightNotFoundException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 404), HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/search")
	public ResponseEntity<Object> findBySearchAndFilter(@RequestBody HashMap<String, String> filterMap) {

		List<FlightWithReferenceData> flights = flightService.findBySearchAndFilter(filterMap);
		return !flights.isEmpty() 
			? new ResponseEntity<>(flights, HttpStatus.OK)
			: new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}
	
	@PostMapping
	public ResponseEntity<Object> create(@RequestBody HashMap<String, String> flightMap) throws ConnectException, SQLException{
		
		try {
			Integer routeId = Integer.parseInt(flightMap.get("flightRouteId"));
			Integer airplaneId = Integer.parseInt(flightMap.get("flightAirplaneId"));
			String dateTime = flightMap.get("flightDepartureTime");
			Integer seatingId = Integer.parseInt(flightMap.get("flightSeatingId"));
			Integer duration = Integer.parseInt(flightMap.get("flightDuration"));
			String status = flightMap.get("flightStatus");
			
			return new ResponseEntity<>(flightService.insert(routeId, airplaneId, dateTime, seatingId, duration, status), HttpStatus.CREATED);
		} catch (AirplaneAlreadyInUseException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 400), HttpStatus.BAD_REQUEST);
		} 
	}
	
	@PutMapping
	public ResponseEntity<Object> update(@RequestBody HashMap<String, String> flightMap) throws ConnectException, SQLException{
		
		try {
			Integer id = Integer.parseInt(flightMap.get("flightId"));
			Integer routeId = Integer.parseInt(flightMap.get("flightRouteId"));
			Integer airplaneId = Integer.parseInt(flightMap.get("flightAirplaneId"));
			String dateTime = flightMap.get("flightDepartureTime");
			Integer seatingId = Integer.parseInt(flightMap.get("flightSeatingId"));
			Integer duration = Integer.parseInt(flightMap.get("flightDuration"));
			String status = flightMap.get("flightStatus");
			
			return new ResponseEntity<>(flightService.update(id, routeId, airplaneId, dateTime, seatingId, duration, status), HttpStatus.CREATED);
		} catch (AirplaneAlreadyInUseException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 409), HttpStatus.CONFLICT);
		} catch (FlightNotFoundException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 400), HttpStatus.BAD_REQUEST);
		} 
	}
	
	/*
	 * @PutMapping public ResponseEntity<Object> update(@RequestBody Flight flight){
	 * try { Flight newAirplane = flightService.update(flight); return new
	 * ResponseEntity<>(newAirplane, HttpStatus.OK); } catch
	 * (FlightNotFoundException err) { return new ResponseEntity<>(new
	 * HttpError(err.getMessage(), 404), HttpStatus.NOT_FOUND); }
	 * catch(ConnectException | IllegalArgumentException | SQLException err) {
	 * return new ResponseEntity<>(new HttpError("Cannot process flightId: " +
	 * flightId, 400), HttpStatus.BAD_REQUEST); } }
	 */
	
	@DeleteMapping("{flightId}")
	public ResponseEntity<Object> deleteById(@PathVariable Integer flightId) {
		try {
			flightService.deleteById(flightId);
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		} catch(FlightNotFoundException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 404), HttpStatus.NOT_FOUND);
		} catch(ConnectException | IllegalArgumentException | SQLException err) {
			return new ResponseEntity<>(new HttpError("Cannot process flightId: " + flightId, 400), HttpStatus.BAD_REQUEST);
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
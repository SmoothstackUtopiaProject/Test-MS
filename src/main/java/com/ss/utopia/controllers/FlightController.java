package com.ss.utopia.controllers;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

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


import com.ss.utopia.models.Flight;
import com.ss.utopia.services.FlightService;

@RestController
@RequestMapping(value = "/flights")
public class FlightController {
	
	@Autowired
	FlightService flightService;
	
	
	@GetMapping()
	public ResponseEntity<Object> getAll(){
		List<Flight> allFlights = flightService.getAll();
		return !allFlights.isEmpty() ? new ResponseEntity<>(allFlights, HttpStatus.OK)
				: new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("id/{flightId}")
	public ResponseEntity<Flight> findById(@PathVariable Integer flightId) {
		return flightService.findById(flightId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	@PostMapping()
	public ResponseEntity<Flight> create(@Valid @RequestBody Flight flight ) {
		return new ResponseEntity<>(flightService.insert(flight), HttpStatus.CREATED);
	}
	
	@PutMapping("id/{flightId}")
	public ResponseEntity<Flight> update(@Valid @RequestBody Flight flight, @PathVariable Integer flightId){
		return flightService.findById(flightId)
				.map(flightObj -> {
					flight.setId(flightObj.getId());
					return ResponseEntity.ok(flightService.update(flight));
				})
				.orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	@DeleteMapping("id/{flightId}")
	public ResponseEntity<Flight> delete(@PathVariable Integer flightId) {
		return flightService.findById(flightId)
				.map(flight -> {
					flightService.delete(flightId);
					return ResponseEntity.ok(flight);
				})
				.orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	
	@GetMapping("/search/{routeId}")
	public ResponseEntity<Object> search(@PathVariable String routeId, @PathParam(value = "date") String date){
		List<Flight> all = flightService.search(routeId, date);
		return !all.isEmpty() ? new ResponseEntity<>(all, HttpStatus.OK)
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
	
//	@ExceptionHandler(ConstraintViolationException.class)
//	public ResponseEntity<Object> invalggidMessage() {
//		return new ResponseEntity<>("Invalid Message Content!", HttpStatus.BAD_REQUEST);
//	}
	

	
	
	
	
	
	

}

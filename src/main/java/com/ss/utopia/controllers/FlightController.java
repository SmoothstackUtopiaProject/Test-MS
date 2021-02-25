package com.utopia.flight.controller;

import java.net.ConnectException;
import java.sql.SQLException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.utopia.flight.model.Flight;
import com.utopia.flight.service.FlightService;

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
	
	@GetMapping("id/{flightId}") 
	public ResponseEntity<Flight> findById(@PathVariable Integer flightId) throws ConnectException, SQLException {
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
	
	@GetMapping("/search")
	public ResponseEntity<Object> search(@RequestParam String orig, @RequestParam String dest, @RequestParam String date, @RequestParam Integer travelers)
			throws ConnectException, SQLException{
		List<Flight> all = flightService.search(orig, dest, date, travelers);
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
	
	
	

	
	
	
	
	
	

}

package com.ss.utopia.controllers;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.ss.utopia.exceptions.AirplaneNotFoundException;
import com.ss.utopia.exceptions.AirplaneTypeNotFoundException;
import com.ss.utopia.models.Airplane;
import com.ss.utopia.models.AirplaneType;
import com.ss.utopia.models.ErrorMessage;
import com.ss.utopia.services.AirplaneService;

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

@RestController
@RequestMapping("/airplanes")
public class AirplaneController {
	
	@Autowired
	private AirplaneService airplaneService;
	
	@GetMapping("/health")
	public ResponseEntity<Object> health() {
		return new ResponseEntity<>("\"status\": \"up\"", HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<Object> findAll() {
		List<Airplane> airplanesList = airplaneService.findAll();
		return !airplanesList.isEmpty()
			? new ResponseEntity<>(airplanesList, HttpStatus.OK)
			: new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("{airplaneId}")
	public ResponseEntity<Object> findById(@PathVariable Integer airplaneId) 
	throws AirplaneNotFoundException {
		Airplane airplane = airplaneService.findById(airplaneId);
		return new ResponseEntity<>(airplane, HttpStatus.OK);
	}

	@GetMapping("/types")
	public ResponseEntity<Object> findAllTypes() {
		List<AirplaneType> airplaneTypesList = airplaneService.findAllAirplaneTypes();
		return !airplaneTypesList.isEmpty()
			? new ResponseEntity<>(airplaneTypesList, HttpStatus.OK)
			: new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@PostMapping("/search")
	public ResponseEntity<Object> findBySearchAndFilter(@RequestBody Map<String, String> filterMap) {
		List<Airplane> airplanesList = airplaneService.findBySearchAndFilter(filterMap);
		return !airplanesList.isEmpty()
			? new ResponseEntity<>(airplanesList, HttpStatus.OK)
			: new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@PostMapping
	public ResponseEntity<Object> insert(@RequestBody Map<String, String> airplaneMap) 
	throws AirplaneTypeNotFoundException {
		Integer airplaneTypeId = Integer.parseInt(airplaneMap.get("airplaneTypeId"));
		Airplane newAirplane = airplaneService.insert(airplaneTypeId);
		return new ResponseEntity<>(newAirplane, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<Object> update(@RequestBody Map<String, String> airplaneMap) 
	throws AirplaneNotFoundException, AirplaneTypeNotFoundException {
		Integer airplaneId = Integer.parseInt(airplaneMap.get("airplaneId"));
		Integer airplaneTypeId = Integer.parseInt(airplaneMap.get("airplaneTypeId"));
		Airplane newAirplane = airplaneService.update(airplaneId, airplaneTypeId);	
		return new ResponseEntity<>(newAirplane, HttpStatus.OK);
	}
	
	@DeleteMapping("{airplaneId}")
	public ResponseEntity<Object> delete(@PathVariable Integer airplaneId) 
	throws AirplaneNotFoundException {
		airplaneService.delete(airplaneId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@ExceptionHandler(AirplaneNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<Object> airplaneNotFoundException(Throwable err) {
		return new ResponseEntity<>(
			new ErrorMessage(err.getMessage()), 
			HttpStatus.NOT_FOUND
		);
	}

	@ExceptionHandler(AirplaneTypeNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<Object> airplaneTypeNotFoundException(Throwable err) {
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

package com.ss.utopia.controllers;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;

import com.ss.utopia.exceptions.AirplaneNotFoundException;
import com.ss.utopia.exceptions.AirplaneTypeNotFoundException;
import com.ss.utopia.models.Airplane;
import com.ss.utopia.models.HttpError;
import com.ss.utopia.services.AirplaneService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.CrossOrigin;
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

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(
	value = "/airplanes",
	produces = { "application/json", "application/xml", "text/xml" },
	consumes = MediaType.ALL_VALUE
)
public class AirplaneController {
	
	@Autowired
	AirplaneService airplaneService;
	
	@GetMapping
	public ResponseEntity<Object> findAll(){
		List<Airplane> airplaneList = airplaneService.findAll();
		if(airplaneList.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		} else return new ResponseEntity<>(airplaneList, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Object> findById(@PathVariable Integer id){
		try {
			Airplane airplane = airplaneService.findById(id);
			return new ResponseEntity<>(airplane, HttpStatus.OK);
		} catch (AirplaneNotFoundException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 404), HttpStatus.NOT_FOUND);	
		} catch(IllegalArgumentException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 400), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/type/{id}")
	public ResponseEntity<Object> findByTypeId(@PathVariable Integer id){
		try {
			List<Airplane> airplane = airplaneService.findByTypeId(id);
			return new ResponseEntity<>(airplane, HttpStatus.OK);
		} catch (AirplaneTypeNotFoundException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 404), HttpStatus.NOT_FOUND);	
		} catch(IllegalArgumentException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 400), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping
	public ResponseEntity<Object> insert(@RequestBody String body) {
		try {
			Integer typeId = Integer.parseInt(body);
			Airplane newAirplane = airplaneService.insert(typeId);
			return new ResponseEntity<>(newAirplane, HttpStatus.CREATED);
		}	catch(AirplaneTypeNotFoundException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 404), HttpStatus.NOT_FOUND);
		} catch(IllegalArgumentException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 400), HttpStatus.BAD_REQUEST);
		}	
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		try {
			airplaneService.delete(id);
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		} catch(AirplaneNotFoundException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 404), HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping
	public ResponseEntity<Object> update(@RequestBody Airplane airplane) {
		try {
			Airplane newAirplane = airplaneService.update(airplane);
			return new ResponseEntity<>(newAirplane, HttpStatus.OK);
		}	catch (AirplaneNotFoundException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 404), HttpStatus.NOT_FOUND);
		} catch (AirplaneTypeNotFoundException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 400), HttpStatus.BAD_REQUEST);
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
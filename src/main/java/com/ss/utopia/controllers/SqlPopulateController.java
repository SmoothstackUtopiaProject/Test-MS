package com.ss.utopia.controllers;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;

import com.ss.utopia.models.Flight;
import com.ss.utopia.models.HttpError;
import com.ss.utopia.models.Route;
import com.ss.utopia.services.SqlPopulateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(
	value = "/populate",
	produces = { "application/json", "application/xml", "text/xml" },
	consumes = MediaType.ALL_VALUE
)
public class SqlPopulateController {
	
	@Autowired
	SqlPopulateService sqlPopulateService;
	
	@GetMapping("/sqlflights")
	public ResponseEntity<Object> populateFlights(){
		List<Flight> flightList = sqlPopulateService.populateFlights();
    return new ResponseEntity<>(flightList, HttpStatus.OK);
	}

  @GetMapping("/sqlroutes")
	public ResponseEntity<Object> populateRoutes(){
		List<Route> routeList = sqlPopulateService.populateRoutes();
    return new ResponseEntity<>(routeList, HttpStatus.OK);
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
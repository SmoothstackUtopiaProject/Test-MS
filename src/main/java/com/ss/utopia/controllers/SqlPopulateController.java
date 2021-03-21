package com.ss.utopia.controllers;

import java.util.List;

import com.ss.utopia.models.Airplane;
import com.ss.utopia.models.Airport;
import com.ss.utopia.models.Booking;
import com.ss.utopia.models.Flight;
import com.ss.utopia.models.Route;
import com.ss.utopia.services.SqlPopulateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/sql")
public class SqlPopulateController {
	
	@Autowired
	private SqlPopulateService sqlPopulateService;
	
	@GetMapping("/airplanes/populate")
	public ResponseEntity<Object> populateAirplanes(){
		List<Airplane> airplaneList = sqlPopulateService.populateAirplanes();
    return new ResponseEntity<>(airplaneList, HttpStatus.OK);
	}

	@GetMapping("/airports/populate")
	public ResponseEntity<Object> populateAirports(){
		List<Airport> airportList = sqlPopulateService.populateAirports();
    return new ResponseEntity<>(airportList, HttpStatus.OK);
	}
	
	@GetMapping("/bookings/populate")
	public ResponseEntity<Object> populateBookings(){
		List<Booking> bookingList = sqlPopulateService.populateBookings();
    return new ResponseEntity<>(bookingList, HttpStatus.OK);
	}

	@GetMapping("/flights/populate")
	public ResponseEntity<Object> populateFlights(){
		List<Flight> flightList = sqlPopulateService.populateFlights();
    return new ResponseEntity<>(flightList, HttpStatus.OK);
	}

  @GetMapping("/routes/populate")
	public ResponseEntity<Object> populateRoutes(){
		List<Route> routeList = sqlPopulateService.populateRoutes();
    return new ResponseEntity<>(routeList, HttpStatus.OK);
	}
}

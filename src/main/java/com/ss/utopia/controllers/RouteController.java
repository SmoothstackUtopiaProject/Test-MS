package com.ss.utopia.controllers;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.Map;
import java.util.List;

import com.ss.utopia.exceptions.AirportNotFoundException;
import com.ss.utopia.exceptions.RouteAlreadyExistsException;
import com.ss.utopia.exceptions.RouteNotFoundException;
import com.ss.utopia.models.ErrorMessage;
import com.ss.utopia.models.Route;
import com.ss.utopia.services.RouteService;

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
@RequestMapping("/routes")
public class RouteController {
	
	@Autowired
	private RouteService routeService;
	
	@GetMapping("/health")
	public ResponseEntity<Object> health() {
		return new ResponseEntity<>("\"status\": \"up\"", HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<Object> findAllRoutes(){
		List<Route> routeList = routeService.findAll();
		return !routeList.isEmpty()
			? new ResponseEntity<>(routeList, HttpStatus.OK)
			: new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Object> findById(@PathVariable Integer id) throws RouteNotFoundException{
		Route route = routeService.findById(id);
		return new ResponseEntity<>(route, HttpStatus.OK);
	}

	@PostMapping("/search")
	public ResponseEntity<Object> findBySearchAndFilter(@RequestBody Map<String, String> filterMap) {
		List<Route> routeList = routeService.findBySearchAndFilter(filterMap);
		return !routeList.isEmpty()
			? new ResponseEntity<>(routeList, HttpStatus.OK)
			: new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@PostMapping
	public ResponseEntity<Object> insert(@RequestBody Map<String, String> routeMap) 
	throws AirportNotFoundException, RouteAlreadyExistsException {
		String routeOriginIataId = routeMap.get("routeOriginIataId");
		String routeDestinationIataId = routeMap.get("routeDestinationIataId");
		Route newRoute = routeService.insert(routeOriginIataId, routeDestinationIataId);
		return new ResponseEntity<>(newRoute, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<Object> update(@RequestBody Map<String, String> routeMap) 
	throws AirportNotFoundException, RouteAlreadyExistsException, RouteNotFoundException {
		Integer routeId = Integer.parseInt(routeMap.get("routeId"));
		String routeOriginIataId = routeMap.get("routeOriginIataId");
		String routeDestinationIataId = routeMap.get("routeDestinationIataId");
		Route newRoute = routeService.update(routeId, routeOriginIataId, routeDestinationIataId);
		return new ResponseEntity<>(newRoute, HttpStatus.OK);
	}
	
	@DeleteMapping("{routeId}")
	public ResponseEntity<Object> delete(@PathVariable Integer routeId) throws RouteNotFoundException {
		routeService.deleteById(routeId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	// Exception Handling
	// ========================================================================
	@ExceptionHandler(AirportNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<Object> airportNotFoundException(Throwable err) {
		return new ResponseEntity<>(
			new ErrorMessage(err.getMessage()), 
			HttpStatus.NOT_FOUND
		);
	}
	
	@ExceptionHandler(RouteAlreadyExistsException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ResponseEntity<Object> routeAlreadyExistsException(Throwable err) {
		return new ResponseEntity<>(
			new ErrorMessage(err.getMessage()), 
			HttpStatus.CONFLICT
		);
	}

	@ExceptionHandler(RouteNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<Object> routeNotFoundException(Throwable err) {
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
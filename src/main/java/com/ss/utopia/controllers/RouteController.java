package com.ss.utopia.controllers;

import java.util.List;

import com.ss.utopia.exceptions.AirportNotFoundException;
import com.ss.utopia.exceptions.RouteAlreadyExistsException;
import com.ss.utopia.exceptions.RouteNotFoundException;
import com.ss.utopia.models.Route;
import com.ss.utopia.services.RouteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/routes", produces = { "application/json", "application/xml", "text/xml"}, consumes = MediaType.ALL_VALUE)
public class RouteController {
	
	@Autowired
	RouteService routeService;
	
	@GetMapping
	public ResponseEntity<List<Route>> findAllRoutes(){
		List<Route> routeList = routeService.findAllRoutes();
		if(routeList.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		} else return new ResponseEntity<>(routeList, HttpStatus.OK);
	}
	
	@GetMapping("/id")
	public ResponseEntity<Route> findById(@RequestParam Integer id){
		Route theRoute =  routeService.findById(id);
		if(theRoute == null ) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		} else return new ResponseEntity<>(theRoute, HttpStatus.OK);
	}
	
	@GetMapping("/destination")
	public ResponseEntity<List<Route>> findByDestination(@RequestParam String dest){
		List<Route>routeList =  routeService.findByDestination(dest);
		if(routeList.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		} else return new ResponseEntity<>(routeList, HttpStatus.OK);
	}
	
	@GetMapping("/origin")
	public ResponseEntity<List<Route>> findByOrigin(@RequestParam String orig){
		List<Route>routeList =  routeService.findByOrigin(orig);
		if(routeList.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		} else return new ResponseEntity<>(routeList, HttpStatus.OK);
	}
	
	@GetMapping("/destOrig")
	public ResponseEntity<Route> findByDestinationAndOrigin(@RequestParam String dest, @RequestParam String orig){
		Route theRoute =  routeService.findByDestinationAndOrigin(dest, orig);
		if(theRoute == null) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		} else return new ResponseEntity<>(theRoute, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Route> insert(@RequestBody Route route) {
		try {
			Route newRoute = routeService.insert(route);
			return new ResponseEntity<>(newRoute, HttpStatus.CREATED);
		}	catch (AirportNotFoundException err) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}	catch(RouteAlreadyExistsException err) {
			return new ResponseEntity<>(null, HttpStatus.CONFLICT);
		} 
	}
	
	@DeleteMapping
	public ResponseEntity<Route> delete(@RequestParam Integer id) {
		try {
			routeService.deleteById(id);
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		} catch(RouteNotFoundException err) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping
	public ResponseEntity<Route> update(@RequestBody Route route) {
		try {
			Route newRoute = routeService.update(route);
			return new ResponseEntity<>(newRoute, HttpStatus.NO_CONTENT);
		}	catch (AirportNotFoundException err) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		} 	catch (RouteAlreadyExistsException err) {
			return new ResponseEntity<>(null, HttpStatus.CONFLICT);
		}
	}
	
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<Object> invalidRequestContent() {
		return new ResponseEntity<>("Invalid Message Content!", HttpStatus.BAD_REQUEST);
	}
	
}
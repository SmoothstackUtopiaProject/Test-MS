package com.ss.utopia;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(
	produces = { "application/json", "application/xml", "text/xml" },
	consumes = MediaType.ALL_VALUE
)
public class TestMSController {

  @Autowired
	RestTemplate restTemplate;

	@RequestMapping(path = "/services")
	public ResponseEntity<Object> services() {
		List<String> services = Arrays.asList(
			"airplane-service",
			"airport-service",
			"booking-service",
			"flight-service",
			"orchestrator-service",
			"passenger-service",
			"route-service",
			"user-service"
		);
		return new ResponseEntity<>(services.toString(), HttpStatus.OK);
	}

	@RequestMapping(path = "/actuator/health")
	public ResponseEntity<Object> health() {
		return new ResponseEntity<>("\"status\": \"up\"", HttpStatus.OK);
	}
}
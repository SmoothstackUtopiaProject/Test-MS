package com.ss.utopia;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/status")
public class TestMSController {

  @Autowired
	RestTemplate restTemplate;

	@GetMapping()
	public ResponseEntity<Object> health() {
		return new ResponseEntity<>("\"status\": \"up\"", HttpStatus.OK);
	}

	@GetMapping("/services")
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
		return new ResponseEntity<>(services, HttpStatus.OK);
	}
}
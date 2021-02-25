package com.ss.utopia.services;

import java.net.ConnectException;
import java.sql.SQLException;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ss.utopia.models.Flight;
import com.ss.utopia.repositories.FlightRespository;

@Service
public class FlightService {

	@Autowired
	FlightRespository flightRespository;

	public List<Flight> findAll() throws ConnectException {
		List<Flight> all = flightRespository.findAllFlights();
		return all;
	}
	
    public Optional<Flight> findById(Integer id) {
        return flightRespository.findById(id);
    }

	public Flight insert(Flight flight) {
		return flightRespository.save(flight);
	}

	public Flight update(Flight flight) {
		return flightRespository.save(flight);
	}

	public void delete(Integer id) {
		flightRespository.deleteById(id);
	}

	// search for flights, given route id and date
	public List<Flight> search(String orig, String dest, String date, Integer travelers) 
			throws ConnectException, IllegalArgumentException, SQLException{
		Date stringToDate = Date.valueOf(date);
		return flightRespository.searchFlightByOrigDestDateSeat(orig, dest, stringToDate, travelers);
	}
}
package com.ss.utopia.services;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ss.utopia.models.Airport;
import com.ss.utopia.models.Flight;
import com.ss.utopia.models.Route;

@Service
public class SqlPopulateService {
	
  @Autowired
  private AirportService airportService;

	@Autowired
	private FlightService flightService;

  @Autowired
  private RouteService routeService;

	public List<Flight> populateFlights() {
    List<Flight> flightList = flightService.findAll();
    List<Route> routeList = routeService.findAllRoutes();

    Random random = new Random(980610);

    for(Route route : routeList) {
      System.out.println("Route origin: " + route.getOrigin() + " to destination: " + route.getDestination());
      for(int month = 1; month < 13; month++) {
        for(int day = 1; day < 3; day++) {

          if(random.nextInt(2) == 1) {
            int hour = random.nextInt(11) + 1;
            int minute = random.nextInt(58) + 1;
            try {
              Flight flight = flightService.insert(
                route.getId(), 1, "2021" + "-" + 
                (month < 10 ? "0" + month : month) + "-" + 
                (day < 10 ? "01" : day * 15) + " " +
                (hour < 10 ? "0" + hour : hour) + ":" + 
                (minute < 10 ? "0" + minute : minute) + ":00",
                1, 2, "GROUNDED"
              );
              
              System.out.println(
                "Added Flight ID: " + flight.getId() + 
                " origin: " + route.getOrigin() + 
                " to destination: " + route.getDestination() +
                " on date: " + flight.getDateTime()
              );

              flightList.add(flight);
            } catch(Exception err){
              System.out.println(err.getMessage());
            }
          }
        }
      }
    }
    return flightList;
	}

  public List<Route> populateRoutes() {
    List<Airport> airportList = airportService.findAll();
    List<Route> routeList = routeService.findAllRoutes();

    for(Airport origin : airportList) {
      for(Airport destination : airportList) {
        
        try {
          Route route = routeService.insert(origin.getAirportIataId(), destination.getAirportIataId());
          System.out.println("Added Route origin: " + route.getOrigin() + " to destination: " + route.getDestination());
          routeList.add(route);
        } catch(Exception err){
          System.out.println(err.getMessage());
        }
      }
    }
    return routeList;
	}
}
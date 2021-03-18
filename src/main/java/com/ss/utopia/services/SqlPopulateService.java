package com.ss.utopia.services;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ss.utopia.models.Airplane;
import com.ss.utopia.models.Airport;
import com.ss.utopia.models.Flight;
import com.ss.utopia.models.Route;

@Service
public class SqlPopulateService {
	
  @Autowired
  private AirplaneService airplaneService;

  @Autowired
  private AirportService airportService;

	@Autowired
	private FlightService flightService;

  @Autowired
  private RouteService routeService;

	public List<Flight> populateFlights() {
    List<Airplane> airplaneList = airplaneService.findAll();
    List<Flight> flightList = flightService.findAll();
    List<Route> routeList = routeService.findAll();

    Random random = new Random(980610);

    Integer index = 1;
    for(Route route : routeList) {
      System.out.println("Route origin: " + route.getRouteOriginIataId() + 
        " to destination: " + route.getRouteDestinationIataId()
      );

      
      for(int month = 1; month < 13; month++) {
        for(int day = 1; day < 3; day++) {

          if(random.nextInt(2) == 1) {
            int airplaneIndex = random.nextInt(airplaneList.size());
            int hour = random.nextInt(11) + 1;
            int minute = random.nextInt(58) + 1;
            int duration = random.nextInt(20000) + 7201;

            try {
              Flight flight = flightService.insert(
                index,
                route.getRouteId(), airplaneList.get(airplaneIndex).getAirplaneId(),
                "2021" + "-" + 
                (month < 10 ? "0" + month : month) + "-" + 
                (day < 10 ? "01" : day * 15) + " " +
                (hour < 10 ? "0" + hour : hour) + ":" + 
                (minute < 10 ? "0" + minute : minute) + ":00",
                index, duration, "GROUNDED"
              );
              
              System.out.println(
                "Added Flight ID: " + flight.getFlightId() + 
                " origin: " + route.getRouteOriginIataId() + 
                " to destination: " + route.getRouteDestinationIataId() +
                " on date: " + flight.getFlightDepartureTime()
              );

              flightList.add(flight);
              index++;
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
    List<Route> routeList = routeService.findAll();

    for(Airport origin : airportList) {
      for(Airport destination : airportList) {
        
        try {
          Route route = routeService.insert(origin.getAirportIataId(), destination.getAirportIataId());
          System.out.println(
            "Added Route origin: " + route.getRouteOriginIataId() + 
            " to destination: " + route.getRouteDestinationIataId()
          );
          
            routeList.add(route);
        } catch(Exception err){
          System.out.println(err.getMessage());
        }
      }
    }
    return routeList;
	}
}
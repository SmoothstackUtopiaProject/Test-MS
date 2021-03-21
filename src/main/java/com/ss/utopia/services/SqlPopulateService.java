package com.ss.utopia.services;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ss.utopia.exceptions.AirplaneTypeNotFoundException;
import com.ss.utopia.exceptions.AirportAlreadyExistsException;
import com.ss.utopia.models.Airplane;
import com.ss.utopia.models.AirplaneType;
import com.ss.utopia.models.Airport;
import com.ss.utopia.models.Booking;
import com.ss.utopia.models.Flight;
import com.ss.utopia.models.Route;
import com.ss.utopia.sqldata.SQLPopulate;

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

  private static final Integer UTOPIA_RANDOM_SEED = 123;
  private static final Integer UTOPIA_AIRPLANE_FLEET_SIZE = 10;

  public List<Airplane> populateAirplanes() {
    List<Airplane> exisitngAirplanes = airplaneService.findAll();
    List<AirplaneType> airplaneTypes = populateAirplaneTypes();
    
    Integer airplaneCount = exisitngAirplanes.size();
    if(airplaneCount < UTOPIA_AIRPLANE_FLEET_SIZE) {

      Random randomTypeSelector = new Random(UTOPIA_RANDOM_SEED);
      for(int i = airplaneCount; i < UTOPIA_AIRPLANE_FLEET_SIZE; i++) {
        try {
          AirplaneType randomType = airplaneTypes.get(randomTypeSelector.nextInt(airplaneTypes.size()));
          exisitngAirplanes.add(airplaneService.insert(randomType.getAirplaneTypeId()));
          System.out.println(
            "UtopiaAirlines welcomes our new: " + randomType.getAirplaneTypeName() + " to the Utopia Fleet!"
          );
        } 
        catch(AirplaneTypeNotFoundException err) {
          /* Ignore AirplaneTypeNotFound as we're passing a list of AirplaneTypes */
        }
      }
    } 
    else {
      System.out.println("The Utopia Fleet is already of the desired size: " + UTOPIA_AIRPLANE_FLEET_SIZE);
    }
    return exisitngAirplanes;
  }

  private List<AirplaneType> populateAirplaneTypes() {
    List<AirplaneType> exisitngAirplaneTypes = airplaneService.findAllAirplaneTypes();
    String[][] prePopulateAirplaneTypes = SQLPopulate.getAirplaneTypes();

    for(int i = 0; i < prePopulateAirplaneTypes.length; i++) {
      final Integer index = i;

      boolean itemAlreadyExists = false;
      for(int ii = 0; ii < exisitngAirplaneTypes.size(); ii++) {
        if(exisitngAirplaneTypes.get(ii).getAirplaneTypeName().equals(prePopulateAirplaneTypes[index][0])) {
          itemAlreadyExists = true;
        }
      }

      if(!itemAlreadyExists) {
        exisitngAirplaneTypes.add(
          airplaneService.insertAirplaneType(
            prePopulateAirplaneTypes[index][0],
            Integer.parseInt(prePopulateAirplaneTypes[index][1]),
            Integer.parseInt(prePopulateAirplaneTypes[index][2]),
            Integer.parseInt(prePopulateAirplaneTypes[index][3]),
            Integer.parseInt(prePopulateAirplaneTypes[index][4]),
            Integer.parseInt(prePopulateAirplaneTypes[index][5]),
            Integer.parseInt(prePopulateAirplaneTypes[index][6]),
            Integer.parseInt(prePopulateAirplaneTypes[index][7]),
            prePopulateAirplaneTypes[index][8]
          )
        );
        System.out.println("AirplaneType: " + prePopulateAirplaneTypes[i][0] + " has been added.");
      } 
      else {
        System.out.println("AirplaneType: " + prePopulateAirplaneTypes[i][0] + " already exists.");
      }
    }
    return exisitngAirplaneTypes;
  }

  public List<Airport> populateAirports() {
    List<Airport> exisitngAirports = airportService.findAll();
    String[][] prePopulateAirports = SQLPopulate.getAirports();

    for(int i = 0; i < prePopulateAirports.length; i++) {
      final Integer index = i;
      try {
        exisitngAirports.add(
          airportService.insert(
            prePopulateAirports[index][0],
            prePopulateAirports[index][1],
            prePopulateAirports[index][2]
          )
        );
        System.out.println("Airport: " + prePopulateAirports[i][0] + " has been added.");
      }
      catch(AirportAlreadyExistsException err) {
        System.out.println("Airport: " + prePopulateAirports[i][0] + " already exists.");
      }
    }
    return exisitngAirports;
  }

  public List<Booking> populateBookings() {
    return null;
  }

	public List<Flight> populateFlights() {
    List<Airplane> airplaneList = airplaneService.findAll();
    List<Flight> flightList = flightService.findAll();
    List<Route> routeList = routeService.findAll();

    Random random = new Random(UTOPIA_RANDOM_SEED);

    Integer index = 1;
    for(Route route : routeList) {
      System.out.println("Route origin: " + route.getRouteOriginIataId() + 
        " to destination: " + route.getRouteDestinationIataId()
      );

      for(int month = 1; month < 13; month++) {
        for(int day = 1; day < 3; day++) {

          if(random.nextInt(3) == 1) {
            int airplaneIndex = random.nextInt(airplaneList.size());
            int hour = random.nextInt(11) + 1;
            int minute = random.nextInt(58) + 1;
            int duration = random.nextInt(20000) + 7201;

            try {
              Flight flight = flightService.insert(
                route.getRouteId(), airplaneList.get(airplaneIndex).getAirplaneId(),
                "2021" + "-" + 
                (month < 10 ? "0" + month : month) + "-" + 
                (day < 10 ? "01" : day * 15) + " " +
                (hour < 10 ? ("0" + hour) : hour) + ":" + 
                (minute < 10 ? ("0" + minute ): minute) + ":00",
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
            } 
            catch(Exception err){
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
        } 
        catch(Exception err){
          System.out.println(err.getMessage());
        }
      }
    }
    return routeList;
	}
}
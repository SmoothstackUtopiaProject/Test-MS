package com.ss.utopia.filters;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import com.ss.utopia.models.FlightWithReferenceData;
import com.ss.utopia.timeformatting.FlightTimeFormatter;

public final class FlightFilters {

  private static final String FLIGHT_AIRPLANE_ID = "flightAirplaneId";
  private static final String FLIGHT_AIRPLANE_TYPE_NAME = "flightAirplaneTypeName";
  private static final String FLIGHT_DEPARTURE_TIME = "flightDepartureTime";
  private static final String FLIGHT_DEPARTURE_TIME_AFTER = "flightDepartureTimeAfter";
  private static final String FLIGHT_DEPARTURE_TIME_BEFORE = "flightDepartureTimeBefore";
  private static final String FLIGHT_DURATION = "flightDuration";
  private static final String FLIGHT_DURATION_GREATER_THAN = "flightDurationGreaterThan";
  private static final String FLIGHT_DURATION_LESS_THAN = "flightDurationLessThan";
  private static final String FLIGHT_ID = "flightId";
  private static final String FLIGHT_ROUTE_ORIGIN_IATA_ID = "flightRouteOriginIataId";
  private static final String FLIGHT_ROUTE_DESTINATION_IATA_ID = "flightRouteDestinationIataId";
  private static final String FLIGHT_ROUTE_ORIGIN_CITY_NAME = "flightRouteOriginCityName";
  private static final String FLIGHT_ROUTE_DESTINATION_CITY_NAME = "flightRouteDestinationCityName";
  private static final String FLIGHT_SEATING_ID = "flightSeatingId";
  private static final String FLIGHT_STATUS = "flightStatus";
  private static final String SEARCH_TERMS = "searchTerms";

  private FlightFilters() {
    throw new IllegalStateException("Utilility class 'FlightFilters' is static and should not be instantiated.");
  }

  // Filter Mapping
  public static List<FlightWithReferenceData> apply(Collection<FlightWithReferenceData> flights, 
  Map<String, String> filterMap) {
    
    // ID
		if(filterMap.keySet().contains(FLIGHT_ID)) {
      Integer parsedFlightId = Integer.parseInt(filterMap.get(FLIGHT_ID));
			flights = FlightFilters.filterByFlightId(flights, parsedFlightId);
		}

		// Route Origin IATA ID
		if(filterMap.keySet().contains(FLIGHT_ROUTE_ORIGIN_IATA_ID)) {
			flights = FlightFilters.filterByFlightRouteOriginIataId(
        flights, filterMap.get(FLIGHT_ROUTE_ORIGIN_IATA_ID)
      );
		}

		// Route Destination IATA ID
		if(filterMap.keySet().contains(FLIGHT_ROUTE_DESTINATION_IATA_ID)) {
			flights = FlightFilters.filterByFlightRouteDestinationIataId(
        flights, filterMap.get(FLIGHT_ROUTE_DESTINATION_IATA_ID)
      );
		}

    		// Route Origin City Name
		if(filterMap.keySet().contains(FLIGHT_ROUTE_ORIGIN_CITY_NAME)) {
			flights = FlightFilters.filterByFlightRouteOriginCityName(
        flights, filterMap.get(FLIGHT_ROUTE_ORIGIN_CITY_NAME)
      );
		}

		// Route Destination City Name
		if(filterMap.keySet().contains(FLIGHT_ROUTE_DESTINATION_CITY_NAME)) {
			flights = FlightFilters.filterByFlightRouteDestinationCityName(
        flights, filterMap.get(FLIGHT_ROUTE_DESTINATION_CITY_NAME)
      );
		}

    // Airplane ID
		if(filterMap.keySet().contains(FLIGHT_AIRPLANE_ID)) {
      Integer parsedFlightAirplaneId = Integer.parseInt(filterMap.get(FLIGHT_AIRPLANE_ID));
			flights = FlightFilters.filterByFlightAirplaneId(flights, parsedFlightAirplaneId);
		}

    // Airplane Type Name
		if(filterMap.keySet().contains(FLIGHT_AIRPLANE_TYPE_NAME)) {
			flights = FlightFilters.filterByFlightAirplaneTypeName(
        flights, filterMap.get(FLIGHT_AIRPLANE_TYPE_NAME)
      );
		}

    // Departure Time - Exact Match
		if(filterMap.keySet().contains(FLIGHT_DEPARTURE_TIME)) {
			flights = FlightFilters.filterByFlightDepartureTime(
        flights, filterMap.get(FLIGHT_DEPARTURE_TIME)
      );
		}

    // Departure Time - After
		if(filterMap.keySet().contains(FLIGHT_DEPARTURE_TIME_AFTER)) {
			flights = FlightFilters.filterByFlightDepartureTimeAfter(
        flights, filterMap.get(FLIGHT_DEPARTURE_TIME_AFTER)
      );
		}

    // Departure Time - Before
		if(filterMap.keySet().contains(FLIGHT_DEPARTURE_TIME_BEFORE)) {
			flights = FlightFilters.filterByFlightDepartureTimeBefore(
        flights, filterMap.get(FLIGHT_DEPARTURE_TIME_BEFORE)
      );
		}

    // Seating ID
		if(filterMap.keySet().contains(FLIGHT_SEATING_ID)) {
      Integer parsedFlightSeatingId = Integer.parseInt(filterMap.get(FLIGHT_SEATING_ID));
			flights = FlightFilters.filterByFlightSeatingId(flights, parsedFlightSeatingId);
		}

    // Duration - Exact Match
		if(filterMap.keySet().contains(FLIGHT_DURATION)) {
      Integer parsedFlightDuration = Integer.parseInt(filterMap.get(FLIGHT_DURATION));
			flights = FlightFilters.filterByFlightDuration(flights, parsedFlightDuration);
		}

    // Duration - Greater Than
    if(filterMap.keySet().contains(FLIGHT_DURATION_GREATER_THAN)) {
      Integer parsedFlightDuration = Integer.parseInt(filterMap.get(FLIGHT_DURATION_GREATER_THAN));
			flights = FlightFilters.filterByFlightDurationGreaterThan(flights, parsedFlightDuration);
		}

    // Duration - Less Than
    if(filterMap.keySet().contains(FLIGHT_DURATION_LESS_THAN)) {
      Integer parsedFlightDuration = Integer.parseInt(filterMap.get(FLIGHT_DURATION_LESS_THAN));
      flights = FlightFilters.filterByFlightDurationLessThan(flights, parsedFlightDuration);
    }

    // Status
		if(filterMap.keySet().contains(FLIGHT_STATUS)) {
			flights = FlightFilters.filterByFlightStatus(
        flights, filterMap.get(FLIGHT_STATUS)
      );
		}

    // SearchTerms
    if(filterMap.keySet().contains(SEARCH_TERMS)) {
      flights = FlightFilters.filterBySearchTerms(flights, filterMap.get(SEARCH_TERMS));
    }
		return flights.stream().collect(Collectors.toList());
  }

  // Filters
  // ==========================================================================================

  // ID
  public static List<FlightWithReferenceData> filterByFlightId 
  (Collection<FlightWithReferenceData> flights, Integer flightId) {
    return flights.parallelStream()
      .filter(i -> i.getFlightId().equals(flightId))
      .collect(Collectors.toList());
  }

  // Route Destination IATA ID
  public static List<FlightWithReferenceData> filterByFlightRouteDestinationIataId 
  (Collection<FlightWithReferenceData> flights, String flightRouteOriginIataId) {
    return flights.parallelStream()
      .filter(i -> i.getFlightRouteDestinationIataId().equals(flightRouteOriginIataId))
      .collect(Collectors.toList());
  }

  // Route Origin IATA ID
  public static List<FlightWithReferenceData> filterByFlightRouteOriginIataId 
  (Collection<FlightWithReferenceData> flights, String flightRouteDestinationIataId) {
    return flights.parallelStream()
      .filter(i -> i.getFlightRouteOriginIataId().equals(flightRouteDestinationIataId))
      .collect(Collectors.toList());
  }

  // Route Destination City Name
  public static List<FlightWithReferenceData> filterByFlightRouteDestinationCityName 
  (Collection<FlightWithReferenceData> flights, String flightRouteDestinationCityName) {
    return flights.parallelStream()
      .filter(i -> i.getFlightRouteDestinationCityName().equals(flightRouteDestinationCityName))
      .collect(Collectors.toList());
  }

  // Route Origin City Name
  public static List<FlightWithReferenceData> filterByFlightRouteOriginCityName 
  (Collection<FlightWithReferenceData> flights, String flightRouteOriginCityName) {
    return flights.parallelStream()
      .filter(i -> i.getFlightRouteOriginCityName().equals(flightRouteOriginCityName))
      .collect(Collectors.toList());
  }

  // Airplane ID
  public static List<FlightWithReferenceData> filterByFlightAirplaneId 
  (Collection<FlightWithReferenceData> flights, Integer flightAirplaneId) {
    return flights.parallelStream()
      .filter(i -> i.getFlightAirplaneId().equals(flightAirplaneId))
      .collect(Collectors.toList());
  }

  // Airplane Type Name
  public static List<FlightWithReferenceData> filterByFlightAirplaneTypeName 
  (Collection<FlightWithReferenceData> flights, String flightAirplaneTypeName) {
    return flights.parallelStream()
      .filter(i -> i.getFlightAirplaneTypeName().equals(flightAirplaneTypeName))
      .collect(Collectors.toList());
  }

  // Departure Time - Exact Match
  public static List<FlightWithReferenceData> filterByFlightDepartureTime 
  (Collection<FlightWithReferenceData> flights, String flightDepartureTime) {
    return flights.parallelStream()
      .filter(i -> i.getFlightDepartureTime().equals(flightDepartureTime))
      .collect(Collectors.toList());
  }

  // Departure Time - After
  public static List<FlightWithReferenceData> filterByFlightDepartureTimeAfter
  (Collection<FlightWithReferenceData> flights, String flightDepartureTime) {
    return flights.parallelStream()
      .filter(i -> 
        LocalDateTime.parse(i.getFlightDepartureTime(), FlightTimeFormatter.getInstance())
        .isAfter(LocalDateTime.parse(flightDepartureTime, FlightTimeFormatter.getInstance()))
      ) 
      .collect(Collectors.toList());
  }

  // Departure Time - Before
  public static List<FlightWithReferenceData> filterByFlightDepartureTimeBefore
  (Collection<FlightWithReferenceData> flights, String flightDepartureTime) {
    return flights.parallelStream()
      .filter(i -> 
        LocalDateTime.parse(i.getFlightDepartureTime(), FlightTimeFormatter.getInstance())
        .isBefore(LocalDateTime.parse(flightDepartureTime, FlightTimeFormatter.getInstance()))
      ) 
      .collect(Collectors.toList());
  }

  // Seating ID
  public static List<FlightWithReferenceData> filterByFlightSeatingId 
  (Collection<FlightWithReferenceData> flights, Integer flightSeatingId) {
    return flights.parallelStream()
      .filter(i -> i.getFlightSeatingId().equals(flightSeatingId))
      .collect(Collectors.toList());
  }

  // Duration - Exact Match
  public static List<FlightWithReferenceData> filterByFlightDuration 
  (Collection<FlightWithReferenceData> flights, Integer flightDuration) {
    return flights.parallelStream()
      .filter(i -> i.getFlightDuration().equals(flightDuration))
      .collect(Collectors.toList());
  }

  // Duration - Greater Than
  public static List<FlightWithReferenceData> filterByFlightDurationGreaterThan
  (Collection<FlightWithReferenceData> flights, Integer flightDuration) {
    return flights.parallelStream()
      .filter(i -> i.getFlightDuration() > flightDuration)
      .collect(Collectors.toList());
  }

  // Duration - Less Than
  public static List<FlightWithReferenceData> filterByFlightDurationLessThan
  (Collection<FlightWithReferenceData> flights, Integer flightDuration) {
    return flights.parallelStream()
      .filter(i -> i.getFlightDuration() < flightDuration)
      .collect(Collectors.toList());
  }

  // Status
  public static List<FlightWithReferenceData> filterByFlightStatus 
  (Collection<FlightWithReferenceData> flights, String flightStatus) {
    return flights.parallelStream()
      .filter(i -> i.getFlightStatus().equals(flightStatus))
      .collect(Collectors.toList());
  }

  // SearchTerms
  public static List<FlightWithReferenceData> filterBySearchTerms
  (Collection<FlightWithReferenceData> flights, String searchTerms) {
    String formattedSearch = searchTerms.toLowerCase(Locale.getDefault()).replace(", ", ",");
    String[] splitTerms = formattedSearch.split(",");

    for(String searchTerm : splitTerms) {
      flights = flights.parallelStream()
      .filter((FlightWithReferenceData i) ->
        i.getFlightId().toString().contains(searchTerm) ||
        i.getFlightRouteId().toString().contains(searchTerm) ||
        i.getFlightRouteOriginIataId().contains(searchTerm) ||
        i.getFlightRouteDestinationIataId().contains(searchTerm) ||
        i.getFlightRouteOriginCityName().contains(searchTerm) ||
        i.getFlightRouteDestinationCityName().contains(searchTerm) ||
        i.getFlightAirplaneId().toString().contains(searchTerm) ||
        i.getFlightAirplaneTypeName().contains(searchTerm) ||
        i.getFlightDepartureTime().contains(searchTerm) ||
        i.getFlightSeatingId().toString().contains(searchTerm) ||
        i.getFlightDuration().toString().contains(searchTerm) ||
        i.getFlightStatus().contains(searchTerm)
      ).collect(Collectors.toList());
    }
		return flights.stream().collect(Collectors.toList());
	}
}

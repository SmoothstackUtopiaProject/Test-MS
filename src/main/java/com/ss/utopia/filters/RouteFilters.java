package com.ss.utopia.filters;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import com.ss.utopia.models.Route;

public final class RouteFilters {

  private static final String ROUTE_ID = "routeId";
  private static final String ROUTE_ORIGIN_IATA_ID = "routeOriginIataId";
  private static final String ROUTE_DESTINATION_IATA_ID = "routeDestinationIataId";
  private static final String SEARCH_TERMS = "searchTerms";

  private RouteFilters() {
    throw new IllegalStateException("Utilility class 'RouteFilters' is static and should not be instantiated.");
  }

  public static List<Route> apply
  (List<Route> routes, Map<String, String> filterMap) {
    
    // ID
		if(filterMap.keySet().contains(ROUTE_ID)) {
      Integer parsedRouteId = Integer.parseInt(filterMap.get(ROUTE_ID));
			routes = RouteFilters.filterByRouteId(routes, parsedRouteId);
		}

		// Origin IATA ID
		if(filterMap.keySet().contains(ROUTE_ORIGIN_IATA_ID)) {
			routes = RouteFilters.filterByRouteOriginIataId(routes, filterMap.get(ROUTE_ORIGIN_IATA_ID));
		}

		// Destination IATA ID
		if(filterMap.keySet().contains(ROUTE_DESTINATION_IATA_ID)) {
			routes = RouteFilters.filterByRouteDestinationIataId(routes, filterMap.get(ROUTE_DESTINATION_IATA_ID));
		}

    // SearchTerms
    if(filterMap.keySet().contains(SEARCH_TERMS)) {
      routes = RouteFilters.filterBySearchTerms(routes, filterMap.get(SEARCH_TERMS));
    }
		return routes;
  }

  public static List<Route> filterByRouteId 
  (Collection<Route> routes, Integer routeId) {
    return routes.parallelStream()
      .filter(i -> i.getRouteId().equals(routeId))
      .collect(Collectors.toList());
  }

  public static List<Route> filterByRouteDestinationIataId 
  (Collection<Route> routes, String routeOriginIataId) {
    return routes.parallelStream()
      .filter(i -> i.getRouteDestinationIataId().equals(routeOriginIataId))
      .collect(Collectors.toList());
  }

  public static List<Route> filterByRouteOriginIataId 
  (Collection<Route> routes, String routeDestinationIataId) {
    return routes.parallelStream()
      .filter(i -> i.getRouteOriginIataId().equals(routeDestinationIataId))
      .collect(Collectors.toList());
  }

  public static List<Route> filterBySearchTerms
  (List<Route> routes, String searchTerms) {
    String formattedSearch = searchTerms.toLowerCase(Locale.getDefault()).replace(", ", ",");
    String[] splitTerms = formattedSearch.split(",");

    for(String searchTerm : splitTerms) {
      routes = routes.parallelStream()
      .filter((Route i) ->
        i.getRouteId().toString().contains(searchTerm) ||
        i.getRouteOriginIataId().contains(searchTerm) ||
        i.getRouteDestinationIataId().contains(searchTerm)
      ).collect(Collectors.toList());
    }
		return routes;
	}
}

package com.ss.utopia.sqldata;

public final class SQLPopulate {

  // Name - Capacity - 1st Class - BusClass - Coach - 
  // 1stClassColumns - BusClassColumns - CoachColumns
  // EmergencyExitRows
  private static final String[][] airplaneTypes = {
    { "Airbus A350", "304", "10", "24", "270", "2", "4", "6", "7-22-45" },
    { "Airbus A380", "496", "12", "40", "444", "2", "4", "6", "7-36-37-74"},
    { "Boeing 737", "202", "6", "16", "180", "2", "4", "6", "7-15-30" },
    { "Boeing 747", "400", "8", "32", "360", "2", "4", "6", "7-30-31-60" }   
  };

  private static final String[][] airports = {
    { "JFK", "John F. Kennedy International Airport", "New York" },
    { "LAX", "Los Angeles International Airport", "Los Angeles" },
    { "SFO", "San Francisco International Airport", "San Francisco" },
    { "DFW", "Dallas/Fort Worth International Airport", "Dallas" },
    { "ORD", "O'Hare International Airport", "Chicago" },
    { "ATL", "Hartsfield-Jackson Atlanta International Airport", "Chicago" },
    { "MIA", "Miami International Airport", "Miami" },
    { "DEN", "Denver International Airport", "Denver" },
    { "SEA", "Seattle-Tacoma International Airport", "Seattle" },
    { "LAS", "McCarran International Airport", "Las Vegas" },
    { "CLT", "Charlotte Douglas International Airport", "Charlotte" },
    { "PHX", "Phoenix Sky Harbor International Airport", "Phoenix" },
    { "EWR", "Newark Liberty International Airport", "Newark" },
    { "DCA", "Ronald Reagan National Airport", "Washington D.C." },
    { "IAD", "Dulles International Airport", "Washington D.C." },
    { "SAN", "San Diego International Airport", "San Diego" },
    { "PDX", "Portland International Airport", "Portland" }
  };

  private SQLPopulate() {
    throw new IllegalStateException("Utilility class 'SQLPrePopulateData' is static and should not be instantiated.");
  }

  public static String[][] getAirplaneTypes() {
    return airplaneTypes.clone();
  }

  public static String[][] getAirports() {
    return airports.clone();
  }
}

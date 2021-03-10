package com.ss.utopia.models;

public class FlightWithReferenceData {

  private Integer flightId;
  private Integer flightRouteId;
	private String flightRouteOriginIataId;
	private String flightRouteDestinationIataId;
  private String flightRouteOriginCityName;
  private String flightRouteDestinationCityName;
  private Integer flightAirplaneId;
	private String flightAirplaneTypeName;
  private String flightDepartureTime;
  private Integer flightSeatingId;
  private Integer flightDuration;
  private String flightStatus;

  public FlightWithReferenceData(
    Integer flightId,
    Integer flightRouteId,
		String flightRouteOriginIataId,
		String flightRouteDestinationIataId,
    String flightRouteOriginCityName,
    String flightRouteDestinationCityName,
    Integer flightAirplaneId,
		String flightAirplaneTypeName,
    String flightDepartureTime,
    Integer flightSeatingId,
    Integer flightDuration,
    String flightStatus
  ) {
    this.flightId = flightId;
    this.flightRouteId = flightRouteId;
		this.flightRouteOriginIataId = flightRouteOriginIataId;
		this.flightRouteDestinationIataId = flightRouteDestinationIataId;
    this.flightRouteOriginCityName = flightRouteOriginCityName;
    this.flightRouteDestinationCityName = flightRouteDestinationCityName;
    this.flightAirplaneId = flightAirplaneId;
		this.flightAirplaneTypeName = flightAirplaneTypeName;
    this.flightDepartureTime = flightDepartureTime;
    this.flightSeatingId = flightSeatingId;
    this.flightDuration = flightDuration;
    this.flightStatus = flightStatus;
  }

  public Integer getFlightId() {
    return flightId;
  }

  public void setFlightId(Integer flightId) {
    this.flightId = flightId;
  }

  public Integer getFlightRouteId() {
    return flightRouteId;
  }

  public void setFlightRouteId(Integer flightRouteId) {
    this.flightRouteId = flightRouteId;
  }

	public String getFlightRouteOriginIataId() {
    return flightRouteOriginIataId;
  }

  public void setFlightRouteOriginIataId(String flightRouteOriginIataId) {
    this.flightRouteOriginIataId = flightRouteOriginIataId;
  }

	public String getFlightRouteDestinationIataId() {
    return flightRouteDestinationIataId;
  }

  public void setFlightRouteDestinationIataId(String flightRouteDestinationIataId) {
    this.flightRouteDestinationIataId = flightRouteDestinationIataId;
  }

	public String getFlightRouteOriginCityName() {
    return flightRouteOriginCityName;
  }

  public void setFlightRouteOriginCityName(String flightRouteOriginCityName) {
    this.flightRouteOriginCityName = flightRouteOriginCityName;
  }

	public String getFlightRouteDestinationCityName() {
    return flightRouteDestinationCityName;
  }

  public void setFlightRouteDestinationCityName(String flightRouteDestinationCityName) {
    this.flightRouteDestinationCityName = flightRouteDestinationCityName;
  }

  public Integer getFlightAirplaneId() {
    return flightAirplaneId;
  }

  public void setFlightAirplaneId(Integer flightAirplaneId) {
    this.flightAirplaneId = flightAirplaneId;
  }

	public String getFlightAirplaneTypeName() {
    return flightAirplaneTypeName;
  }

  public void setFlightAirplaneTypeName(String flightAirplaneTypeName) {
    this.flightAirplaneTypeName = flightAirplaneTypeName;
  }

  public String getFlightDepartureTime() {
    return flightDepartureTime;
  }

  public void setFlightDepartureTime(String flightDepartureTime) {
    this.flightDepartureTime = flightDepartureTime;
  }

  public Integer getFlightSeatingId() {
    return flightSeatingId;
  }

  public void setFlightSeatingId(Integer flightSeatingId) {
    this.flightSeatingId = flightSeatingId;
  }

  public Integer getFlightDuration() {
    return flightDuration;
  }

  public void setFlightDuration(Integer flightDuration) {
    this.flightDuration = flightDuration;
  }

  public String getFlightStatus() {
    return flightStatus;
  }

  public void setFlightStatus(String flightStatus) {
    this.flightStatus = flightStatus;
  }
}
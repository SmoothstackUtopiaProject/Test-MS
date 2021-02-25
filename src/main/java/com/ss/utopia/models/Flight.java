package com.utopia.flight.model;



import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;



@Entity
@Table(name = "flight")
public class Flight {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@NotNull(message = "Route id should not be empty")
	@ManyToOne
	@JoinColumn(name = "route_id")
	private Route routeId;

	@NotNull(message = "Airplane id should not be empty")
	@Column(name = "airplane_id")
	private Integer airplaneId;

	@NotNull(message = "Departure date should not be empty")
	@Column(name = "departure_date")
	private LocalDate date;

	@NotNull(message = "Departure time should not be empty")
	@Column(name = "departure_time")
	private LocalTime time;

	@NotNull(message = "Seat price should not be empty")
	@Min(1)
	@Column(name = "seat_price")
	private Double seatPrice;

	@Column(name = "available_seats", nullable = true)
	private Integer availableSeats;

	public Flight() {
	};
	
	public Flight(Integer id, Route routeId, Integer airplaneId, LocalDate date, LocalTime time, double seatPrice) {
		super();
		this.id = id;
		this.routeId = routeId;
		this.airplaneId = airplaneId;
		this.date = date;
		this.time = time;
		this.seatPrice = seatPrice;
	}
	
	
	public Flight(Integer id, Route routeId, Integer airplaneId, LocalDate date, LocalTime time, double seatPrice,
			Integer availableSeats) {
		super();
		this.id = id;
		this.routeId = routeId;
		this.airplaneId = airplaneId;
		this.date = date;
		this.time = time;
		this.seatPrice = seatPrice;
		this.availableSeats = availableSeats;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Route getRouteId() {
		return routeId;
	}

	public void setRouteId(Route routeId) {
		this.routeId = routeId;
	}

	public Integer getAirplaneId() {
		return airplaneId;
	}

	public void setAirplaneId(Integer airplaneId) {
		this.airplaneId = airplaneId;
	}

	public double getSeatPrice() {
		return seatPrice;
	}

	public void setSeatPrice(double seatPrice) {
		this.seatPrice = seatPrice;
	}

	public Integer getAvailableSeats() {
		return availableSeats;
	}

	public void setAvailableSeats(Integer availableSeats) {
		this.availableSeats = availableSeats;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}
	
	

}

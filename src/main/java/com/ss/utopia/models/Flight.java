package com.ss.utopia.models;


import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "flight")
public class Flight {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@NotNull(message = "Route id should not be empty")
	@JoinColumn(name = "route_id")
	private Integer routeId;

	@NotNull(message = "Airplane id should not be empty")
	@Column(name = "airplane_id")
	private Integer airplaneId;

	@NotNull(message = "Departure time should not be empty")
	@Column(name = "departure_timestamp")
	private Timestamp dateTime;
	
	@NotNull(message = "Seating id should not be empty")
	@GeneratedValue
	@Column(name = "seating_id")
	private Integer seatingId;
	
	@NotNull(message="Duration should not be empty")
	@Column(name="duration")
	private Integer duration;
	
	@NotNull(message="Status should not be empty")
	@Column(name="status")
	private String status;
	
	public Flight() {
	}
	

	public Flight(Integer id, @NotNull(message = "Route id should not be empty") Integer routeId,
			@NotNull(message = "Airplane id should not be empty") Integer airplaneId,
			@NotNull(message = "Departure time should not be empty") Timestamp dateTime,
			@NotNull(message = "Seating id should not be empty") Integer seatingId,
			@NotNull(message = "Duration should not be empty") Integer duration,
			@NotNull(message = "Status should not be empty") String status) {
		super();
		this.id = id;
		this.routeId = routeId;
		this.airplaneId = airplaneId;
		this.dateTime = dateTime;
		this.seatingId = seatingId;
		this.duration = duration;
		this.status = status;
	}


	public Flight(Integer routeId, Integer airplaneId, Timestamp dateTime, Integer seatingId, Integer duration,String status) 
	{
		this.routeId = routeId;
		this.airplaneId = airplaneId;
		this.dateTime = dateTime;
		this.seatingId = seatingId;
		this.duration = duration;
		this.status = status;
	}

	public Flight(@NotNull(message = "Route id should not be empty") Integer routeId,
			@NotNull(message = "Airplane id should not be empty") Integer airplaneId,
			@NotNull(message = "Departure time should not be empty") Timestamp dateTime) {
		super();
		this.routeId = routeId;
		this.airplaneId = airplaneId;
		this.dateTime = dateTime;
	}



	public Timestamp getDateTime() {
		return dateTime;
	}

	public void setDateTime(Timestamp dateTime) {
		this.dateTime = dateTime;
	}

	public Integer getSeatingId() {
		return seatingId;
	}

	public void setSeatingId(Integer seatingId) {
		this.seatingId = seatingId;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRouteId() {
		return routeId;
	}

	public void setRouteId(Integer routeId) {
		this.routeId = routeId;
	}

	public Integer getAirplaneId() {
		return airplaneId;
	}

	public void setAirplaneId(Integer airplaneId) {
		this.airplaneId = airplaneId;
	}

}
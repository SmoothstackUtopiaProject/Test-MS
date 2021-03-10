package com.ss.utopia.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "airplane_type")
public class AirplaneType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer airplaneTypeId;
	
	@Column(name = "max_capacity")
	private Integer capacity;

	@Column(name = "name")
	private String airplaneTypeName;
	
	public AirplaneType() {}
	public AirplaneType(Integer airplaneTypeId) {
		this.airplaneTypeId = airplaneTypeId;
	}

	public Integer getAirplaneTypeId() {
		return airplaneTypeId;
	}

	public void setAirplaneTypeId(Integer airplaneTypeId) {
		this.airplaneTypeId = airplaneTypeId;
	}

	public String getAirplaneTypeName() {
		return airplaneTypeName;
	}

	public void setAirplaneTypeName(String airplaneTypeName) {
		this.airplaneTypeName = airplaneTypeName;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}
}
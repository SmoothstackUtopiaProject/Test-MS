package com.ss.utopia.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "airplane")
public class Airplane {
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer airplaneId;
	
	@NotNull(message = "Type ID should not be empty")
	@Column(name = "type_id", nullable = false)
	private Integer airplaneTypeId;

	public Airplane() {}
	public Airplane(Integer airplaneTypeId) {
		this.airplaneTypeId = airplaneTypeId;
	}
	public Airplane(Integer airplaneId, Integer airplaneTypeId) {
		this.airplaneId = airplaneId;
		this.airplaneTypeId = airplaneTypeId;
	}

	public Integer getAirplaneId() {
		return airplaneId;
	}

	public void setAirplaneId(Integer airplaneId) {
		this.airplaneId = airplaneId;
	}

	public Integer getAirplaneTypeId() {
		return airplaneTypeId;
	}

	public void setAirplaneTypeId(Integer airplaneTypeId) {
		this.airplaneTypeId = airplaneTypeId;
	}
}
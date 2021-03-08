package com.ss.utopia.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "route")
public class Route {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "origin_id")
	private String origin;

	@Column(name = "destination_id")
	private String destination;

	public Route() {}
	
	public Route(Integer id) {
		this.id = id;
	}

	public Route(Integer id, String origin, String destination) {
		this.id = id;
		this.origin = origin;
		this.destination = destination;
	}
	
	public Route(String origin, String destination) {
		this.origin = origin;
		this.destination = destination;
	}
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}
}
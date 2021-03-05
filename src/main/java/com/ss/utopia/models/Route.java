package com.ss.utopia.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "route")
public class Route {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "origin_id")
	private Airport origin;

	@ManyToOne
	@JoinColumn(name = "destination_id")
	private Airport destination;

	public Route() {
	}
	
	public Route(Integer id) {
		super();
		this.id = id;
	}

	public Route(Integer id, Airport origin, Airport destination) {
		super();
		this.id = id;
		this.origin = origin;
		this.destination = destination;
	}
	
	public Route(Airport origin, Airport destination) {
		super();
		this.origin = origin;
		this.destination = destination;
	}
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Airport getOrigin() {
		return origin;
	}

	public void setOrigin(Airport origin) {
		this.origin = origin;
	}

	public Airport getDestination() {
		return destination;
	}

	public void setDestination(Airport destination) {
		this.destination = destination;
	}

}
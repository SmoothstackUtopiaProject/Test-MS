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
	private Integer routeId;
	
	@Column(name = "origin_routeId")
	private String routeOriginIataId;

	@Column(name = "destination_routeId")
	private String routeDestinationIataId;

	public Route() {}
	public Route(Integer routeId, String routeOriginIataId, String routeDestinationIataId) {
		this.routeId = routeId;
		this.routeOriginIataId = routeOriginIataId;
		this.routeDestinationIataId = routeDestinationIataId;
	}
	
	public Route(String routeOriginIataId, String routeDestinationIataId) {
		this.routeOriginIataId = routeOriginIataId;
		this.routeDestinationIataId = routeDestinationIataId;
	}
	
	
	public Integer getRouteId() {
		return routeId;
	}

	public void setRouteId(Integer routeId) {
		this.routeId = routeId;
	}

	public String getRouteOriginIataId() {
		return routeOriginIataId;
	}

	public void setRouteOriginIataId(String routeOriginIataId) {
		this.routeOriginIataId = routeOriginIataId;
	}

	public String getRouteDestinationIataId() {
		return routeDestinationIataId;
	}

	public void setRouteDestinationIataId(String routeDestinationIataId) {
		this.routeDestinationIataId = routeDestinationIataId;
	}
}